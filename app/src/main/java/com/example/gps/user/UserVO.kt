package com.example.gps.user

data class UserVO(
    val email: String,
    val pw: String,
    val pwCk: String,
    val nick: String,
    val profilePic: String
) {
    constructor(): this("","","","","")
}