package com.example.wifitrilateration.data.remote.interceptor

import androidx.lifecycle.asFlow
import com.example.wifitrilateration.data.preferences.SharedPreferencesSynchronizedStorage
import com.example.wifitrilateration.data.remote.RouterConfigurationService
import com.example.wifitrilateration.domain.entity.Router
import com.glebalekseevjk.yasmrhomework.data.local.dao.RouterDao
import com.glebalekseevjk.yasmrhomework.data.local.model.RouterDbModel
import com.glebalekseevjk.yasmrhomework.domain.mapper.Mapper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import javax.inject.Inject

class SynchronizedInterceptor @Inject constructor(
    private val synchronizedStorage: SharedPreferencesSynchronizedStorage,
    private val routerDao: RouterDao,
    private val routerConfigurationService: RouterConfigurationService,
    private val mapper: Mapper<Router, RouterDbModel>,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!synchronizedStorage.getSynchronizedStatus()) {
            val localRouterConfigList = runBlocking {
                routerDao.getAll().asFlow().first()
            }.map { mapper.mapDbModelToItem(it) }

            val postResult = runCatching {
                routerConfigurationService.setRoutersPosition(localRouterConfigList).execute()
            }.getOrNull()
            if (postResult != null && postResult.code() == 200) {
                synchronizedStorage.setSynchronizedStatus(
                    SharedPreferencesSynchronizedStorage.SYNCHRONIZED
                )
            } else if (postResult != null && postResult.code() == 400) {
                synchronizedStorage.setSynchronizedStatus(
                    SharedPreferencesSynchronizedStorage.SYNCHRONIZED
                )
            } else {
                return Response.Builder()
                    .code(600)
                    .protocol(Protocol.HTTP_2)
                    .request(chain.request())
                    .build()
            }
        }
        return chain.proceed(chain.request())
    }
}