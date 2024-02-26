package br.com.guilherme.androidtechuol.ui.view

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.guilherme.androidtechuol.R
import br.com.guilherme.androidtechuol.data.models.Album
import br.com.guilherme.androidtechuol.databinding.FragmentUserDetailsBinding
import br.com.guilherme.androidtechuol.ui.adapters.UserDetailsAdapter
import br.com.guilherme.androidtechuol.ui.viewmodel.UserDetailsViewModel

class UserDetailsFragment : Fragment() {
    private var _binding: FragmentUserDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var viewModel: UserDetailsViewModel
    private var userId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserDetailsViewModel::class.java)
        navController = findNavController()

        arguments?.let { args ->
            val name = args.getString("name")
            val email = args.getString("email")
            val username = args.getString("username")
            userId = args.getInt("id")

            binding.userDetailsNome.text = name
            binding.userDetailsEmail.text = email
            binding.userDetailsUsername.text = username
        }

        val toolbar = binding.toolbar
        toolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }

        userId?.let { viewModel.fetchAlbumList(it) }
        viewModel.albumList.observe(viewLifecycleOwner) { albumList ->
            albumList?.let { updateRecyclerView(it) }
        }
    }

    private fun updateRecyclerView(albumList: List<Album>) {
        val adapter = UserDetailsAdapter(requireContext(), albumList) { album ->
            val bundle = Bundle().apply {
                putString("album", album.title)
                putInt("id", album.userId)
            }
            navController.navigate(R.id.action_detailsUserFragment_to_albumPhotosFragment, bundle)
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.bottom = 32
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}