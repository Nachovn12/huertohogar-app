package ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import data.repository.ProductoRepository
import data.model.Producto
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ProductoRepository(application)

    private val _productosApi = MutableLiveData<List<Producto>>()
    val productosApi: LiveData<List<Producto>> = _productosApi

    private val _productosDb = MutableLiveData<List<Producto>>()
    val productosDb: LiveData<List<Producto>> = _productosDb

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun cargarDesdeApi() {
        viewModelScope.launch {
            val productos = repository.getProductosFromApi()
            if (productos.isNotEmpty()) {
                _productosApi.value = productos
            } else {
                _error.value = "Error al cargar desde API"
            }
        }
    }

    fun cargarDesdeDb() {
        viewModelScope.launch {
            val productos = repository.getProductosFromDb()
            if (productos.isNotEmpty()) {
                _productosDb.value = productos
            } else {
                _error.value = "No hay datos locales almacenados"
            }
        }
    }
}
