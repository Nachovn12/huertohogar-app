package com.huertohogar.huertohogar_app.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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
    private val localDataSource: ProductDataSource,
    private val remoteDataSource: ProductDataSource? = null
) : ViewModel() {

    private enum class LastSource { NONE, LOCAL, REMOTE }
    private var lastSource: LastSource = LastSource.NONE

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



    // Busca y carga un producto específico por SKU
    fun loadProductBySku(sku: String) {
        viewModelScope.launch {
            try {
                _selectedProduct.value = localDataSource.getProductBySku(sku)
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Error al cargar producto por sku", e)
                _selectedProduct.value = null
            }
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

    // Verifica la disponibilidad de conexión a Internet
    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    // Cargar productos desde la API pública (remoteDataSource)
    fun loadProductsFromApi(onComplete: (() -> Unit)? = null) {
        remoteDataSource?.let { remote ->
            viewModelScope.launch {
                try {
                    val remoteList = remote.getAllProducts()
                    Log.d("ProductViewModel", "📡 Productos recibidos de API: ${remoteList.size}")
                    
                    // Eliminar duplicados por SKU
                    val uniqueProducts = remoteList.distinctBy { it.sku }
                    Log.d("ProductViewModel", "✅ Productos únicos (sin duplicados): ${uniqueProducts.size}")
                    
                    // Verificar si hay duplicados
                    if (remoteList.size != uniqueProducts.size) {
                        Log.w("ProductViewModel", "⚠️ Se encontraron ${remoteList.size - uniqueProducts.size} productos duplicados")
                    }
                    
                    _products.value = uniqueProducts
                    lastSource = LastSource.REMOTE
                    onComplete?.invoke()
                } catch (e: Exception) {
                    Log.e("ProductViewModel", "❌ Error al cargar productos desde API", e)
                    _products.value = emptyList()
                    onComplete?.invoke()
                }
            }
        } ?: run {
            Log.w("ProductViewModel", "⚠️ RemoteDataSource no está configurado")
            onComplete?.invoke()
        }
    }

    // Cargar productos desde la base de datos local (Room)
    fun loadProductsFromDb(onComplete: (() -> Unit)? = null, onEmpty: (() -> Unit)? = null) {
        viewModelScope.launch {
            try {
                roomRepository?.let { repo ->
                    val productos = repo.getAllProducts()
                    Log.d("ProductViewModel", "💾 Productos recibidos de BD Local: ${productos.size}")
                    
                    // Eliminar duplicados por SKU
                    val uniqueProducts = productos.distinctBy { it.sku }
                    Log.d("ProductViewModel", "✅ Productos únicos (sin duplicados): ${uniqueProducts.size}")
                    
                    // Verificar si hay duplicados
                    if (productos.size != uniqueProducts.size) {
                        Log.w("ProductViewModel", "⚠️ Se encontraron ${productos.size - uniqueProducts.size} productos duplicados en BD")
                    }
                    
                    if (uniqueProducts.isNotEmpty()) {
                        _products.value = uniqueProducts
                        lastSource = LastSource.LOCAL
                        Log.d("ProductViewModel", "✅ Productos cargados desde Room BD: ${uniqueProducts.size}")
                        onComplete?.invoke()
                    } else {
                        Log.d("ProductViewModel", "📭 Base de datos local vacía")
                        _products.value = emptyList()
                        onEmpty?.invoke()
                    }
                } ?: run {
                    Log.e("ProductViewModel", "❌ RoomRepository no configurado para cargar desde BD")
                    onEmpty?.invoke()
                }
            } catch (e: Exception) {
                Log.e("ProductViewModel", "❌ Error cargando productos desde DB", e)
                _products.value = emptyList()
                onEmpty?.invoke()
            }
        }
    }



    // Variable para el repositorio Room (se inyecta desde afuera)
    private var roomRepository: com.huertohogar.huertohogar_app.data.repository.RoomProductRepository? = null

    // Setter para inyectar el repositorio Room
    fun setRoomRepository(repo: com.huertohogar.huertohogar_app.data.repository.RoomProductRepository) {
        this.roomRepository = repo
    }

    // Guardar los productos actualmente visibles en la base de datos local
    fun saveProductsToDb(onSuccess: (() -> Unit)? = null, onError: ((String) -> Unit)? = null) {
        viewModelScope.launch {
            try {
                val currentProducts = _products.value
                if (currentProducts.isEmpty()) {
                    Log.w("ProductViewModel", "⚠️ No hay productos para guardar")
                    onError?.invoke("No hay productos para guardar")
                    return@launch
                }

                // Eliminar duplicados antes de guardar
                val uniqueProducts = currentProducts.distinctBy { it.sku }
                Log.d("ProductViewModel", "💾 Preparando para guardar ${uniqueProducts.size} productos únicos")
                
                if (currentProducts.size != uniqueProducts.size) {
                    Log.w("ProductViewModel", "⚠️ Se eliminaron ${currentProducts.size - uniqueProducts.size} duplicados antes de guardar")
                }

                roomRepository?.let { repo ->
                    // ✅ LIMPIAR BD ANTES DE GUARDAR (EVITA DUPLICADOS Y DATOS ANTIGUOS)
                    repo.clearAllProducts()
                    Log.d("ProductViewModel", "🗑️ Base de datos limpiada antes de guardar")
                    
                    // Guardar productos nuevos
                    val success = repo.saveProducts(uniqueProducts)
                    if (success) {
                        Log.d("ProductViewModel", "✅ ${uniqueProducts.size} productos guardados en BD Local")
                        onSuccess?.invoke()
                    } else {
                        Log.e("ProductViewModel", "❌ Error al guardar en la base de datos")
                        onError?.invoke("Error al guardar en la base de datos")
                    }
                } ?: run {
                    Log.e("ProductViewModel", "❌ RoomRepository no está configurado")
                    onError?.invoke("Repositorio no configurado")
                }
            } catch (e: Exception) {
                Log.e("ProductViewModel", "❌ Error al guardar productos en BD", e)
                onError?.invoke(e.message ?: "Error desconocido")
            }
        }
    }

    // Cargar productos desde assets/products.json
    fun loadProductsFromAssets(context: Context, onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            try {
                val localRepo = com.huertohogar.huertohogar_app.repository.LocalProductRepository(context)
                val localProducts = localRepo.getAllProducts()
                if (localProducts.isNotEmpty()) {
                    _products.value = localProducts
                    lastSource = LastSource.LOCAL
                    Log.d("ProductViewModel", "Productos cargados desde assets: ${localProducts.size}")
                    onComplete?.invoke()
                } else {
                    Log.d("ProductViewModel", "No hay productos en assets/products.json")
                    _products.value = emptyList()
                    onComplete?.invoke()
                }
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Error cargando productos desde assets", e)
                _products.value = emptyList()
                onComplete?.invoke()
            }
        }
    }

    // Verifica si la base de datos local está vacía (Room)
    fun isLocalDbEmpty(): Boolean {
        return _products.value.isEmpty()
    }
}
