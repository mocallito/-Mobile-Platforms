package com.example.report

import org.json.JSONObject

data class Post(
    /*
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String

     */

    val keywords: List<PostArg>,
    val status: String


    //val obj: JSONObject
)

data class PostArg(
    val keyword: String,
    val score: Double
)