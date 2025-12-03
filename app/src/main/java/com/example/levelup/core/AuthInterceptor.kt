package com.example.levelup.core

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val requestBuilder = original.newBuilder()

        val token = UserSession.jwt

        val isAuthCall = original.url.encodedPath.startsWith("/api/v1/auth")

        if(!token.isNullOrBlank() && !isAuthCall){
            requestBuilder.header("Authorization","Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}