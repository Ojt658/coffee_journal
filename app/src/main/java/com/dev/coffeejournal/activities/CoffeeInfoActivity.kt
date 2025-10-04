package com.dev.coffeejournal.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dev.coffeejournal.ux.AppBar
import com.dev.coffeejournal.ui.CoffeeViewModel
import com.dev.coffeejournal.ui.CoffeeViewModelFactory
import com.dev.coffeejournal.R
import com.dev.coffeejournal.data.Coffee
import com.dev.coffeejournal.data.CoffeeDatabase
import com.dev.coffeejournal.ui.theme.CoffeeJournalTheme
import com.dev.coffeejournal.ux.CoffeeCardList
import com.dev.coffeejournal.ux.CoffeeCardSheet


class CoffeeInfoActivity : ComponentActivity() {
    private val viewModel: CoffeeViewModel by viewModels {
        CoffeeViewModelFactory(
            CoffeeDatabase.getDatabase(applicationContext).coffeeDao()
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoffeeJournalTheme {
                var showAddCoffeeSheet by remember { mutableStateOf(false) }
                var showCoffeeInfoSheet by remember { mutableStateOf(false) }
                val addSheetState = rememberModalBottomSheetState()
                val infoSheetState = rememberModalBottomSheetState()

                var selectedCoffee by remember { mutableStateOf<Coffee?>(null) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        AppBar(
                            "Coffees",
                            canNavigateBack = true,
                            navigateUp = { finish() })
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { showAddCoffeeSheet = true },
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
                    CoffeeInfoPage(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel,
                        onCoffeeSelected = { coffee ->
                            selectedCoffee = coffee
                            showCoffeeInfoSheet = true
                        }
                    )
                }

                if (showAddCoffeeSheet) {
                    ModalBottomSheet(
                        onDismissRequest = { showAddCoffeeSheet = false },
                        sheetState = addSheetState
                    ) {
                        NewCoffeeForm(onAddCoffee = { newCoffee ->
                            viewModel.addCoffee(newCoffee)
                            showAddCoffeeSheet = false
                        },
                        modifier = Modifier.imePadding())
                    }
                }
                if (showCoffeeInfoSheet && selectedCoffee != null) {
                    ModalBottomSheet(
                        onDismissRequest = {
                            showCoffeeInfoSheet = false
                            selectedCoffee = null
                       },
                        sheetState = infoSheetState
                    ) {
                        CoffeeCardSheet(selectedCoffee!!)
                    }
                }
            }
        }
    }
}

@Composable
fun CoffeeInfoPage(viewModel: CoffeeViewModel, modifier: Modifier = Modifier, onCoffeeSelected: (Coffee) -> Unit) {
    val coffees by viewModel.allCoffees.collectAsState()
    Box (modifier = modifier.fillMaxSize()) {
        Image (
            painter = painterResource(id = R.drawable.coffee_beans_background),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        when {
            coffees.isEmpty() -> Text(
                text = "No coffees to display",
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            else -> LazyColumn (modifier = Modifier.fillMaxSize()) {
                items(coffees) { coffee ->
                    CoffeeCardList(coffee = coffee, onCoffeeSelected = onCoffeeSelected, viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun NewCoffeeForm(onAddCoffee: (Coffee) -> Unit, modifier: Modifier = Modifier) {
    var name by remember { mutableStateOf("") }
    var origin by remember { mutableStateOf("") }
    var process by remember { mutableStateOf("") }
    var roastProfile by remember { mutableStateOf("") }
    var flavourProfile by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    val textFieldColors = TextFieldDefaults.colors(
        focusedLabelColor = MaterialTheme.colorScheme.background,
        unfocusedLabelColor = MaterialTheme.colorScheme.background,
        focusedTextColor = MaterialTheme.colorScheme.background,
        unfocusedTextColor = MaterialTheme.colorScheme.background,
        focusedContainerColor = MaterialTheme.colorScheme.tertiary,
        unfocusedContainerColor = MaterialTheme.colorScheme.tertiary,
        cursorColor = MaterialTheme.colorScheme.primary
    )

    Box(modifier = modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.primaryContainer)) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(scrollState)
                .fillMaxWidth()
        ) {
            Text(text = "Add a new Coffee", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)

            TextField(value = name, onValueChange = {name = it}, label = { Text("Name") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
            TextField(value = origin, onValueChange = {origin = it}, label = { Text("Origin") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
            TextField(value = process, onValueChange = {process = it}, label = { Text("Process") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
            TextField(value = roastProfile, onValueChange = {roastProfile = it}, label = { Text("Roast Profile") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
            TextField(value = flavourProfile, onValueChange = {flavourProfile = it}, label = { Text("Flavour Profile") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)

            Button(
                onClick = {
                    val newCoffee = Coffee(
                        name = name,
                        origin = origin,
                        process = process,
                        roastProfile = roastProfile,
                        flavourProfile = flavourProfile
                    )
                    onAddCoffee(newCoffee)
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Save Coffee")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}