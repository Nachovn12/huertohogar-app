package ui.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DetalleViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetalleViewModel::class.java)) {
            return DetalleViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
