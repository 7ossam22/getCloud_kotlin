package com.example.cloud.viewModels

import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.example.cloud.R
import com.example.cloud.api.FirebaseApi
import com.example.cloud.data.CloudData
import kotlinx.coroutines.*

class HomeViewModel : ViewModel() {
    private val job = Job()
    private val uiScope: CoroutineScope = CoroutineScope(Dispatchers.Main + job)
    private val api = FirebaseApi.singleton()
    private val _usage = MutableLiveData<String>()
    private val _percentage = MutableLiveData<Int>()
    private val _cloudList = MutableLiveData<List<CloudData>>()
    private val _listChecker = MutableLiveData<Boolean>()
    private val _progressListener = MutableLiveData<Boolean>()


    val usage: LiveData<String> get() = _usage
    val percentage: LiveData<Int> get() = _percentage
    val cloudList: LiveData<List<CloudData>> get() = _cloudList
    val listChecker: LiveData<Boolean> get() = _listChecker
    val progressListener: LiveData<Boolean> get() = _progressListener
    fun showData(imgView: ImageView) {
        uiScope.launch {
            _progressListener.value = true
            _cloudList.value = api.getAllData()
            if (_cloudList.value.isNullOrEmpty()) {
                _progressListener.value = false
                _listChecker.value = true
            } else {
                _progressListener.value = false
            }
            withContext(Dispatchers.IO) {
                api.getUserData()
            }
            Glide
                .with(imgView.context)
                .load(api.getUserData().profilePic)
                .circleCrop()
                .placeholder(R.drawable.ic_person)
                .into(imgView)

            val sizeKB = (api.userData.usage.toDouble() / 1024)
            val sizeMB = sizeKB / 1024
            val sizeGB = sizeMB / 1024
            val sizeTB = sizeGB / 1024
            _percentage.value = ((sizeKB / 5242880) * 100).toInt()
            _usage.value = when {
                sizeTB >= 1 -> sizeTB.toInt().toString() + "TB used /5GB"
                sizeGB >= 1 -> sizeGB.toInt().toString() + "GB used /5GB"
                sizeMB >= 1 -> sizeMB.toInt().toString() + "MB used /5GB"
                sizeKB >= 1 -> sizeKB.toInt().toString() + "KB used /5GB"
                else -> api.userData.usage + "Bytes used /5GB"
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}