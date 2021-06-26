package com.example.covidtrackerindia

import okhttp3.OkHttpClient
import okhttp3.Request


object Client {

    private  var client = OkHttpClient()

    private val URL = "https://api.covid19india.org/data.json"

    private var request = Request.Builder()
        .url(URL)
        .build()

    val api = client.newCall(request)

}