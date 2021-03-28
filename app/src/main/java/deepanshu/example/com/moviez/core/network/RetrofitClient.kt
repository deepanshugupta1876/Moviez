package deepanshu.example.com.moviez.core.network

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    var retrofitInstance: Retrofit? = null

    fun getRetrofitInstance(baseUrl: String): Retrofit {
        if (retrofitInstance == null) {
            createInstance(baseUrl)
        } else if (retrofitInstance!!.baseUrl().uri().toString() != baseUrl) {
            createInstance(baseUrl)
        }
        return retrofitInstance!!
    }

    private fun createInstance(baseUrl: String) {
        retrofitInstance = Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }
}