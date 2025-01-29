package com.gikezian.guesstheflag

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.gikezian.guesstheflag.databinding.ActivityQuizBinding
import com.gikezian.guesstheflag.viewmodel.MainViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding
    private lateinit var viewModel: MainViewModel
    private val TAG = "QuizActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActivityView()

        // Initialize the ViewModel
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // Fetch flag data from the ViewModel
        getFlagDataFromViewModel()
        //getFlagDataFromJSON()
    }


    private fun getFlagDataFromViewModel() {
        CoroutineScope(Dispatchers.Main).launch{
            val request = viewModel.getFlagDataInViewModel()
            Log.d(TAG, "request is $request")
            if (request != null) {
                Log.d(TAG, "request.flagCodes is ${request.flagCodes}")
            }
            if(request?.flagCodes != null){
                val flagCodes = request.flagCodes
                val randomCountryCode = request.flagCodes.keys.random()
                val flagUrl = "https://flagcdn.com/w2560/${randomCountryCode}.png"
                // Load the image using Picasso
                Picasso.get().load(flagUrl).into(binding.flagPicture)
                binding.textView2.text = flagCodes[randomCountryCode].toString()
            }
        }
    }

    private fun getFlagDataFromJSON() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // Use IO dispatcher for reading the file and parsing JSON
                val jsonString = withContext(Dispatchers.IO) {
                    assets.open("flagcodes_countries.json").bufferedReader().use {
                        it.readText()
                    }
                }

                // Parse the JSON string into a Map
                val gson = Gson()
                val mapType = object : TypeToken<Map<String, Any>>() {}.type
                val flagCodes: Map<String, Any> = gson.fromJson(jsonString, mapType)
                Log.d(TAG, "Parsed Map: $flagCodes")

                if (flagCodes.isNotEmpty()) {
                    val randomCountryCode = flagCodes.keys.random()
                    Log.d(TAG, "Parsed randomCountryCode: $randomCountryCode")
                    //TODO: Change png to svg
                    val flagUrl = "https://flagcdn.com/w2560/${randomCountryCode}.png"

                    // Load the image using Picasso
                    Picasso.get().load(flagUrl).into(binding.flagPicture)
                    binding.textView2.text = flagCodes[randomCountryCode].toString()
                } else {
                    Log.e(TAG, "Flag data is null or empty")
                    // Display a placeholder image if flag data is empty or null
                    binding.flagPicture.setImageResource(R.drawable.placeholder_flag)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching flag data: ${e.message}")
                // Handle error gracefully, display placeholder image
                binding.flagPicture.setImageResource(R.drawable.placeholder_flag)
            }
        }
    }

    private fun setupActivityView() {
        enableEdgeToEdge()
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
