package com.dev.coffeejournal.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.dev.coffeejournal.ui.AppBar
import com.dev.coffeejournal.R
import com.dev.coffeejournal.data.Coffee
import com.dev.coffeejournal.data.CoffeeDatabase
import com.dev.coffeejournal.ui.CoffeeViewModel
import com.dev.coffeejournal.ui.CoffeeViewModelFactory
import com.dev.coffeejournal.ui.theme.CoffeeJournalTheme
import com.dev.coffeejournal.ui.CoffeeCardList
import com.dev.coffeejournal.ui.CoffeeCardSheet
import kotlin.getValue

/**
 * This activity displays a list of the user's favorite coffees.
 */
class FavouritesActivity : ComponentActivity() {

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
                var showCoffeeInfoSheet by remember { mutableStateOf(false) }
                val sheetState = rememberModalBottomSheetState()

                var selectedCoffee by remember { mutableStateOf<Coffee?>(null) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        AppBar(
                            "Favourites",
                            canNavigateBack = true,
                            navigateUp = { finish() })
                    }
                ) { innerPadding ->
                    FavouritesPage(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel,
                        onCoffeeSelected = {
                            coffee ->
                            selectedCoffee = coffee
                            showCoffeeInfoSheet = true
                        }
                    )

                    if (showCoffeeInfoSheet && selectedCoffee != null) {
                        ModalBottomSheet(
                            onDismissRequest = {
                                showCoffeeInfoSheet = false
                                selectedCoffee = null
                            },
                            sheetState = sheetState
                        ) {
                            CoffeeCardSheet(selectedCoffee!!)
                        }
                    }
                }
            }
        }
    }
}

/**
 * This composable displays the main content of the Favourites screen.
 *
 * @param viewModel The view model for accessing coffee data.
 * @param modifier The modifier to be applied to the composable.
 * @param onCoffeeSelected A callback that is invoked when a coffee is selected.
 */
@Composable
fun FavouritesPage(viewModel: CoffeeViewModel, modifier: Modifier = Modifier, onCoffeeSelected: (Coffee) -> Unit) {
    val coffees by viewModel.allFavourites.collectAsState()
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