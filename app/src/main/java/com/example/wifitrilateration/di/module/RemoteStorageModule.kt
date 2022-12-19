package com.example.wifitrilateration.di.module

import android.content.Context
import android.util.Log
import com.example.wifitrilateration.data.remote.AuthService
import com.example.wifitrilateration.data.remote.MapResourceService
import com.example.wifitrilateration.data.remote.RouterConfigurationService
import com.example.wifitrilateration.data.remote.UserPositionService
import com.example.wifitrilateration.data.remote.interceptor.*
import com.example.wifitrilateration.utils.getResourceString
import com.glebalekseevjk.wifitrilateration.R
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier


@Module
interface RemoteStorageModule {

    companion object {
        @Provides
        fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
            return HttpLoggingInterceptor {
                Log.d("Network", it)
            }
                .setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        // Retrofit Builder`s
        @AuthServer
        @Provides
        fun provideAuthRetrofitBuilder(context: Context): Retrofit.Builder {
            return Retrofit.Builder()
                .baseUrl(context.getResourceString(R.string.default_server_auth_url))
                .addConverterFactory(GsonConverterFactory.create())
        }

        @ApiServer
        @Provides
        fun provideApiRetrofitBuilder(context: Context): Retrofit.Builder {
            return Retrofit.Builder()
                .baseUrl(context.getResourceString(R.string.default_server_api_url))
                .addConverterFactory(GsonConverterFactory.create())
        }

//        // Retrofit Api
        @Provides
        fun provideAuthService(
            @AuthServer retrofitBuilder: Retrofit.Builder,
            @AuthServer okHttpClient: OkHttpClient
        ): AuthService {
            return retrofitBuilder
                .client(okHttpClient)
                .build()
                .create(AuthService::class.java)
        }
//
        @Provides
        fun provideMapResourceService(
            @ApiServer retrofitBuilder: Retrofit.Builder,
            @ApiServerMapRevision okHttpClient: OkHttpClient
        ): MapResourceService {
            return retrofitBuilder
                .client(okHttpClient)
                .build()
                .create(MapResourceService::class.java)
        }

        @Provides
        fun provideRouterConfigurationService(
            @ApiServer retrofitBuilder: Retrofit.Builder,
            @ApiServer okHttpClient: OkHttpClient
        ): RouterConfigurationService {
            return retrofitBuilder
                .client(okHttpClient)
                .build()
                .create(RouterConfigurationService::class.java)
        }

        @Provides
        fun provideUserPositionService(
            @ApiServer retrofitBuilder: Retrofit.Builder,
            @ApiServer okHttpClient: OkHttpClient
        ): UserPositionService {
            return retrofitBuilder
                .client(okHttpClient)
                .build()
                .create(UserPositionService::class.java)
        }

        // OkHttpClients

        @AuthServer
        @Provides
        fun provideAuthServerOkHttpClient(
            httpLoggingInterceptor: HttpLoggingInterceptor,
            authorizationInterceptor: AuthorizationInterceptor,
        ): OkHttpClient {
            return OkHttpClient.Builder()
                .connectTimeout(200, TimeUnit.MILLISECONDS)
                .addNetworkInterceptor(httpLoggingInterceptor)
                .addNetworkInterceptor(authorizationInterceptor)
                .build()
        }

        @ApiServerMapRevision
        @Provides
        fun provideApiServerOkHttpClient(
            httpLoggingInterceptor: HttpLoggingInterceptor,
            authorizationFailedInterceptor: AuthorizationFailedInterceptor,
            authorizationInterceptor: AuthorizationInterceptor,
            synchronizedInterceptor: SynchronizedInterceptor,
            mapRevisionInterceptor: MapRevisionInterceptor,
            mapRevisionFailedInterceptor: MapRevisionFailedInterceptor
        ): OkHttpClient {
            return OkHttpClient.Builder()
                .connectTimeout(200, TimeUnit.MILLISECONDS)
                .addNetworkInterceptor(httpLoggingInterceptor)
                .addInterceptor(mapRevisionFailedInterceptor)
                .addInterceptor(authorizationFailedInterceptor)
                .addNetworkInterceptor(authorizationInterceptor)
                .addNetworkInterceptor(mapRevisionInterceptor)
                .addNetworkInterceptor(synchronizedInterceptor)
                .build()
        }

        @ApiServer
        @Provides
        fun provideApiServerMapRevisionOkHttpClient(
            httpLoggingInterceptor: HttpLoggingInterceptor,
            authorizationFailedInterceptor: AuthorizationFailedInterceptor,
            authorizationInterceptor: AuthorizationInterceptor,
        ): OkHttpClient {
            return OkHttpClient.Builder()
                .connectTimeout(200, TimeUnit.MILLISECONDS)
                .addNetworkInterceptor(httpLoggingInterceptor)
                .addInterceptor(authorizationFailedInterceptor)
                .addNetworkInterceptor(authorizationInterceptor)
                .build()
        }


        @Qualifier
        @Retention(AnnotationRetention.RUNTIME)
        annotation class AuthServer

        @Qualifier
        @Retention(AnnotationRetention.RUNTIME)
        annotation class ApiServer

        @Qualifier
        @Retention(AnnotationRetention.RUNTIME)
        annotation class ApiServerMapRevision

    }
}


