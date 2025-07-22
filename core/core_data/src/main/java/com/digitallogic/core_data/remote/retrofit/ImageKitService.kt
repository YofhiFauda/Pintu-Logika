package com.digitallogic.core_data.remote.retrofit


import com.digitallogic.core_data.remote.response.ImageKitResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ImageKitService {
    @Multipart
    @POST("v1/files/upload")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("fileName") fileName: RequestBody,
        @Part("useUniqueFileName") useUniqueFileName: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), "true")
    ): Response<ImageKitResponse>
}
