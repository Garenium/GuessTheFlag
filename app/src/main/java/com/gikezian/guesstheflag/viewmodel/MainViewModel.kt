package com.gikezian.guesstheflag.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gikezian.guesstheflag.APIFormat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainViewModel : ViewModel() {

    // Use viewModelScope for coroutine handling
    fun getFlagDataInViewModel(): LiveData<APIFormat> {
        val liveData = MutableLiveData<APIFormat>()

        viewModelScope.launch(Dispatchers.IO) {
            val url = URL("https://flagcdn.com/fr/codes.json")
            val connection = url.openConnection() as HttpsURLConnection
            if (connection.responseCode == 200) {
                val inputStream = connection.inputStream
                val inputStreamReader = InputStreamReader(inputStream, "UTF-8")

                // Read the entire JSON response as a String
                val rawJson = inputStreamReader.readText()
                inputStreamReader.close()
                inputStream.close()

                val flagCodes: Map<String, String> = Gson().fromJson(rawJson, object : TypeToken<Map<String, String>>() {}.type)
                Log.d("MainViewModel", "Parsed APIFormat object: $flagCodes")
                liveData.postValue(APIFormat(flagCodes))
            } else {
                Log.e("MainViewModel", "Failed request: ${connection.responseCode}")
                liveData.postValue(null)
            }
            Log.d("MainViewModel", "Response Code: ${connection.responseCode}")

        }
        return liveData
    }
}