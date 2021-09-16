package com.example.cloud.api


import com.example.cloud.data.CloudData
import com.example.cloud.data.UserData
import com.google.firebase.FirebaseApiNotAvailableException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.firestore.ktx.toObject

open class FirebaseApi : ApiCalls {
    private val firebaseFirestore = FirebaseFirestore.getInstance()
    private val auth = Firebase.auth
    private val reference = firebaseFirestore.collection("users")
    lateinit var userData: UserData
    private lateinit var cloudData : List<CloudData>

    //Firebase singleton
    companion object {

        private var instanse: FirebaseApi? = null

        fun singleton(): FirebaseApi {
            if (instanse == null) {
                instanse = FirebaseApi()
            }
            return instanse as FirebaseApi
        }
    }

    override suspend fun login(email: String, password: String): Boolean {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            true
        } catch (e: FirebaseAuthInvalidUserException) {
            false
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            false
        } catch (e: FirebaseNetworkException) {
            false
        } catch (e: FirebaseApiNotAvailableException) {
            false
        }

    }

    override suspend fun register(username: String, email: String, password: String): Boolean {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            onPushingUserData(username,email)
            true
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            false
        } catch (e: FirebaseAuthInvalidUserException) {
            false
        }
    }

    override suspend fun getAllData(): List<CloudData> {
        cloudData = reference.document(auth.currentUser!!.uid).collection("data").get().await().toObjects()
        return cloudData
    }

    override suspend fun getUserData(): UserData {
        userData = reference.document(auth.currentUser!!.uid).get().await().toObject<UserData>()!!
        return userData

    }
    private suspend fun onPushingUserData(username: String, email: String)
    {
        val map = HashMap<String,String>()
        map["name"] = username
        map["email"] = email
        map["profilePic"] = ""
        map["usage"] = "0"
        reference.document(auth.currentUser!!.uid).set(map).await()
    }
}