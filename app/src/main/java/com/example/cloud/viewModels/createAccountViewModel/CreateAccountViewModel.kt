package com.example.cloud.viewModels.createAccountViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cloud.api.FirebaseApi
import kotlinx.coroutines.*

class CreateAccountViewModel : ViewModel() {
    private val job = Job()
    private val uiScope : CoroutineScope = CoroutineScope(Dispatchers.Main+job)
    private val firebaseApi = FirebaseApi()
    private val _spinner = MutableLiveData<Boolean>()
    private val _registerSuccess = MutableLiveData<Boolean>()

    val spinner : LiveData<Boolean> get() = _spinner
    val registerSuccess : LiveData<Boolean> get() = _registerSuccess

    fun register(username:String,email:String,password:String)
    {
        _spinner.value = true
        uiScope.launch {
            val boolean =firebaseApi.register(username, email, password)
            if (boolean)
            {
                _spinner.value = false
                _registerSuccess.value = true
            }
            else
            {
                _spinner.value = false
                _registerSuccess.value = false
            }
            withContext(Dispatchers.IO)
            {
                onCleared()
            }
        }
    }



    override fun onCleared() {
        super.onCleared()
        job.cancel()
        _spinner.value = null
        _registerSuccess.value = null
    }
}