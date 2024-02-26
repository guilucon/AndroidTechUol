package br.com.guilherme.androidtechuol.data.api

import br.com.guilherme.androidtechuol.data.models.Album
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AlbumService {
    @GET("/albums")
    fun getAlbums(@Query("userId") userId: Int): Call<List<Album>>
}