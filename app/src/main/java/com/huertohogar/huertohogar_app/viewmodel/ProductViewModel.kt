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

// Clase auxiliar para representar items del carrito con su cantidad
data class CartItem(val product: Product, val quantity: Int)

/**
 * ViewModel principal que maneja el estado de productos, carrito y pedidos
 * Aprendí a usar StateFlow para gestionar el estado reactivo en la app
 */
class ProductViewModel(
    private val dataSource: ProductDataSource
) : ViewModel() {
    
    // Lista de productos disponibles
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    // Producto seleccionado para la pantalla de detalle
    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct.asStateFlow()

    // Items del carrito de compras
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    // Historial de pedidos realizados
    private val _pedidos = MutableStateFlow<List<Pedido>>(emptyList())
    val pedidos: StateFlow<List<Pedido>> = _pedidos.asStateFlow()

    // Contador total de items (suma de cantidades)
    val totalCartItems = cartItems.map { items ->
        items.sumOf { it.quantity }
    }.stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.Lazily, 0)

    // Carga inicial de productos desde el repositorio
    fun loadProducts() {
        viewModelScope.launch {
            _products.value = dataSource.getAllProducts()
            Log.d("ProductViewModel", "Productos cargados: ${_products.value.size}")
        }
    }

    // Busca y carga un producto específico por SKU
    fun loadProductBySku(sku: String) {
        viewModelScope.launch {
            _selectedProduct.value = dataSource.getProductBySku(sku)
        }
    }

    // Agrega productos al carrito o incrementa cantidad si ya existe
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

    // Actualiza la cantidad de un producto en el carrito
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

    // Elimina un producto específico del carrito
    fun removeFromCart(productSku: String) {
        val currentCart = _cartItems.value.toMutableList()
        currentCart.removeAll { it.product.sku == productSku }
        _cartItems.value = currentCart
    }

    // Vacía todo el carrito (usado después de crear pedido)
    fun clearCart() {
        _cartItems.value = emptyList()
    }

    // Registra un nuevo pedido en el historial
    fun crearPedido(pedido: Pedido) {
        val currentPedidos = _pedidos.value.toMutableList()
        currentPedidos.add(0, pedido)
        _pedidos.value = currentPedidos
        Log.d("ProductViewModel", "Pedido #${pedido.numeroPedido} registrado")
    }
}
