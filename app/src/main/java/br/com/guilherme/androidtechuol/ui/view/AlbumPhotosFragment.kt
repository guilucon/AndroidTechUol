package br.com.guilherme.androidtechuol.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import br.com.guilherme.androidtechuol.data.models.Photo
import br.com.guilherme.androidtechuol.databinding.FragmentAlbumPhotosBinding
import br.com.guilherme.androidtechuol.ui.adapters.UserAlbumPhotosAdapter
import br.com.guilherme.androidtechuol.ui.viewmodel.AlbumPhotosViewModel


class AlbumPhotosFragment : Fragment() {
    private var _binding: FragmentAlbumPhotosBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var viewModel: AlbumPhotosViewModel
    private var albumId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlbumPhotosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(AlbumPhotosViewModel::class.java)
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

        albumId?.let { viewModel.fetchPhotoList(it) }
        viewModel.photoList.observe(viewLifecycleOwner) { photoList ->
            photoList?.let { updateRecyclerView(it) }
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