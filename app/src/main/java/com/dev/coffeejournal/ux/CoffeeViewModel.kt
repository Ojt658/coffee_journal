package com.dev.coffeejournal.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.coffeejournal.data.Coffee
import com.dev.coffeejournal.data.CoffeeDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CoffeeViewModel(private val dao: CoffeeDao) : ViewModel() {

    /**
     * Holds the list of all coffees from the database as a StateFlow.
     * The UI will collect this flow to get updates automatically.
     */
    val allCoffees: StateFlow<List<Coffee>> = dao.getAllCoffees()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
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
}
    