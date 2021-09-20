package com.example.cloud.data

import com.google.firebase.firestore.DocumentId

class CloudData {
    @DocumentId
    lateinit var id: String
    lateinit var date: String
    lateinit var name: String
    lateinit var size: String
    lateinit var type: String
    lateinit var link: String
}