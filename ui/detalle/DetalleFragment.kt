package ui.detalle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.huertohogar_app.R
import data.model.Producto

class DetalleFragment : Fragment() {
    private val viewModel: DetalleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detalle, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val producto = arguments?.getParcelable<Producto>("producto")
        viewModel.producto = producto
        producto?.let {
            view.findViewById<TextView>(R.id.tvSku).text = it.sku
            view.findViewById<TextView>(R.id.tvNombre).text = it.nombre
            view.findViewById<TextView>(R.id.tvPrecio).text = "${it.precio}"
            view.findViewById<TextView>(R.id.tvDescripcion).text = it.descripcion ?: "Sin descripción"
            // Si tienes una librería de imágenes, podrías cargar it.imagen aquí
        }
    }
}
