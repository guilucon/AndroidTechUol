package br.com.guilherme.androidtechuol.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.guilherme.androidtechuol.data.api.ApiConfig
import br.com.guilherme.androidtechuol.data.models.User
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserListViewModel : ViewModel() {
    private var originalUserList: List<User>? = null
    private val _userList = MutableLiveData<List<User>?>()
    val userList: LiveData<List<User>?> get() = _userList

    init {
        fetchUserList()
    }

    private fun fetchUserList() {
        viewModelScope.launch {
            try {
                val response = withContext(IO) {
                    ApiConfig().userService.getAll().execute()
                }
                if (response.isSuccessful) {
                    val users = response.body()
                    originalUserList = users // Armazenar a lista original
                    _userList.postValue(users)
                }
            } catch (e: Exception) {
                Log.i("fetchUserList", "Erro ao obter a lista de users.")
            }
        }
    }

    fun filterUserList(query: String) {
        val originalList = originalUserList
        if (query.isNullOrEmpty()) {
            _userList.postValue(originalList)
            return
        }

        val filteredList = originalList?.filter { user ->
            user.name.contains(query, ignoreCase = true) ||
                    user.username.contains(query, ignoreCase = true) ||
                    user.email.contains(query, ignoreCase = true)
        }
        _userList.postValue(filteredList)
    }
}