package com.example.cloud.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cloud.api.FirebaseApi
import com.example.cloud.api.LoginOperation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)
    private val api = FirebaseApi.singleton()

    private val _spinner = MutableLiveData<Boolean>()
    private val _loginSuccess = MutableLiveData<LoginOperation>()

    val spinner: LiveData<Boolean> get() = _spinner
    val loginSuccess: LiveData<LoginOperation> get() = _loginSuccess

    fun login(email: String, password: String) {
        _loginSuccess.value = LoginOperation.OnHold
        _spinner.value = true
        uiScope.launch {
            _loginSuccess.value = api.login(email, password)
            _spinner.value = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}