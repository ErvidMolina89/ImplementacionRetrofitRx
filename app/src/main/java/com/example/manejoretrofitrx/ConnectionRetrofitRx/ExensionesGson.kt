package com.example.manejoretrofitrx.ConnectionRetrofitRx

import android.content.Context
import android.net.ConnectivityManager
import com.google.gson.Gson
import java.lang.Exception

fun IRetrofitParcelable.ConvertirAObjeto(json: String): IRetrofitParcelable{
    val tmp = Gson().fromJson(json, this.javaClass)
    return tmp
}

fun isNetworkAvailable(context: Context): Boolean {
    try {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    } catch (e: Exception) {
        return false
    }
}