package com.dev.coffeejournal.data.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dev.coffeejournal.data.databases.CoffeeDao
import com.dev.coffeejournal.data.databases.RecipeDao

/**
 * Factory for creating [CoffeeViewModel] instances.
 * @param dao The [CoffeeDao] instance.
 */
class CoffeeViewModelFactory(private val dao: CoffeeDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CoffeeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CoffeeViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

/**
 * Factory for creating [RecipeViewModel] instances.
 * @param dao The [RecipeDao] instance.
 */
class RecipeViewModelFactory(private val dao: RecipeDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipeViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}