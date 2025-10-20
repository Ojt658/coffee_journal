package com.dev.coffeejournal.data.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.coffeejournal.data.databases.Coffee
import com.dev.coffeejournal.data.databases.CoffeeDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel for managing coffee data.
 *
 * @param dao The Data Access Object for coffees.
 */
class CoffeeViewModel(private val dao: CoffeeDao) : ViewModel() {

    /**
     * Holds the list of all coffees from the database as a StateFlow.
     * The UI will collect this flow to get updates automatically.
     */
    val allCoffees: StateFlow<List<Coffee>> = dao.getAllCoffees()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * Holds the list of all favourite coffees from the database as a StateFlow.
     * The UI will collect this flow to get updates automatically.
     */
    val allFavourites: StateFlow<List<Coffee>> = dao.getAllFavourites()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * Inserts a new coffee into the database on a background thread.
     */
    fun addCoffee(coffee: Coffee) {
        viewModelScope.launch {
            dao.insert(coffee)
        }
    }

    /**
     * Updates a coffee in the database on a background thread.
     */
    fun updateCoffee(coffee: Coffee) {
        viewModelScope.launch {
            dao.update(coffee)
        }
    }

    fun getCoffeeOrigin(coffeeId: Int): String {
        val coffee = allCoffees.value.find { it.id == coffeeId }
        return coffee?.origin ?: ""
    }
}
    