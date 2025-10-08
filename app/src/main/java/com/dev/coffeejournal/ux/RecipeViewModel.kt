package com.dev.coffeejournal.ux

import androidx.lifecycle.*
import com.dev.coffeejournal.data.Recipe
import com.dev.coffeejournal.data.RecipeDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class RecipeViewModel (private val dao: RecipeDao): ViewModel() {
    val allRecipes: StateFlow<List<Recipe>> = dao.getAllRecipes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addRecipe(recipe: Recipe) {
        viewModelScope.launch {
            dao.insert(recipe)
        }

    }
}