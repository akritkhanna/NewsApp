package com.newsapp.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Tools {



    fun utcToLocal(dateToConvert : String, outputFormat : String) : String{
        var dateToReturn = dateToConvert;
        val inputFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'"

        val sdfInput = SimpleDateFormat(inputFormat, Locale.getDefault())
        sdfInput.timeZone = TimeZone.getTimeZone("UTC")

        val sdfOutput = SimpleDateFormat(outputFormat, Locale.getDefault())
        sdfOutput.timeZone = TimeZone.getDefault()




        try {
           val gmt = sdfInput.parse(dateToConvert)
            dateToReturn = sdfOutput.format(gmt)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return dateToReturn

    }

    fun checkInternet(context: Context?): Boolean {
        return if (context != null) {
            val cm = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            (activeNetwork != null
                    && activeNetwork.isConnected)
        } else {
            false
        }
    }


}