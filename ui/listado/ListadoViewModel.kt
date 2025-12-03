package ui.listado

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import data.repository.ProductoRepository
import data.model.Producto
import kotlinx.coroutines.launch

class ListadoViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ProductoRepository(application)

    private val _productos = MutableLiveData<List<Producto>>()
    val productos: LiveData<List<Producto>> = _productos

    private val _mensaje = MutableLiveData<String?>()
    val mensaje: LiveData<String?> = _mensaje

    fun setProductos(productos: List<Producto>) {
        _productos.value = productos
    }

    fun guardarEnDb() {
        viewModelScope.launch {
            _productos.value?.let {
                repository.saveProductosToDb(it)
                _mensaje.value = "Productos guardados en la base de datos local"
            }
        }
    }
}
