package br.com.guilherme.androidtechuol.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import br.com.guilherme.androidtechuol.data.api.ApiConfig
import br.com.guilherme.androidtechuol.data.models.Photo
import br.com.guilherme.androidtechuol.databinding.FragmentAlbumPhotosBinding
import br.com.guilherme.androidtechuol.ui.adapters.UserAlbumPhotosAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AlbumPhotosFragment : Fragment() {
    private var _binding: FragmentAlbumPhotosBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private var albumId = 0;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlbumPhotosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        arguments?.let { args ->
            val name = args.getString("album")
            albumId = args.getInt("id")
            binding.toolbar.title = name
        }
        val toolbar = binding.toolbar
        toolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }
        fetchPhotoList()
    }

    private fun fetchPhotoList() {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiConfig().photoService.getPhotos(albumId).execute()
                }
                if (response.isSuccessful) {
                    val photos = response.body()
                    Log.i("photo", "Photos: $photos")
                    photos?.let { updateRecyclerView(it) }
                }
            } catch (e: Exception) {
                Log.e("UserDetailsFragment", "Error fetching album list", e)
            }
        }
    }

    private fun updateRecyclerView(photoList: List<Photo>) {
        val adapter = UserAlbumPhotosAdapter(requireContext(), photoList)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}