package com.google.challengesophos.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import com.google.challengesophos.R
import com.google.challengesophos.ViewModel.LoginViewModel


class MainActivity : AppCompatActivity() {
//This will only contian fragements

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//Put the view elements into variables
        val logInBtn: Button = findViewById(R.id.btnLogin)
        val emailIn: TextView = findViewById(R.id.etEmail)
        val passwordIn: TextView = findViewById(R.id.etPassword)


//listens and saves the
        logInBtn.setOnClickListener {

            val emailLogin = emailIn.text.toString().trim()
            val passwordLogin = passwordIn.text.toString().trim()
            loginViewModel.getLoginViewModel(emailLogin, passwordLogin)
        }





    }


}