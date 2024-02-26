package br.com.guilherme.androidtechuol.ui.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.guilherme.androidtechuol.data.models.Photo
import br.com.guilherme.androidtechuol.databinding.ItemPhotoBinding
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream

class UserAlbumPhotosAdapter (
    private val context: Context,
    private val photos: List<Photo>,
) : RecyclerView.Adapter<UserAlbumPhotosAdapter.ViewHolder>() {
    private val picasso: Picasso = Picasso.Builder(context)
        .memoryCache(com.squareup.picasso.Cache.NONE)
        .build()

    inner class ViewHolder(private val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: Photo) {
            picasso.load(photo.thumbnailUrl).into(binding.imageView)
            binding.root.setOnClickListener {
                openImageInDefaultApp(context, photo.url)
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

    fun openImageInDefaultApp(context: Context, photoUrl: String) {
        picasso.load(photoUrl).into(object : com.squareup.picasso.Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                bitmap?.let {
                    val uri = getImageUri(context, it)
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        setDataAndType(uri, "image/*")
                        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    }
                    context.startActivity(intent)
                }
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                Log.e("Picasso", "Failed to load image", e)
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                //TODO: implementar um loading
            }
        })
    }

    fun getImageUri(context: Context, bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path)
    }
}