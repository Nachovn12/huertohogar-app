package com.huertohogar.huertohogar_app.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class Address(val id: Int, val street: String, val city: String, val details: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressScreen(navController: NavController) {

    val addresses = remember {
        listOf(
            Address(1, "Avenida Siempreviva 742", "Springfield", "Casa rosada, al lado de la de Flanders"),
            Address(2, "Calle Falsa 123", "Concepción", "Departamento 302, torre B"),
            Address(3, "Pasaje El Maitén 456", "San Pedro de la Paz", "Casa con reja blanca")
        )
    }
    var selectedAddressId by remember { mutableStateOf(addresses.first().id) }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Dirección de Envío", fontWeight = FontWeight.Bold, color = Color(0xFF05161B)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        },
        bottomBar = {
            Button(
                onClick = { /* TODO: Navegar a la pantalla de pago */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF23AA49))
            ) {
                Text("Continuar", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            LazyColumn(
                modifier = Modifier.padding(paddingValues),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(addresses) {
                    AddressItem(address = it, isSelected = it.id == selectedAddressId, onSelect = { selectedAddressId = it.id })
                }
                item {
                    OutlinedButton(
                        onClick = { /* TODO: Lógica para añadir nueva dirección */ },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, Color(0xFFDDDDDD))
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Añadir", tint = Color(0xFF23AA49))
                        Text("Añadir nueva dirección", color = Color(0xFF23AA49), fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 12.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun AddressItem(address: Address, isSelected: Boolean, onSelect: () -> Unit) {
    val borderColor = if (isSelected) Color(0xFF23AA49) else Color(0xFFEEEEEE)
    val icon = if (isSelected) Icons.Filled.CheckCircle else Icons.Outlined.RadioButtonUnchecked
    val iconColor = if (isSelected) Color(0xFF23AA49) else Color(0xFFDDDDDD)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .background(Color.White)
            .clickable { onSelect() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(address.street, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF05161B))
            Text(address.city, fontSize = 14.sp, color = Color.Gray)
            Text(address.details, fontSize = 14.sp, color = Color.Gray)
        }
        Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(24.dp))
    }
}
