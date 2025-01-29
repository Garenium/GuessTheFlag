package com.gikezian.guesstheflag.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.gikezian.guesstheflag.APIFormat
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import kotlin.coroutines.CoroutineContext

class MainViewModel : ViewModel() {

    suspend fun getFlagDataInViewModel(): APIFormat? {
        val defer = CoroutineScope(Dispatchers.IO).async {
            val url =
                URL("https://flagcdn.com/en/codes.json")
            val connection = url.openConnection() as HttpsURLConnection
            if (connection.responseCode == 200) {
                val inputSystem = connection.inputStream
                val inputStreamReader = InputStreamReader(inputSystem, "UTF-8")
                val request = Gson().fromJson(inputStreamReader, APIFormat::class.java)
                inputStreamReader.close()
                inputSystem.close()
                return@async request
            }
            else {
                return@async null
            }
        }
        return defer.await()
    }
}

