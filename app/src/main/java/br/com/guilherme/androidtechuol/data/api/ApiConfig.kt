package br.com.guilherme.androidtechuol.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    private val retrofit = Retrofit.Builder()
                                   .baseUrl("https://jsonplaceholder.typicode.com/")
                                   .addConverterFactory(GsonConverterFactory.create())
                                   .build()

    val userService: UserService = retrofit.create(UserService::class.java)
    val albumService: AlbumService = retrofit.create(AlbumService::class.java)
    val photoService: PhotoService = retrofit.create(PhotoService::class.java)
}