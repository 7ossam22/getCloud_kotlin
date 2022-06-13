package com.example.cloud.viewModels.profileViewModel

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cloud.api.FirebaseApi
import kotlinx.coroutines.*

class ProfileViewModel() : ViewModel() {
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)
    private val api = FirebaseApi()
    private val _imageSelecting: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    val imageSelecting: LiveData<Boolean> get() = _imageSelecting

    fun uploadPicture(uri: Uri, name: String) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                api.uploadFileToDataBase(uri, name)
            }
        }
    }

    fun selectingImageFromStorage() {
        _imageSelecting.value = true
    }

    fun onSelectingImageFromStorageComplete() {
        _imageSelecting.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}