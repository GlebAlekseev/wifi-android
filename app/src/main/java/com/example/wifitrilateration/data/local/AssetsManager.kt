package com.example.wifitrilateration.data.local

import android.content.Context
import java.io.InputStream
import javax.inject.Inject


// Класс отвечает за получение локальный данных в папке Assets
class AssetsManager @Inject constructor(private val context: Context) {
    fun getStringFromAsset(fileName: String): String {
        val stream: InputStream = context.assets.open(fileName)
        val size: Int = stream.available()
        val buffer = ByteArray(size)
        stream.read(buffer)
        stream.close()
        return String(buffer, Charsets.UTF_8)
    }
}