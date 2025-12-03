package ui.home

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.huertohogar_app.R

class HomeFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnApi = view.findViewById<Button>(R.id.btnApi)
        val btnDb = view.findViewById<Button>(R.id.btnDb)

        // Cargar automáticamente los productos de la BD Local al iniciar
        viewModel.cargarDesdeDb()

        btnApi.setOnClickListener {
            if (isInternetAvailable(requireContext())) {
                viewModel.cargarDesdeApi()
            } else {
                Toast.makeText(requireContext(), "Sin conexión a Internet", Toast.LENGTH_SHORT).show()
            }
        }

        btnDb.setOnClickListener {
            viewModel.cargarDesdeDb()
        }

        viewModel.productosApi.observe(viewLifecycleOwner) { productos ->
            if (productos != null) {
                val action = HomeFragmentDirections.actionHomeFragmentToListadoFragment(productos.toTypedArray())
                findNavController().navigate(action)
            }
        }
        viewModel.productosDb.observe(viewLifecycleOwner) { productos ->
            if (productos != null) {
                val action = HomeFragmentDirections.actionHomeFragmentToListadoFragment(productos.toTypedArray())
                findNavController().navigate(action)
            }
        }
        viewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            errorMsg?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
