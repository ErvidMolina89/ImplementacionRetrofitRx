package com.example.manejoretrofitrx.ConnectionRetrofitRx

interface IServiceParameters {

    enum class Methods{
        GET,
        POST,
        PUT,
        DELETE,
        OPTIONS,
    }

    fun getURL() : String
    fun getMethods() : Methods
    fun getUrlWithComplement(complement: String = ""): IServiceParameters
}