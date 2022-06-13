package com.example.cloud.api


import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import com.example.cloud.model.CloudData
import com.example.cloud.model.UserData
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
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.lang.Exception

const val STORAGE_NAME = "Data"

@Suppress("DEPRECATION")
open class FirebaseApi : ApiCalls {
    private val firebaseFirestore = FirebaseFirestore.getInstance()
    private val auth = Firebase.auth
    private val reference = firebaseFirestore.collection("users")
    private val storage: StorageReference = FirebaseStorage.getInstance().getReference(STORAGE_NAME)
    lateinit var userData: UserData
    private lateinit var cloudData: List<CloudData>

    companion object {

        private  var instanse: FirebaseApi? = null

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
        cloudData =
            reference.document(auth.currentUser!!.uid).collection("data").get().await().toObjects()
        return cloudData
    }

    override suspend fun getUserData(): UserData {
        userData = reference.document(auth.currentUser!!.uid).get().await().toObject<UserData>()!!
        return userData

    }

    override suspend fun uploadFileToDataBase(uri: Uri, fileName: String) {
        try {
            storage.child(fileName).putFile(uri).await()
        } catch (e: Exception) {

        }
    }


    //this function will be fired once the user clicked on downloading file
    override suspend fun downloadDataFromDataBase(context: Context, link: String, name: String) {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val uri = Uri.parse(link)
        val request = DownloadManager.Request(uri)
        request.setTitle(name)
        request.setDescription("Downloading")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationUri(Uri.fromFile(Environment.getExternalStorageDirectory()))
        downloadManager.enqueue(request)

    }

    //this function will be fired if the user requested to delete his
    override suspend fun deleteEntireAccount(uid: String) {
        TODO("Not yet implemented")
    }

}