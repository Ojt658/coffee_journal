package com.dev.coffeejournal.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.dev.coffeejournal.ui.AppBar
import com.dev.coffeejournal.data.databases.Recipe
import com.dev.coffeejournal.data.models.RecipeViewModelFactory
import com.dev.coffeejournal.ui.theme.CoffeeJournalTheme
import com.dev.coffeejournal.data.databases.RecipeDatabase
import com.dev.coffeejournal.data.models.RecipeViewModel
import com.dev.coffeejournal.data.models.CoffeeViewModel
import com.dev.coffeejournal.data.models.CoffeeViewModelFactory
import com.dev.coffeejournal.data.databases.CoffeeDatabase


/**
 * This activity displays a list of coffee recipes.
 */
class RecipesActivity : ComponentActivity() {

    private val recipeViewModel: RecipeViewModel by viewModels {
        RecipeViewModelFactory(RecipeDatabase.getDatabase(applicationContext).recipeDao())
    }

    private val coffeeViewModel: CoffeeViewModel by viewModels {
        CoffeeViewModelFactory(CoffeeDatabase.getDatabase(applicationContext).coffeeDao())
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoffeeJournalTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar = {
                        AppBar(
                            "Recipes",
                            canNavigateBack = true,
                            navigateUp = { finish() })
                    }
                ) { innerPadding ->
                    RecipesPage(recipeViewModel = recipeViewModel, coffeeViewModel = coffeeViewModel, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

/**
 * This composable displays the main content of the Recipes screen.
 *
 * @param modifier The modifier to be applied to the composable.
 */
@Composable
fun RecipesPage(recipeViewModel: RecipeViewModel, coffeeViewModel: CoffeeViewModel, modifier: Modifier = Modifier) {
    val recipes by recipeViewModel.allRecipes.collectAsState()
    Box (modifier = modifier.fillMaxSize()
        .background(MaterialTheme.colorScheme.background)) {
//        Image (
//            painter = painterResource(id = R.drawable.coffee_beans_background),
//            contentDescription = "Background",
//            contentScale = ContentScale.Crop,
//            modifier = Modifier.fillMaxSize()
//        )
        when {
            recipes.isEmpty() ->
                Text (
                    text = "No recipes to display",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            else ->
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(recipes) { recipe ->
                        RecipeCard(recipe = recipe)
                    }
                }

        }
    }
}

@Composable
fun RecipeCard(recipe: Recipe) {
    Text(text = recipe.id.toString(), style = MaterialTheme.typography.headlineMedium)
}
