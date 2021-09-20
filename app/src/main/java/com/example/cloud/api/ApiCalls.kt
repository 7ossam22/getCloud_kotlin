package com.example.cloud.api

import com.example.cloud.data.CloudData
import com.example.cloud.data.UserData

enum class LoginOperation{ Ok, Failed, OnHold }
enum class RegisterOperation{ Ok, WeakPassword, InvalidEmail, EmailExists , OnHold }
interface ApiCalls {
    suspend fun login(email:String,password:String): LoginOperation
    suspend fun register(username:String,email:String,password:String):RegisterOperation
    suspend fun getAllData(): List<CloudData>
    suspend fun getUserData() : UserData
    suspend fun deleteItem(item: CloudData)
    suspend fun uploadItem()
}