package com.example.gps.board

data class CommentVO(val nick: String, val msg: String, val time: String,val key: String ) {

    //반드시 FireBase RealTime DataBase를 사용하려면 기본생성자를 만들어야 함
    constructor(): this("","","", "")


}