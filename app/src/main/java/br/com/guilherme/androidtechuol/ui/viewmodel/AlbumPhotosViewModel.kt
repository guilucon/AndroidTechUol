package br.com.guilherme.androidtechuol.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.guilherme.androidtechuol.data.api.ApiConfig
import br.com.guilherme.androidtechuol.data.models.Photo
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlbumPhotosViewModel : ViewModel() {
    private val _photoList = MutableLiveData<List<Photo>?>()
    val photoList: MutableLiveData<List<Photo>?> get() = _photoList

    fun fetchPhotoList(albumId: Int) {
        viewModelScope.launch {
            try {
                val response = withContext(IO) {
                    ApiConfig().photoService.getPhotos(albumId).execute()
                }
                if (response.isSuccessful) {
                    val photos = response.body()
                    _photoList.postValue(photos)
                }
            } catch (e: Exception) {
                Log.i("fetchPhotoList", "Erro ao obter a lista de photos.")
            }
        }
    }
}