package br.com.guilherme.androidtechuol.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.guilherme.androidtechuol.R
import br.com.guilherme.androidtechuol.data.api.ApiConfig
import br.com.guilherme.androidtechuol.data.models.User
import br.com.guilherme.androidtechuol.databinding.FragmentUserListBinding
import br.com.guilherme.androidtechuol.ui.adapters.UserListAdapter
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserListFragment : Fragment() {
    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        fetchUserList()
        setupSearchView()
    }

    private fun setupSearchView() {
        binding.searchView.clearFocus()
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    (binding.recyclerView.adapter as UserListAdapter).onQueryTextChange(newText)
                }
                return true
            }
        })
    }

    private fun fetchUserList() {
        lifecycleScope.launch {
            try {
                val response = withContext(IO) {
                    ApiConfig().userService.getAll().execute()
                }
                if (response.isSuccessful) {
                    val users = response.body()
                    users?.let { updateRecyclerView(it) }
                }
            } catch (e: Exception) {
                Log.e("UserListFragment", "Error fetching user list", e)
            }
        }
    }

    private fun updateRecyclerView(userList: List<User>) {
        val adapter = UserListAdapter(requireContext(), userList) { user ->
            val bundle = Bundle().apply {
                putString("name", user.name)
                putString("username", user.username)
                putString("email", user.email)
                putInt("id", user.id)
            }
            navController.navigate(R.id.action_userListFragment_to_detailsUserFragment, bundle)
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val divisor = DividerItemDecoration(context, LinearLayout.VERTICAL)
        binding.recyclerView.addItemDecoration(divisor)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}