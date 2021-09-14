package com.example.cloud.api

import com.example.cloud.data.CloudData
import com.example.cloud.data.UserData


interface ApiCalls {
    suspend fun login(email:String,password:String): Boolean
    suspend fun register(username:String,email:String,password:String):Boolean
    suspend fun getAllData(): List<CloudData>
    suspend fun getUserData() : UserData
}