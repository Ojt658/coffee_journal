package com.dev.coffeejournal.activities

import  android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dev.coffeejournal.ui.AppBar
import com.dev.coffeejournal.data.models.CoffeeViewModel
import com.dev.coffeejournal.data.models.CoffeeViewModelFactory
import com.dev.coffeejournal.R
import com.dev.coffeejournal.data.databases.Coffee
import com.dev.coffeejournal.data.databases.CoffeeDatabase
import com.dev.coffeejournal.ui.theme.CoffeeJournalTheme
import com.dev.coffeejournal.ui.CoffeeCardList
import com.dev.coffeejournal.ui.CoffeeCardSheet
import com.dev.coffeejournal.ui.NewCoffeeForm


/**
 * This activity displays a list of coffees and allows the user to add new coffees.
 */
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

/**
 * This composable displays the main content of the Coffee Info screen.
 *
 * @param viewModel The view model for accessing coffee data.
 * @param modifier The modifier to be applied to the composable.
 * @param onCoffeeSelected A callback that is invoked when a coffee is selected.
 */
@Composable
fun CoffeeInfoPage(viewModel: CoffeeViewModel, modifier: Modifier = Modifier, onCoffeeSelected: (Coffee) -> Unit) {
    val coffees by viewModel.allCoffees.collectAsState()
    Box (modifier = modifier.fillMaxSize()
        .background(MaterialTheme.colorScheme.background)) {
//        Image (
//            painter = painterResource(id = R.drawable.coffee_beans_background),
//            contentDescription = "Background",
//            contentScale = ContentScale.Crop,
//            modifier = Modifier.fillMaxSize()
//        )
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