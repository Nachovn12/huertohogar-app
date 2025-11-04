package com.huertohogar.huertohogar_app.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Calendar

data class UserProfileData(
    val nombre: String = "Juanito Pérez González",
    val email: String = "juanito.perez@email.com",
    val telefono: String = "+56 9 8765 4321",
    val direccion: String = "Av. O'Higgins 1234, Depto. 302",
    val ciudad: String = "Concepción",
    val comuna: String = "Concepción",
    val region: String = "Región del Biobío",
    val avatarUrl: String? = null
)

class UserViewModel : ViewModel() {
    private val _userProfile = MutableStateFlow(UserProfileData())
    val userProfile: StateFlow<UserProfileData> = _userProfile.asStateFlow()

    fun updateNombre(nombre: String) {
        _userProfile.value = _userProfile.value.copy(nombre = nombre)
    }

    fun updateEmail(email: String) {
        _userProfile.value = _userProfile.value.copy(email = email)
    }

    fun updateTelefono(telefono: String) {
        _userProfile.value = _userProfile.value.copy(telefono = telefono)
    }

    fun updateDireccion(direccion: String) {
        _userProfile.value = _userProfile.value.copy(direccion = direccion)
    }

    fun updateCiudad(ciudad: String) {
        _userProfile.value = _userProfile.value.copy(ciudad = ciudad)
    }

    fun updateComuna(comuna: String) {
        _userProfile.value = _userProfile.value.copy(comuna = comuna)
    }

    fun updateRegion(region: String) {
        _userProfile.value = _userProfile.value.copy(region = region)
    }

    fun getGreeting(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 0..5 -> "Buenas noches"
            in 6..11 -> "Buenos días"
            in 12..18 -> "Buenas tardes"
            else -> "Buenas noches"
        }
    }

    fun getGreetingEmoji(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 0..5 -> "🌙"
            in 6..11 -> "☀️"
            in 12..18 -> "🌤️"
            else -> "🌙"
        }
    }

    fun getPrimerNombre(): String {
        return _userProfile.value.nombre.split(" ").firstOrNull() ?: "Usuario"
    }
}

