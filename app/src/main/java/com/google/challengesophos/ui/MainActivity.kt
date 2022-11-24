package com.google.challengesophos.ui


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.challengesophos.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//Databinding initialized
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


}