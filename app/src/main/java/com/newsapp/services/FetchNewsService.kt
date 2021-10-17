package com.newsapp.services

import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log

class FetchNewsService : Service() {
    private val TAG = "FetchNewsService"
    private var countdownTimer : CountDownTimer? = null;


    init {

        Log.d(TAG, "Service started....")
    }

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()


        countdownTimer = object : CountDownTimer(300000 /*5 minutes*/, 1000){
            override fun onTick(p0: Long) {
                Log.d(TAG, "onTick: will latest news fetch in $p0")
            }

            override fun onFinish() {
                val fetchNewsIntent = Intent(packageName+"fetch_news")
                fetchNewsIntent.putExtra("shouldFetch", true)
                sendBroadcast(fetchNewsIntent)
            }

        }

        countdownTimer?.start()

    }

    override fun onDestroy() {
        countdownTimer?.cancel()
        super.onDestroy()
    }


}