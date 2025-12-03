package com.huertohogar.huertohogar_app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.huertohogar.huertohogar_app.R
import com.huertohogar.huertohogar_app.model.Product
import com.huertohogar.huertohogar_app.utils.formatPrecio

@Composable
fun ProductItem(
    product: Product,
    onClick: () -> Unit = {},
    onAdd: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen con placeholder
            val imageModel = product.imageUrl
            AsyncImage(
                model = imageModel,
                placeholder = painterResource(id = R.drawable.logo_huerto_hogar),
                error = painterResource(id = R.drawable.logo_huerto_hogar),
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(84.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = product.name ?: "Producto", style = MaterialTheme.typography.titleMedium, color = Color(0xFF05161B))
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = product.sku, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "$ ${product.price.formatPrecio()}", style = MaterialTheme.typography.titleSmall, color = Color(0xFFFF314A))
            }

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = onAdd,
                modifier = Modifier
                    .size(44.dp)
                    .background(Color(0xFF23AA49), shape = RoundedCornerShape(22.dp))
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar", tint = Color.White)
            }
        }
    }
}
