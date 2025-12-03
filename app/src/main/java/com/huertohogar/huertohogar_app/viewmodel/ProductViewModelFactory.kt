package com.huertohogar.huertohogar_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.huertohogar.huertohogar_app.repository.ProductDataSource

class ProductViewModelFactory(
    private val localDataSource: ProductDataSource,
    private val remoteDataSource: ProductDataSource? = null
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            return ProductViewModel(localDataSource, remoteDataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

