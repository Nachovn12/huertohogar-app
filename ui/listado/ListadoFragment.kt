package ui.listado

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.huertohogar_app.R
import data.model.Producto

class ListadoFragment : Fragment() {
    private val viewModel: ListadoViewModel by viewModels()
    private lateinit var adapter: ProductoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_listado, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val btnGuardar = view.findViewById<Button>(R.id.btnGuardar)

        val productos = arguments?.getParcelableArray("productos")?.map { it as Producto } ?: emptyList()
        adapter = ProductoAdapter(productos) { producto ->
            val action = ListadoFragmentDirections.actionListadoFragmentToDetalleFragment(producto)
            findNavController().navigate(action)
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        viewModel.setProductos(productos)

        btnGuardar.setOnClickListener {
            viewModel.guardarEnDb()
        }

        viewModel.mensaje.observe(viewLifecycleOwner) { msg ->
            msg?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
