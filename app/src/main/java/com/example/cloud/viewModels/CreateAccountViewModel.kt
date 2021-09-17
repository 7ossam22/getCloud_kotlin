package com.example.cloud.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cloud.api.FirebaseApi
import com.example.cloud.api.RegisterOperation
import kotlinx.coroutines.*

class CreateAccountViewModel : ViewModel() {
    private val job = Job()
    private val uiScope: CoroutineScope = CoroutineScope(Dispatchers.Main + job)
    private val firebaseApi = FirebaseApi()
    private val _spinner = MutableLiveData<Boolean>()
    private val _registerSuccess = MutableLiveData<RegisterOperation>()

    val spinner: LiveData<Boolean> get() = _spinner
    val registerSuccess: LiveData<RegisterOperation> get() = _registerSuccess

    fun register(username: String, email: String, password: String) {
        _spinner.value = true
        uiScope.launch {
            _registerSuccess.value = firebaseApi.register(username, email, password)
            _spinner.value = false

        }
    }


    override fun onCleared() {
        super.onCleared()
        job.cancel()
        _spinner.value = null
        _registerSuccess.value = null
    }
}