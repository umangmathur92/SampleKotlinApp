package com.umangmathur.umangdemoapp

import android.support.v4.app.Fragment
import com.umangmathur.umangdemoapp.network.BASE_URL
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

open class BaseFragment : Fragment()  {

    internal fun initializeRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    internal fun setActionBarTitle(title: String) {
        (activity as MainActivity).setActionBarTitle(title)
    }


}