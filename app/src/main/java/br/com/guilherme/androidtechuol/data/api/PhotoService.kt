package br.com.guilherme.androidtechuol.data.api

import br.com.guilherme.androidtechuol.data.models.Photo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotoService {
    @GET("/photos")
    fun getPhotos(@Query("albumId") albumId: Int): Call<List<Photo>>
}