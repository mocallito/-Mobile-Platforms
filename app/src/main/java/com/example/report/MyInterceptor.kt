package com.example.report

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class MyInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain) : Response {
        val request : Request = chain. request()
            .newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("X-Platform", "Android")
            .addHeader("Accept", "application/json")
            //.addHeader("User-Agent", "Report/1.0")
            //.addHeader("X-Auth-Token", "GAk0QhLz6I5ERr3uqUpu9nSq:XRjGjenjJ0RveekulpR93hrK80JOMabmuo53mUi3w21bpGRW")
            .addHeader("Authorization", "Basic R0FrMFFoTHo2STVFUnIzdXFVcHU5blNxOlhSakdqZW5qSjBSdmVla3VscFI5M2hySzgwSk9NYWJtdW81M21VaTN3MjFicEdSVw==")
            //.addHeader("X-Request-ID", "GAk0QhLz6I5ERr3uqUpu9nSq")
            .build()
        return chain.proceed(request)
    }
}