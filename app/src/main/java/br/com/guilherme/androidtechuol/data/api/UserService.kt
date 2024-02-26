package br.com.guilherme.androidtechuol.data.api

import br.com.guilherme.androidtechuol.data.models.User
import retrofit2.Call
import retrofit2.http.GET

interface UserService {
    @GET(value = "users")
    fun getAll(): Call<List<User>>
}