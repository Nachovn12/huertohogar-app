package ui.listado

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ListadoViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListadoViewModel::class.java)) {
            return ListadoViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
