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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
                var showSheet by remember { mutableStateOf(false) }
                val sheetState = rememberModalBottomSheetState()

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
                            onClick = { showSheet = true },
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
                    CoffeeInfoPage(modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel)
                }

                if (showSheet) {
                    ModalBottomSheet(
                        onDismissRequest = { showSheet = false },
                        sheetState = sheetState
                    ) {
                        NewCoffeeForm(onAddCoffee = { newCoffee ->
                            viewModel.addCoffee(newCoffee)
                            showSheet = false
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun CoffeeInfoPage(viewModel: CoffeeViewModel, modifier: Modifier = Modifier) {
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
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.secondary
            )
            else -> LazyColumn (modifier = Modifier.fillMaxSize()) {
                items(coffees) { coffee ->
                    CoffeeCard(coffee = coffee)
                }
            }
        }

    }
}

@Composable
fun CoffeeCard(coffee: Coffee, modifier: Modifier = Modifier) {
    Text(text = "${ coffee.name } from ${ coffee.origin }",
        modifier = modifier.padding(16.dp).fillMaxWidth())
}


@Composable
fun NewCoffeeForm(onAddCoffee: (Coffee) -> Unit, modifier: Modifier = Modifier) {
    var name by remember { mutableStateOf("") }
    var origin by remember { mutableStateOf("") }
    var process by remember { mutableStateOf("") }
    var roastProfile by remember { mutableStateOf("") }
    var flavourProfile by remember { mutableStateOf("") }

    Box(modifier = modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.primaryContainer)) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)) {
            Text(text = "Add a new Coffee", style = MaterialTheme.typography.headlineSmall)

            TextField(value = name, onValueChange = {name = it}, label = { Text("Name") })
            TextField(value = origin, onValueChange = {origin = it}, label = { Text("Origin") })
            TextField(value = process, onValueChange = {process = it}, label = { Text("Process") })
            TextField(value = roastProfile, onValueChange = {roastProfile = it}, label = { Text("Roast Profile") })
            TextField(value = flavourProfile, onValueChange = {flavourProfile = it}, label = { Text("Flavour Profile") })

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
        }
    }
}