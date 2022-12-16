package com.example.gps.board

data class CommentVO(
    var comment: String,
    var uid: String,
    var time: String,
) {
    constructor(): this("", "", "")
}