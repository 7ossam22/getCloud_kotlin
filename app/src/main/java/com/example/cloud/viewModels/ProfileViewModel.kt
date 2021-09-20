package com.example.cloud.viewModels

import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.example.cloud.R
import com.example.cloud.api.FirebaseApi
import com.example.cloud.data.UserData
import kotlinx.coroutines.*

class ProfileViewModel : ViewModel() {
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main+job)
    private val api = FirebaseApi.singleton()
    private lateinit var data : UserData
    private val _userName = MutableLiveData<String>()
    private val _email = MutableLiveData<String>()
    private val _progress = MutableLiveData<Boolean>()

    val userName : LiveData<String> get() = _userName
    val email : LiveData<String> get() = _email
    val progress : LiveData<Boolean> get() = _progress
    fun onDisplayingUserData(img:ImageView){
        uiScope.launch {
            _progress.value = true
            withContext(Dispatchers.IO)
            {
                data = api.getUserData()
            }
                 Glide.with(img.context)
                .load(data.profilePic)
                .circleCrop()
                .placeholder(R.drawable.ic_person)
                .into(img)
               _userName.value = data.name
               _email.value = data.email
               _progress.value = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}