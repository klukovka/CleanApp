package com.example.cleanapp.presentation.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cleanapp.R
import com.example.cleanapp.common.extension.isEmail
import com.example.cleanapp.data.login.remote.dto.LoginRequest
import com.example.cleanapp.databinding.ActivityLoginBinding
import com.example.cleanapp.databinding.ActivityRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        signUp()
    }

    private fun signUp() {
        binding.createAccountButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            val name = binding.nameEditText.text.toString().trim()
            if (validate(email, password, name)) {
             //TODO: Add callback
            }
        }
    }

    private fun setEmailError(e: String? = null) {
        binding.emailInput.error = e
    }

    private fun setPasswordError(e: String? = null) {
        binding.passwordInput.error = e
    }

    private fun setNameError(e: String? = null){
        binding.nameInput.error = e
    }

    private fun resetAllError() {
        setEmailError()
        setPasswordError()
        setNameError()
    }

    private fun validate(email: String?, password: String?, name: String?): Boolean {
        resetAllError()

        var isValid = false;

        if (email?.isEmail() != false) {
            isValid = true;
        } else {
            setEmailError(getString(R.string.error_email_not_valid))
        }

        if (password?.length ?:0 < 8) {
            setPasswordError(getString(R.string.error_password_not_valid))
        } else {
            isValid = true;
        }

        if (name?.isNotEmpty() != false){
            isValid = true;
        } else {
            setNameError(getString(R.string.error_name_not_valid))
        }

        return isValid;
    }
}