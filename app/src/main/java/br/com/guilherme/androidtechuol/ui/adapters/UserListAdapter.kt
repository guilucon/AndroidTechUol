package br.com.guilherme.androidtechuol.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import br.com.guilherme.androidtechuol.data.models.User
import br.com.guilherme.androidtechuol.databinding.ItemUserBinding

class UserListAdapter(
    private val context: Context,
    private val users: List<User>,
    var onItemClickListener: (user: User) -> Unit = {}
) : RecyclerView.Adapter<UserListAdapter.ViewHolder>(), SearchView.OnQueryTextListener {
    private var filteredUserList = users.toList()

    inner class ViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.userListNameOfUser.text = user.name
            binding.userListUsername.text = user.username
            binding.root.setOnClickListener {
                onItemClickListener(user)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = ItemUserBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return filteredUserList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = filteredUserList[position]
        holder.bind(user)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onQueryTextChange(newText: String?): Boolean {
        filteredUserList = if (newText.isNullOrEmpty()) {
            users.toList()
        } else {
            users.filter { user -> user.name.contains(newText, ignoreCase = true) }
        }
        notifyDataSetChanged()
        return true
    }
}