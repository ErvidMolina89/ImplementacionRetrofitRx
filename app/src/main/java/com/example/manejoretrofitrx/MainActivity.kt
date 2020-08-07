package com.example.manejoretrofitrx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.manejoretrofitrx.Connection.RetroCrypto
import com.example.manejoretrofitrx.ConnectionRetrofitRx.ConvertirAObjeto
import com.example.manejoretrofitrx.ConnectionRetrofitRx.HandlerProxyRetrofitRx
import com.example.manejoretrofitrx.ConnectionRetrofitRx.ProxyRetrofitRx
import com.example.manejoretrofitrx.ConnectionRetrofitRx.Services
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MyAdapter.Listener {

    private var myAdapter: MyAdapter? = null
    private var myRetroCryptoArrayList: ArrayList<RetroCrypto>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRecyclerView()
        loadData()
    }


//Initialise the RecyclerView//

    private fun initRecyclerView() {

//Use a layout manager to position your items to look like a standard ListView//

        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(this)
        rv_lista.layoutManager = layoutManager

    }
    //Implement loadData//

    private fun loadData() {
        HandlerProxyRetrofitRx(this)
            .withListenerAnswerObjets {
                handleResponse(it as RetroCrypto)
            }
            .withListenerListObjetcs {
                Log.e("", "")
            }
            .withListenerOfFailure { titulo, message ->

            }
            .withMyClass(RetroCrypto::class.java)
            .withMyService(Services.list_citas)
            .withObjectSend(llenarObjeto())
            .EjecutarServicio()
    }

    private fun llenarObjeto(): RetroCrypto{
        val item = RetroCrypto()
        item.total_pages = 5
        item.total_results = 2
        return  item
    }

    private fun handleResponse(cryptoList: RetroCrypto) {
        val list: MutableList<RetroCrypto> = emptyList<RetroCrypto>().toMutableList()
        list.add(cryptoList)

        myRetroCryptoArrayList = ArrayList(list)
        myAdapter = MyAdapter(myRetroCryptoArrayList!!, this)

//Set the adapter//

        rv_lista.adapter = myAdapter

    }
    override fun onItemClick(retroCrypto: RetroCrypto) {
        Toast.makeText(this, "You clicked: ${retroCrypto.total_pages}", Toast.LENGTH_LONG).show()
    }
}
