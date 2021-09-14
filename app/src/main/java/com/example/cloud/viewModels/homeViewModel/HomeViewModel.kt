package com.example.cloud.viewModels.homeViewModel

import android.widget.ImageView
import androidx.core.net.toUri
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
    private val _percentage = MutableLiveData<Double>()
    private val _cloudList = MutableLiveData<List<CloudData>>()


    val usage: LiveData<String> get() = _usage
    val percentage: LiveData<Double> get() = _percentage
    val cloudList: LiveData<List<CloudData>> get() = _cloudList
    fun showData(imgView: ImageView) {
        uiScope.launch {
            _cloudList.value = api.getAllData()
            withContext(Dispatchers.IO) {
                api.getUserData()
            }
            Glide
                .with(imgView.context)
                .load(api.getUserData().profilePic.toUri().buildUpon().scheme("https").build())
                .centerCrop()
                .placeholder(R.drawable.ic_person)
                .into(imgView)

            val capacity: Int = api.userData.usage.toInt()
            val sizeKB = (capacity / 1024).toDouble()
            val sizeMB = sizeKB / 1024
            val sizeGB = sizeMB / 1024
            val sizeTB = sizeGB / 1024
            _percentage.value = sizeKB / 5242880 * 100
            when {
                sizeTB >= 1 -> {
                    _usage.value = (sizeTB.toInt().toString() + "TB used /5GB")
                }
                sizeGB >= 1 -> {
                    _usage.value = (sizeGB.toInt().toString() + "GB used /5GB")
                }
                sizeMB >= 1 -> {
                    _usage.value = (sizeMB.toInt().toString() + "MB used /5GB")
                }
                sizeKB >= 1 -> {
                    _usage.value = (sizeKB.toInt().toString() + "KB used /5GB")
                }
                else -> {
                    _usage.value = (capacity.toString() + "Bytes used /5GB")
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}