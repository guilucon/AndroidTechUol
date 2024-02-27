package br.com.guilherme.androidtechuol.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.guilherme.androidtechuol.data.models.Photo
import br.com.guilherme.androidtechuol.databinding.ItemPhotoBinding
import br.com.guilherme.androidtechuol.utils.ImageUtils
import com.squareup.picasso.Picasso

class UserAlbumPhotosAdapter (
    private val context: Context,
    private val photos: List<Photo>,
) : RecyclerView.Adapter<UserAlbumPhotosAdapter.ViewHolder>() {
    private val picasso: Picasso = Picasso.Builder(context)
                                          .memoryCache(com.squareup.picasso.Cache.NONE)
                                          .build()
    val imageUtils = ImageUtils(context, picasso)

    inner class ViewHolder(private val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: Photo) {
            picasso.load(photo.thumbnailUrl).into(binding.imageView)
            binding.root.setOnClickListener {
                imageUtils.openImageInDefaultApp(photo.url)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = ItemPhotoBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photo = photos[position]
        holder.bind(photo)
    }


}