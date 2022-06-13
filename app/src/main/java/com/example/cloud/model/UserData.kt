package com.example.cloud.model

data class UserData(
    var name: String,
    var profilePic: String,
    var email: String,
    var usage: String
) {
    constructor() : this("", "", "", "")
}
