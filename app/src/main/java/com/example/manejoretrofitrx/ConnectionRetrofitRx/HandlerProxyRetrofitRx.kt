package com.example.manejoretrofitrx.ConnectionRetrofitRx

import android.content.Context
import android.util.Log
import com.example.manejoretrofitrx.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.net.ConnectException

class HandlerProxyRetrofitRx (private val context: Context) {

    private var listenerAnswerObjet : ((IRetrofitParcelable?)->Unit)?= null
    fun withListenerAnswerObjets(listenerAnswerObjet : ((IRetrofitParcelable?)->Unit)?) : HandlerProxyRetrofitRx{
        this.listenerAnswerObjet =  listenerAnswerObjet
        return this
    }

    private var listenerListObjet : ((MutableList<IRetrofitParcelable>?)->Unit)?= null
    fun withListenerListObjetcs(listenerListObjet : ((MutableList<IRetrofitParcelable>?)->Unit)?) : HandlerProxyRetrofitRx{
        this.listenerListObjet = listenerListObjet
        return this
    }

    private var listenerOfFailure : ((Int,Int)->Unit)?= null
    fun withListenerOfFailure(listenerOfFailure : ((Int,Int)->Unit)?) : HandlerProxyRetrofitRx{
        this.listenerOfFailure = listenerOfFailure
        return this
    }

    private var myClass : Class<*> ?= null
    fun withMyClass(myClass : Class<*>? ) : HandlerProxyRetrofitRx{
        this.myClass = myClass
        return this
    }

    private var myService : IServiceParameters ?= null
    fun withMyService(myService : IServiceParameters) : HandlerProxyRetrofitRx{
        this.myService = myService
        return this
    }

    private var myObjectSend : IRetrofitParcelable ?= null
    fun withObjectSend(myObjectSend : IRetrofitParcelable) : HandlerProxyRetrofitRx{
        this.myObjectSend = myObjectSend
        return this
    }

    fun EjecutarServicio () {
        ProxyRetrofitRx(context)
            .conEscuchadorRespuesta{
                answerPositive(it!!)
            }
            .conEscuchadorFalla {
                if (it is ConnectException){
                    listenerOfFailure?.invoke(R.string.Internet, R.string.detail_falla_Internet)
                }
            }
            .conManejadorCodigoRespuesta {

            }
            .withService(myService!!)
            .conObjetoAEnviar(myObjectSend)
            .loadData()
    }

    private fun answerPositive(response: Any) {
        val json = Gson().toJson(response)
        if(generateSingleObject(json)){
            return
        }
        if(generateListObject(json)){
            return
        }

        listenerAnswerObjet?.invoke(null)
        listenerListObjet?.invoke(null)
    }

    private fun generateSingleObject(json : String) : Boolean{
        return try {
            val myObject = Gson().fromJson(json, myClass!!)
            listenerAnswerObjet?.invoke(myObject as? IRetrofitParcelable)
            true
        }catch (e : Exception){
            false
        }
    }

    private fun generateListObject(json : String) : Boolean{
        return try {
            val myType = object : TypeToken<MutableList<Any>>(){}.type
            val myList = Gson().fromJson<Any>(json,myType) as MutableList<*>
            val castedList = emptyArray<Any>().toMutableList()
            for (item in myList){
                val json = Gson().toJson(item)
                val myObject = Gson().fromJson(json,myClass!!)
                castedList.add(myObject)
            }
            listenerListObjet?.invoke(castedList as? MutableList<IRetrofitParcelable>)
            true
        }catch (e  : Exception){
            false
        }
    }
}