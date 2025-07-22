package com.digitallogic.core_data.remote.retrofit

import android.util.Base64
import androidx.core.os.BuildCompat
import com.digitallogic.core_data.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://upload.imagekit.io/api/"

    private val authInterceptor = Interceptor { chain ->
        val original = chain.request()
        val credentials = "${BuildConfig.IMAGEKIT_PRIVATE_KEY}:"
        val encoded = Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)

        val newRequest = original.newBuilder()
            .header("Authorization", "Basic $encoded")
            .build()
        chain.proceed(newRequest)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    val instance: ImageKitService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ImageKitService::class.java)
    }
}
