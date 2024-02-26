package br.com.guilherme.androidtechuol.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.guilherme.androidtechuol.data.models.Album
import br.com.guilherme.androidtechuol.databinding.ItemUserdetailsBinding

class UserDetailsAdapter(
    private val context: Context,
    private val albuns: List<Album>,
    var onItemClickListener: (album: Album) -> Unit = {}
) : RecyclerView.Adapter<UserDetailsAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemUserdetailsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album) {
            binding.userDetailAlbumName.text = album.title
            binding.root.setOnClickListener {
                onItemClickListener(album)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = ItemUserdetailsBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return albuns.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = albuns[position]
        holder.bind(user)
    }
}