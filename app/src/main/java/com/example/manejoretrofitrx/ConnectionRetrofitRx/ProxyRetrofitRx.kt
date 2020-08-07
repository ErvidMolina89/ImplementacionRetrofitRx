package com.example.manejoretrofitrx.ConnectionRetrofitRx

import android.content.Context
import android.util.Log
import com.example.manejoretrofitrx.BuildConfig
import com.example.manejoretrofitrx.Connection.GetData
import com.example.manejoretrofitrx.R
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ProxyRetrofitRx (private val context: Context) {

    fun loadData(){

        val myCompositeDisposable = CompositeDisposable()
        val client = generarCabezera()
        val rxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create()
        //Define the Retrofit request//

        val requestInterface = Retrofit.Builder()
            .baseUrl(BuildConfig.Base_Url)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(rxJava2CallAdapterFactory)
            .build()


        val implementacionDeInterface = requestInterface.create(GetData::class.java)

        myCompositeDisposable.add(generarGetData(implementacionDeInterface)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeWith(manejadorDeRespuesta())
        )

    }

    private fun generarCabezera(): OkHttpClient {
        val httpClient = OkHttpClient.Builder().addInterceptor(object:Interceptor{
            override fun intercept(chain: Interceptor.Chain): Response {
                val original = chain.request()
                val request = original.newBuilder()
                request.addHeader("autorization", "new_token")
                codigoRespuesta?.invoke(chain.proceed(request.build()).code())
                return  chain.proceed(request.build())
            }
        }).build()
        return httpClient
    }

    private fun generarGetData(requestInterface: GetData): Observable<Any>{
        return when(serviceSelected?.getMethods()) {
            IServiceParameters.Methods.GET -> requestInterface.getData(serviceSelected?.getURL()!!)
            IServiceParameters.Methods.POST -> requestInterface.postData(serviceSelected?.getURL()!!, objetoAEnviar)
            IServiceParameters.Methods.PUT -> requestInterface.putData(serviceSelected?.getURL()!!, objetoAEnviar)
            IServiceParameters.Methods.DELETE -> requestInterface.deleteData(serviceSelected?.getURL()!!, objetoAEnviar)
            IServiceParameters.Methods.OPTIONS -> requestInterface.optionsData(serviceSelected?.getURL()!!, objetoAEnviar)
            else -> requestInterface.getData(serviceSelected?.getURL()!!)
        }
    }

    private fun manejadorDeRespuesta(): DisposableObserver<Any> {
        return object : DisposableObserver<Any>(){
            override fun onComplete() {

            }

            override fun onNext(t: Any) {
                handleResponse(t)
            }

            override fun onError(e: Throwable) {
                EscuchadorFalla?.invoke(e)
            }

        }
    }

    private  var codigoRespuesta : ((Int?)->Unit) ?= null
    fun conManejadorCodigoRespuesta(code : ((Int?)->Unit)): ProxyRetrofitRx {
        this.codigoRespuesta = code
        return this
    }

    private fun handleResponse(response: Any){
        EscuchadorRespuesta?.invoke(response)
    }

    private  var EscuchadorRespuesta : ((Any?)->Unit) ?= null
    fun conEscuchadorRespuesta(responseRX:((Any?)->Unit)): ProxyRetrofitRx {
        this.EscuchadorRespuesta = responseRX
        return this
    }

    private  var EscuchadorFalla : ((Throwable)->Unit) ?= null
    fun conEscuchadorFalla(falla:((Throwable)->Unit)): ProxyRetrofitRx {
        this.EscuchadorFalla = falla
        return this
    }

    private var serviceSelected : IServiceParameters ?= null
    fun withService(service : IServiceParameters) : ProxyRetrofitRx{
        this.serviceSelected = service
        return this
    }

    private var objetoAEnviar: IRetrofitParcelable? = null
    fun conObjetoAEnviar(objeto: IRetrofitParcelable?): ProxyRetrofitRx {
        this.objetoAEnviar = objeto
        return this
    }
}