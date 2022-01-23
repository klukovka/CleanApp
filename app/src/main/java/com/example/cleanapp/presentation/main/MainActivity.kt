package com.example.cleanapp.presentation.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cleanapp.R
import com.example.cleanapp.common.utils.SharedPref
import com.example.cleanapp.databinding.ActivityLoginBinding
import com.example.cleanapp.databinding.ActivityMainBinding
import com.example.cleanapp.presentation.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var prefs : SharedPref

    override fun onStart() {
        super.onStart()
        checkIsLogin()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun checkIsLogin(){
        if (prefs.getToken().isNotEmpty()){
            goToLoginActivity()
        }
    }

    private fun goToLoginActivity(){
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}