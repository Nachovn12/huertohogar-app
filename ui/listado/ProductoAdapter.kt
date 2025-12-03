package ui.listado

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import data.model.Producto
import com.example.huertohogar_app.R
import com.bumptech.glide.Glide

class ProductoAdapter(
    private val productos: List<Producto>,
    private val onItemClick: (Producto) -> Unit
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_producto, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        holder.bind(productos[position])
    }

    override fun getItemCount(): Int = productos.size

    inner class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val sku: TextView = itemView.findViewById(R.id.tvSku)
        private val nombre: TextView = itemView.findViewById(R.id.tvNombre)
        private val precio: TextView = itemView.findViewById(R.id.tvPrecio)
        private val imagen: ImageView? = itemView.findViewById(R.id.ivImagen)

        fun bind(producto: Producto) {
            sku.text = producto.sku
            nombre.text = producto.nombre
            precio.text = "${producto.precio}"
            imagen?.let {
                Glide.with(itemView.context)
                    .load(producto.imagen)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(it)
            }
            itemView.setOnClickListener { onItemClick(producto) }
        }
    }
}
