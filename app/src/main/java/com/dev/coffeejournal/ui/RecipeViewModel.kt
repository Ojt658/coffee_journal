package com.dev.coffeejournal.ui

import androidx.lifecycle.*
import com.dev.coffeejournal.data.Recipe
import com.dev.coffeejournal.data.RecipeDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel for managing recipe data.
 *
 * @param dao The Data Access Object for recipes.
 */
class RecipeViewModel (private val dao: RecipeDao): ViewModel() {
    /**
     * Holds the list of all recipes from the database as a StateFlow.
     * The UI will collect this flow to get updates automatically.
     */
    val allRecipes: StateFlow<List<Recipe>> = dao.getAllRecipes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * Inserts a new recipe into the database on a background thread.
     *
     * @param recipe The recipe to add.
     */
    fun addRecipe(recipe: Recipe) {
        viewModelScope.launch {
            dao.insert(recipe)
        }

    }
}