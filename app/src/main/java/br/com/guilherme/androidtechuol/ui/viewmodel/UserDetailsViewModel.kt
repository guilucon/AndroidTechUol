package br.com.guilherme.androidtechuol.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.guilherme.androidtechuol.data.api.ApiConfig
import br.com.guilherme.androidtechuol.data.models.Album
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserDetailsViewModel : ViewModel() {
    private val _albumList = MutableLiveData<List<Album>?>()
    val albumList: MutableLiveData<List<Album>?> get() = _albumList

    fun fetchAlbumList(userId: Int) {
        viewModelScope.launch {
            try {
                val response = withContext(IO) {
                    ApiConfig().albumService.getAlbums(userId).execute()
                }
                if (response.isSuccessful) {
                    val albums = response.body()
                    _albumList.postValue(albums)
                }
            } catch (e: Exception) {
                Log.i("fetchAlbumList", "Erro ao obter a lista de albuns.")
            }
        }
    }
}