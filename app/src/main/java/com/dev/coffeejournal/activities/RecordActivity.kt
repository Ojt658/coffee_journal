package com.dev.coffeejournal.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dev.coffeejournal.ui.AppBar
import com.dev.coffeejournal.data.models.CoffeeViewModel
import com.dev.coffeejournal.data.models.RecipeViewModel
import com.dev.coffeejournal.R
import com.dev.coffeejournal.data.databases.Coffee
import com.dev.coffeejournal.data.databases.CoffeeDatabase
import com.dev.coffeejournal.data.databases.RecipeDatabase
import com.dev.coffeejournal.data.models.CoffeeViewModelFactory
import com.dev.coffeejournal.data.models.RecipeViewModelFactory
import com.dev.coffeejournal.ui.theme.CoffeeJournalTheme
import com.dev.coffeejournal.ui.NewCoffeeForm

/**
 * Activity for recording a new coffee brew.
 *
 * This activity allows the user to record a new brew, including selecting an existing coffee
 * or adding a new one.
 */
class RecordActivity : ComponentActivity() {
    private val coffeeViewModel: CoffeeViewModel by viewModels {
        CoffeeViewModelFactory(
            CoffeeDatabase.getDatabase(applicationContext).coffeeDao()
        )
    }

    private val recipeViewModel: RecipeViewModel by viewModels {
        RecipeViewModelFactory(RecipeDatabase.getDatabase(applicationContext).recipeDao())
    }
    
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoffeeJournalTheme {
                var showAddCoffeeSheet by remember { mutableStateOf(false) }
                val addSheetState = rememberModalBottomSheetState()

                var newCoffeeSelection by remember { mutableStateOf<Coffee?>(null) }

                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar = {
                        AppBar(
                            "Record a Brew",
                            canNavigateBack = true,
                            navigateUp = { finish() })
                    }, floatingActionButton = {
                        FloatingActionButton(
                            onClick = {},
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.primary,
                            shape = CircleShape,
                            modifier = Modifier.padding(16.dp),
                            content = {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = "Add Button"
                                )
                            }
                        )
                    }
                ) { innerPadding ->
                    RecordPage(
                        coffeeViewModel,
                        newlyAddedCoffee = newCoffeeSelection,
                        onNewCoffeeClick = { showAddCoffeeSheet = true },
                        onNewCoffeeHandled = { newCoffeeSelection = null },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                if (showAddCoffeeSheet) {
                    ModalBottomSheet(
                        onDismissRequest = { showAddCoffeeSheet = false },
                        sheetState = addSheetState
                    ) {
                        NewCoffeeForm(onAddCoffee = { newCoffee ->
                            coffeeViewModel.addCoffee(newCoffee)
                            showAddCoffeeSheet = false
                            newCoffeeSelection = newCoffee
                        },
                            modifier = Modifier.imePadding())
                    }

                }
            }
        }
    }
}

/**
 * Composable that displays the page for recording a new coffee brew.
 *
 * @param viewModel The [CoffeeViewModel] for accessing coffee data.
 * @param newlyAddedCoffee A newly added coffee to be pre-selected.
 * @param onNewCoffeeHandled Callback to be invoked when the newly added coffee has been handled.
 * @param onNewCoffeeClick Callback to be invoked when the user clicks on "Add a new Coffee".
 * @param modifier The modifier to be applied to the composable.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordPage(viewModel: CoffeeViewModel, newlyAddedCoffee: Coffee?, onNewCoffeeHandled: () -> Unit, onNewCoffeeClick: () -> Unit, modifier: Modifier = Modifier) {
    val coffees by viewModel.allCoffees.collectAsState(initial = emptyList())
    var expandedCoffees by remember { mutableStateOf(false) }
    var selectedCoffee by remember { mutableStateOf<Coffee?>(null) }

    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(selectedCoffee) {
        if (selectedCoffee != null) {
            searchQuery = selectedCoffee?.name ?: ""
        }
    }

    val filteredCoffees = remember(searchQuery, coffees) {
        if (searchQuery.isBlank()) {
            coffees
        } else {
            coffees.filter { coffee ->
                coffee.name.contains(searchQuery, ignoreCase = true)
                }
        }
    }

    LaunchedEffect(newlyAddedCoffee) {
        if (newlyAddedCoffee != null) {
            selectedCoffee = newlyAddedCoffee
            onNewCoffeeHandled()
        }
    }
    
    val methods = listOf("Espresso", "Chemex", "V60", "AeroPress", "French Press", "Batch Brew")
    var expandedMethods by remember { mutableStateOf(false) }
    var selectedMethod by remember { mutableStateOf<String?>(null) }
    
    Box (modifier = modifier.fillMaxSize()) {
        Image (
            painter = painterResource(id = R.drawable.coffee_beans_background),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        
        ExposedDropdownMenuBox(
            expanded = expandedCoffees,
            onExpandedChange = { expandedCoffees = it }
        ) {
            TextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    if (it != selectedCoffee?.name) {
                        selectedCoffee = null
                    }
                    expandedCoffees = true
                },
                label = { Text("Select or search for a Coffee") },
                readOnly = false,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCoffees) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier.menuAnchor().width(280.dp)
            )

            ExposedDropdownMenu(
                expanded = expandedCoffees,
                onDismissRequest = { expandedCoffees = false }
            ) {
                filteredCoffees.forEach { coffee ->
                    DropdownMenuItem(
                        text = { Text(text = coffee.name) },
                        onClick = {
                            selectedCoffee = coffee
                            expandedCoffees = false
                        }
                    )
                }
                DropdownMenuItem(
                    text = { Text(text = "Add a new Coffee") },
                    onClick = {
                        expandedCoffees = false
                        onNewCoffeeClick()
                    }
                )
            }
        }
    }
}
