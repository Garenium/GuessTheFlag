package com.gikezian.guesstheflag

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gikezian.guesstheflag.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val TAG: String = "MainActivity: INFO"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //initialize buttons
        val buttonAbout: Button = findViewById(R.id.aboutButton)
        val buttonStart: Button = findViewById(R.id.startButton)
        val buttonSettings: Button = findViewById(R.id.settingsButton)
        //initialize builder object
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val flagUrl = "https://flagcdn.com/w640/ua.png"
        Picasso.get().load(flagUrl).into(binding.imageView)

        //set up button listeners
        buttonAbout.setOnClickListener{onClick(buttonAbout)}
        buttonStart.setOnClickListener{onClick(buttonStart)}
        buttonSettings.setOnClickListener{onClick(buttonSettings)}


/*        builder
            .setTitle("Welcome!")
            .setMessage("Would you like this app to be playable online or offline?")
            .setPositiveButton("Yes") { dialog, which ->
                // Do something.
            }
            .setNegativeButton("No") { dialog, which ->
                // Do something else.
            }

        val promptOnlineOffline: AlertDialog = builder.create()
        promptOnlineOffline.show()*/
    }

    fun onClick(v: View?){
        when(v?.id) {
           R.id.startButton -> {
               val intent = Intent(this,QuizActivity::class.java)
               startActivity(intent)
               //Log.i(TAG,"startButton not implemented")
           }
           R.id.settingsButton -> {
               //val intent = Intent(this,SettingsActivity::class.java)
               //startActivity(intent)
               Log.i(TAG,"settingsButton not implemented")
           }
           R.id.aboutButton -> {
               val intent = Intent(this,AboutActivity::class.java)
               startActivity(intent)
           }
        }
    }

}