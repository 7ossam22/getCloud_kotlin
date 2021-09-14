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

open class FirebaseApi : ApiCalls {
    private val firebaseFirestore = FirebaseFirestore.getInstance()
    private val auth = Firebase.auth
    private val reference = firebaseFirestore.collection("Users")
    lateinit var userData: UserData

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
            true
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            false
        } catch (e: FirebaseAuthInvalidUserException) {
            false
        }
    }

    override suspend fun getAllData(): List<CloudData> {
        val response: List<CloudData>?
        response = reference.document(auth.currentUser!!.uid).collection("Data").get().await().toObjects()
        return response
    }

    override suspend fun getUserData(): UserData {
        userData = UserData()
        reference.document(auth.currentUser!!.uid).get().addOnCompleteListener {
            userData.name = it.result!!.get("Name").toString()
            userData.email = it.result!!.get("Email").toString()
            userData.profilePic = it.result!!.get("Profile_Pic").toString()
            userData.usage = it.result!!.get("Usage").toString()
        }.await()
        return this.userData
    }

}