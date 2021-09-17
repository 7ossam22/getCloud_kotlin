package com.example.cloud.api

import com.example.cloud.data.CloudData
import com.example.cloud.data.UserData

enum class LoggingOperation{Ok,Failed,OperationPrepared}
enum class RegisterOperation{ Ok, WeakPassword, InvalidEmail, EmailExists , OperationPrepared }
interface ApiCalls {
    suspend fun login(email:String,password:String): LoggingOperation
    suspend fun register(username:String,email:String,password:String):RegisterOperation
    suspend fun getAllData(): List<CloudData>
    suspend fun getUserData() : UserData
}