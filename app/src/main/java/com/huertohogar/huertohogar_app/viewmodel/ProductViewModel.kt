package com.huertohogar.huertohogar_app.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huertohogar.huertohogar_app.model.Product
import com.huertohogar.huertohogar_app.model.Pedido
import com.huertohogar.huertohogar_app.repository.ProductDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CartItem(val product: Product, val quantity: Int)

class ProductViewModel(
    private val dataSource: ProductDataSource
) : ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct.asStateFlow()

    // Carrito de compras
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    // Lista de pedidos
    private val _pedidos = MutableStateFlow<List<Pedido>>(emptyList())
    val pedidos: StateFlow<List<Pedido>> = _pedidos.asStateFlow()

    // Cantidad total de items en el carrito
    val totalCartItems = cartItems.map { items ->
        items.sumOf { it.quantity }
    }.stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.Lazily, 0)

    fun loadProducts() {
        viewModelScope.launch {
            _products.value = dataSource.getAllProducts()
            Log.d("ProductViewModel", "Loaded products count=${_products.value.size}")
        }
    }

    fun loadProductBySku(sku: String) {
        viewModelScope.launch {
            _selectedProduct.value = dataSource.getProductBySku(sku)
        }
    }

    fun addToCart(product: Product, quantity: Int = 1) {
        val currentCart = _cartItems.value.toMutableList()
        val existingItem = currentCart.find { it.product.sku == product.sku }

        if (existingItem != null) {
            val updatedItem = existingItem.copy(quantity = existingItem.quantity + quantity)
            val itemIndex = currentCart.indexOf(existingItem)
            currentCart[itemIndex] = updatedItem
        } else {
            currentCart.add(CartItem(product, quantity))
        }
        _cartItems.value = currentCart
    }

    fun updateQuantity(productSku: String, newQuantity: Int) {
        val currentCart = _cartItems.value.toMutableList()
        val itemIndex = currentCart.indexOfFirst { it.product.sku == productSku }

        if (itemIndex != -1) {
            if (newQuantity > 0) {
                currentCart[itemIndex] = currentCart[itemIndex].copy(quantity = newQuantity)
            } else {
                currentCart.removeAt(itemIndex)
            }
            _cartItems.value = currentCart
        }
    }

    fun removeFromCart(productSku: String) {
        val currentCart = _cartItems.value.toMutableList()
        currentCart.removeAll { it.product.sku == productSku }
        _cartItems.value = currentCart
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    fun crearPedido(pedido: Pedido) {
        val currentPedidos = _pedidos.value.toMutableList()
        currentPedidos.add(0, pedido) // Agregar al inicio para que aparezca primero
        _pedidos.value = currentPedidos
        Log.d("ProductViewModel", "Pedido creado: ${pedido.numeroPedido}, Total pedidos: ${currentPedidos.size}")
    }
}
