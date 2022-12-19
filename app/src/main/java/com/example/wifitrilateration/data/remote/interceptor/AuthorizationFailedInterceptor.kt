package com.example.wifitrilateration.data.remote.interceptor

import com.example.wifitrilateration.data.preferences.SharedPreferencesSynchronizedStorage
import com.example.wifitrilateration.data.preferences.SharedPreferencesUserStorage
import com.example.wifitrilateration.data.remote.AuthService
import com.example.wifitrilateration.data.remote.model.RefreshToken
import com.glebalekseevjk.yasmrhomework.data.local.dao.RouterDao
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthorizationFailedInterceptor @Inject constructor(
    private val userStorage: SharedPreferencesUserStorage,
    private val synchronizedStorage: SharedPreferencesSynchronizedStorage,
    private val authService: AuthService,
    private val routerDao: RouterDao,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequestTimestamp = System.currentTimeMillis()
        val originalResponse = chain.proceed(chain.request())

        // все неавторизованные запросы кидать в handleUnauthorizedResponse
        return originalResponse
            .takeIf { it.code != 401 }
            ?: handleUnauthorizedResponse(chain, originalResponse, originalRequestTimestamp)
    }

    private fun handleUnauthorizedResponse(
        chain: Interceptor.Chain,
        originalResponse: Response,
        requestTimestamp: Long
    ): Response {
        val latch = getLatch()
        return when {
            // Если включена защелка, выполняется refresh (атомарное поведение), надо ждать завершения операции
            latch != null && latch.count > 0 -> handleTokenIsUpdating(
                chain,
                latch,
                requestTimestamp
            )
                ?: originalResponse
            // Пока шел ответ со старым токеном, другой запрос уже рефрешнул токены
            tokenUpdateTime > requestTimestamp -> updateTokenAndProceedChain(chain)
            // Если защелки нет, если защелка = 0, если последнее время обновления просрочено
            else -> handleTokenNeedRefresh(chain) ?: originalResponse
        }
    }

    // Кидаю в ожидание все запросы, после начала обновления токенов
    // Ожидаю защелку (выполнение рефреша - лжидание ответа сервера) REQUEST_TIMEOUT
    private fun handleTokenIsUpdating(
        chain: Interceptor.Chain,
        latch: CountDownLatch,
        requestTimestamp: Long
    ): Response? {
        return if (latch.await(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            && tokenUpdateTime > requestTimestamp
        ) {
            updateTokenAndProceedChain(chain)
        } else {
            null
        }
    }

    // Обновление токенов
    // Повтор запроса с обновленным access токеном
    private fun handleTokenNeedRefresh(
        chain: Interceptor.Chain
    ): Response? {
        return if (refreshToken()) {
            updateTokenAndProceedChain(chain)
        } else {
            null
        }
    }

    // Повторный запрос с новым access токеном
    private fun updateTokenAndProceedChain(
        chain: Interceptor.Chain
    ): Response {
        val newRequest = updateOriginalCallWithNewToken(chain.request())
        return chain.proceed(newRequest)
    }

    // Вернет модифицированный заголовок с токеном авторизации
    private fun updateOriginalCallWithNewToken(request: Request): Request {
        return userStorage.getAccessToken()?.let { newAccessToken ->
            request
                .newBuilder()
                .header("Authorization", newAccessToken)
                .build()
        } ?: request
    }

    // Атомарное обновление токенов
    private fun refreshToken(): Boolean {
        initLatch()
        val lastRefreshToken = userStorage.getRefreshToken().orEmpty()
        val authResponse = runCatching {
            authService.refreshTokenPair(RefreshToken(lastRefreshToken)).execute()
        }.getOrNull()
        val tokenRefreshed = if (authResponse != null && authResponse.code() == 200) {
            val tokenPair = authResponse.body()?.data
            if (tokenPair != null) {
                userStorage.setTokenPair(tokenPair)
                true
            } else {
                false
            }
        } else {
            false
        }
        if (tokenRefreshed) {
            tokenUpdateTime = System.currentTimeMillis()
        } else {
            // Неверный refresh_token, выйти из аккаунта
            userStorage.clear()
            synchronizedStorage.setSynchronizedStatus(SharedPreferencesSynchronizedStorage.SYNCHRONIZED)
            routerDao.deleteAll()
        }
        getLatch()?.countDown()
        return tokenRefreshed
    }

    companion object {
        // Время ожидания других запросов, атомарного обновления рефреша
        private const val REQUEST_TIMEOUT = 30L

        // Не кешируется процессором, устанавливается значение последнего обновления
        // Другой поток будет получать только последний экземпляр
        @Volatile
        private var tokenUpdateTime: Long = 0L

        //  Защелка для осуществления атомарного выоплнения refresh
        private var countDownLatch: CountDownLatch? = null

        // Монитор устаналивается единый на все объекты данного класса, поэтому
        // может выполнять в один момент времени или initLatch, или getLatch
        @Synchronized
        fun initLatch() {
            countDownLatch = CountDownLatch(1)
        }

        @Synchronized
        fun getLatch() = countDownLatch
    }
}