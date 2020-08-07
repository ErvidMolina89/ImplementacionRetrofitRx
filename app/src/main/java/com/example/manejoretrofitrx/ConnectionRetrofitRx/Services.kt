package com.example.manejoretrofitrx.ConnectionRetrofitRx


enum class Services (url : String,
                     method : IServiceParameters.Methods)
    : IServiceParameters {

    //list_movie_populars("movie/popular?api_key=8356aae6ec4d020df19b90310e913e16",IServiceParameters.Methods.GET),
    list_citas("api/preagendar_cita_con_psicologo",IServiceParameters.Methods.POST),
    list_citas_put("api/preagendar_cita_con_psicologo",IServiceParameters.Methods.PUT),
    list_citas_delete("api/preagendar_cita_con_psicologo",IServiceParameters.Methods.DELETE),
    list_citas_options("api/preagendar_cita_con_psicologo",IServiceParameters.Methods.OPTIONS),
    ;

    private val url : String
    private val method : IServiceParameters.Methods
    private var complement: String = ""

    init {
        this.url = url
        this.method = method
    }

    override fun getURL(): String {
        return url + complement
    }

    override fun getMethods(): IServiceParameters.Methods {
        return method
    }

    override fun getUrlWithComplement(complement: String): Services {
        this.complement = complement
        return this
    }
}