package com.example.manejoretrofitrx.Connection

import io.reactivex.Observable
import retrofit2.http.*

interface GetData {

    @GET
    fun getData(@Url ruta: String): Observable<Any>
    @POST
    fun postData(@Url ruta: String, @Body body: Any?): Observable<Any>
    @DELETE
    fun deleteData(@Url ruta: String, @Body body: Any?): Observable<Any>
    @PUT
    fun putData(@Url ruta: String, @Body body: Any?): Observable<Any>
    @OPTIONS
    fun optionsData(@Url ruta: String, @Body body: Any?): Observable<Any>
}