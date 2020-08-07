package com.example.manejoretrofitrx.Connection

import com.example.manejoretrofitrx.ConnectionRetrofitRx.IRetrofitParcelable
import com.google.gson.annotations.SerializedName

class RetroCrypto: IRetrofitParcelable{
    var page                    : Int       ?= null
    @SerializedName("codigo")
    var total_results           : Int       ?= null
    @SerializedName("estado")
    var total_pages             : Int       ?= null
    var results                 : MutableList<MoviePopular> ?= null
}