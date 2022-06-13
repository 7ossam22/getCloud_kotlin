package com.example.cloud.api

import android.content.Context
import android.net.Uri
import com.example.cloud.model.CloudData
import com.example.cloud.model.UserData
import com.google.firebase.storage.UploadTask
import java.net.URI

interface ApiCalls {
    suspend fun login(email: String, password: String): Boolean
    suspend fun register(username: String, email: String, password: String): Boolean
    suspend fun getAllData(): List<CloudData>
    suspend fun getUserData(): UserData
    suspend fun uploadFileToDataBase(uri: Uri, fileName: String)
    suspend fun downloadDataFromDataBase(context: Context, link: String, name: String)
    suspend fun deleteEntireAccount(uid: String)
}