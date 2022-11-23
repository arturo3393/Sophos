package com.google.challengesophos.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import com.google.challengesophos.R
import com.google.challengesophos.ViewModel.GetDocsByIdViewModel
import com.google.challengesophos.ViewModel.GetDocsViewModel
import com.google.challengesophos.ViewModel.LoginViewModel


class MainActivity : AppCompatActivity() {
//This will only contian fragements

    //private val loginViewModel: LoginViewModel by viewModels()

    //private val getDocsViewModel:GetDocsViewModel by viewModels()

    private val getDocsByIdViewModel: GetDocsByIdViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//Put the view elements into variables
        val logInBtn: Button = findViewById(R.id.btnLogin)
        val emailIn: TextView = findViewById(R.id.etEmail)
        val passwordIn: TextView = findViewById(R.id.etPassword)
        val docSpecificId: TextView =findViewById(R.id.tvIdDocument)


//listens and saves the email and password from the login
     /*   logInBtn.setOnClickListener {

            val emailLogin = emailIn.text.toString().trim()
            val passwordLogin = passwordIn.text.toString().trim()
            loginViewModel.getLoginViewModel(emailLogin, passwordLogin)
        }*/


// Listens and brings the list of docs only with the email and pressing the btn

     /*   logInBtn.setOnClickListener {
            val emailLogin = emailIn.text.toString().trim()
            getDocsViewModel.getDocsViewModel(emailLogin)
        }*/


        logInBtn.setOnClickListener {
            val idDoc = docSpecificId.text.toString().trim()
            getDocsByIdViewModel.getDocsViewModel(idDoc)
        }



    }


}