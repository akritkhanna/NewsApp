package com.newsapp.di

import android.content.Context
import com.newsapp.api.ApiInterface
import com.newsapp.util.Constants
import com.newsapp.util.Tools
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideRetrofit(@ApplicationContext context: Context): Retrofit {

        val builder = OkHttpClient.Builder()
        //This is to check the logs of api request
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        builder.writeTimeout(60, TimeUnit.SECONDS)
        builder.readTimeout(60, TimeUnit.SECONDS)
        builder.connectTimeout(60, TimeUnit.SECONDS)

        //create network interceptor

        //create network interceptor
        builder.addNetworkInterceptor(Interceptor { chain: Interceptor.Chain ->
            val request = chain.request()
            chain.proceed(request).newBuilder()
                .header("Cache-Control", "public")
                .removeHeader("Pragma")
                .build()
        })

        //create offline interceptor

        //create offline interceptor
        builder.addInterceptor(Interceptor { chain: Interceptor.Chain ->
            var request = chain.request()

            if (!Tools.checkInternet(context)) {
                val maxStale = 60 * 60 * 24 * 30 // Offline cache available for 30 days
                request = request.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                    .removeHeader("Pragma")
                    .build()
            }
            chain.proceed(request)
        })

        builder.addInterceptor(logging)

        //setup cache


        //setup cache
        val httpCacheDirectory: File = File(context.getCacheDir(), "responses")
        val cacheSize = 10 * 1024 * 1024 // 10 MB

        val cache = Cache(httpCacheDirectory, cacheSize.toLong())

        builder.cache(cache)


        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(builder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideApi(retrofit: Retrofit): ApiInterface = retrofit.create(ApiInterface::class.java)


/*    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            null,
            null
        )*/
}