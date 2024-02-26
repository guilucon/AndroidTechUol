package br.com.guilherme.androidtechuol.ui.fragments

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.guilherme.androidtechuol.R
import br.com.guilherme.androidtechuol.data.api.ApiConfig
import br.com.guilherme.androidtechuol.data.models.Album
import br.com.guilherme.androidtechuol.data.models.User
import br.com.guilherme.androidtechuol.databinding.FragmentUserDetailsBinding
import br.com.guilherme.androidtechuol.databinding.FragmentUserListBinding
import br.com.guilherme.androidtechuol.ui.adapters.UserDetailsAdapter
import br.com.guilherme.androidtechuol.ui.adapters.UserListAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserDetailsFragment : Fragment() {
    private var _binding: FragmentUserDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private var userId = 0;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        fetchAlbumList()
    }

    private fun fetchAlbumList() {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiConfig().albumService.getAlbums(userId).execute()
                }
                if (response.isSuccessful) {
                    val users = response.body()
                    users?.let { updateRecyclerView(it) }
                }
            } catch (e: Exception) {
                Log.e("UserDetailsFragment", "Error fetching album list", e)
            }
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