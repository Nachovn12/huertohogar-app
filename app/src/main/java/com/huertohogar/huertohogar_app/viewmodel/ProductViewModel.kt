package com.huertohogar.huertohogar_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huertohogar.huertohogar_app.model.Product
import com.huertohogar.huertohogar_app.repository.ProductRepository
import com.huertohogar.huertohogar_app.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CartItem(val product: Product, val quantity: Int)

class ProductViewModel(
    private val repository: ProductRepository = ProductRepository(
        api = RetrofitInstance.api
    )
) : ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct.asStateFlow()

    // Carrito de compras
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    // Cantidad total de items en el carrito
    val totalCartItems = cartItems.map { items ->
        items.sumOf { it.quantity }
    }.stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.Lazily, 0)

    fun loadProducts() {
        viewModelScope.launch {
            _products.value = repository.getAllProducts()
        }
    }

    fun loadProductById(id: Long) {
        viewModelScope.launch {
            _selectedProduct.value = repository.getProductById(id)
        }
    }

    fun addToCart(product: Product, quantity: Int = 1) {
        val currentCart = _cartItems.value.toMutableList()
        val existingItem = currentCart.find { it.product.id == product.id }

        if (existingItem != null) {
            val updatedItem = existingItem.copy(quantity = existingItem.quantity + quantity)
            val itemIndex = currentCart.indexOf(existingItem)
            currentCart[itemIndex] = updatedItem
        } else {
            currentCart.add(CartItem(product, quantity))
        }
        _cartItems.value = currentCart
    }

    fun updateQuantity(productId: Long, newQuantity: Int) {
        val currentCart = _cartItems.value.toMutableList()
        val itemIndex = currentCart.indexOfFirst { it.product.id == productId }

        if (itemIndex != -1) {
            if (newQuantity > 0) {
                currentCart[itemIndex] = currentCart[itemIndex].copy(quantity = newQuantity)
            } else {
                currentCart.removeAt(itemIndex)
            }
            _cartItems.value = currentCart
        }
    }

    fun removeFromCart(productId: Long) {
        _cartItems.value = _cartItems.value.filter { it.product.id != productId }
    }
}
