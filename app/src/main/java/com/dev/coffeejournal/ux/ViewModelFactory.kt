package com.dev.coffeejournal.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dev.coffeejournal.data.CoffeeDao

class CoffeeViewModelFactory(private val dao: CoffeeDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CoffeeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CoffeeViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}