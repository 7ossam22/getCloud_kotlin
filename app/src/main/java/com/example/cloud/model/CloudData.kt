package com.example.cloud.model

import com.google.firebase.firestore.DocumentId

data class CloudData(
    @DocumentId
    var id: String,
    var date: String,
    var name: String,
    var size: String,
    var type: String,
    var link: String
) {
    constructor() : this("", "", "", "", "", "")
}
