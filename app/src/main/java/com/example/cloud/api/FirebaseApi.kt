package com.example.cloud.api


import com.example.cloud.data.CloudData
import com.example.cloud.data.UserData
import com.google.firebase.FirebaseApiNotAvailableException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

open class FirebaseApi : ApiCalls {
    private val firebaseFirestore = FirebaseFirestore.getInstance()
    private val auth = Firebase.auth
    private val reference = firebaseFirestore.collection("users")
    lateinit var userData: UserData
    private lateinit var cloudData: List<CloudData>

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

    override suspend fun login(email: String, password: String): LoggingOperation {
        LoggingOperation.OperationPrepared
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            LoggingOperation.Ok
        } catch (e: FirebaseAuthInvalidUserException) { LoggingOperation.Failed }
        catch (e: FirebaseAuthInvalidCredentialsException) { LoggingOperation.Failed }
        catch (e: FirebaseNetworkException) { LoggingOperation.Failed }
        catch (e: FirebaseApiNotAvailableException) { LoggingOperation.Failed }

    }

    override suspend fun register(username: String, email: String, password: String): RegisterOperation {
        RegisterOperation.OperationPrepared
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            onPushingUserData(username, email)
            RegisterOperation.Ok
        } catch (e: FirebaseAuthWeakPasswordException) {
             RegisterOperation.WeakPassword
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            RegisterOperation.InvalidEmail

        } catch (e: FirebaseAuthUserCollisionException) {
            RegisterOperation.EmailExists

        }
    }

    override suspend fun getAllData(): List<CloudData> {
        cloudData =
            reference.document(auth.currentUser!!.uid).collection("data").get().await().toObjects()
        return cloudData
    }

    override suspend fun getUserData(): UserData {
        userData = reference.document(auth.currentUser!!.uid).get().await().toObject<UserData>()!!
        return userData

    }

    private suspend fun onPushingUserData(username: String, email: String) {
        val map = HashMap<String, String>()
        map["name"] = username
        map["email"] = email
        map["profilePic"] = ""
        map["usage"] = "0"
        reference.document(auth.currentUser!!.uid).set(map).await()
    }
}