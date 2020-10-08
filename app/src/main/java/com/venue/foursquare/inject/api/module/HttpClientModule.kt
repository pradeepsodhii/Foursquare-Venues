package com.venue.foursquare.inject.api.module

import com.venue.foursquare.inject.api.ApiSessionScope
import com.venue.foursquare.inject.api.NetworkInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import okhttp3.Interceptor
import okhttp3.Response


@Module(includes = [InterceptorsModule::class])
class HttpClientModule(
    private val timeoutSeconds: Long = 250
) {

    @Provides
    @ApiSessionScope
    fun httpClient(
        interceptors: Set<@JvmSuppressWildcards Interceptor>,
        @NetworkInterceptor netInterceptors: Set<@JvmSuppressWildcards Interceptor>
    ): OkHttpClient {
        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient().newBuilder()
            .addInterceptor(RequestInterceptor())
            .cookieJar(CookieJar.NO_COOKIES)
            .connectTimeout(timeoutSeconds, TimeUnit.SECONDS)
            .readTimeout(timeoutSeconds, TimeUnit.SECONDS)
            .writeTimeout(timeoutSeconds, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
            .apply { interceptors.forEach { addInterceptor(it) } }
            .apply { netInterceptors.forEach { addNetworkInterceptor(it) } }
            .addInterceptor(logInterceptor)
            .build()
    }

    class RequestInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val url =  chain.request()
                .url
                .newBuilder()
                .addQueryParameter("client_id" , CLIENT_ID)
                .addQueryParameter("client_secret" , CLIENT_SECRET)
                .addQueryParameter("v" , DEVELOPMENT_DATE)
                .build()

            val request = chain.request()
                .newBuilder()
                .url(url)
                .build()

            return chain.proceed(request)
        }

    }

    companion object {
        private const val CLIENT_ID = "MWABYZWKPYWC3OPL3ZDL3N0TIVSCI5JC2QN3BYHLIIQKGOGE"
        private const val CLIENT_SECRET = "NLXLP2OBZSE2LCIGVTCJFDJIDD01MERGGAIUXUZNXDKZFLI4"
        private const val DEVELOPMENT_DATE = "202001008"
    }
}



