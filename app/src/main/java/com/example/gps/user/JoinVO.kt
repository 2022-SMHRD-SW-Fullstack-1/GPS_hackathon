package com.example.gps.user

data class JoinVO(val uid: String, val nick: String, val profileUrl: String, val email: String) {

    constructor() : this("", "", "", "")

}