package com.huertohogar.huertohogar_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.huertohogar.huertohogar_app.repository.ProductDataSource

class ProductViewModelFactory(private val dataSource: ProductDataSource) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass)
    }
}

