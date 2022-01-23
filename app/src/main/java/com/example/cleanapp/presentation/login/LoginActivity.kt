package com.example.cleanapp.presentation.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.cleanapp.R
import com.example.cleanapp.common.extension.isEmail
import com.example.cleanapp.common.extension.showGenericAlertDialog
import com.example.cleanapp.common.extension.showToast
import com.example.cleanapp.common.utils.SharedPref
import com.example.cleanapp.data.login.remote.dto.LoginRequest
import com.example.cleanapp.data.login.remote.dto.LoginResponse
import com.example.cleanapp.databinding.ActivityLoginBinding
import com.example.cleanapp.domain.common.utils.WrappedResponse
import com.example.cleanapp.domain.entity.LoginEntity
import com.example.cleanapp.presentation.main.MainActivity
import com.example.cleanapp.presentation.register.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import kotlin.math.log


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var prefs : SharedPref

    private val openRegisterActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
        if (result.resultCode == RESULT_OK){
            goToMainActivity()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        login()
        goToRegisterActivity()
        goToMainActivity()
        observe()
    }

    private fun goToRegisterActivity(){
        binding.registerButton.setOnClickListener{
            openRegisterActivity.launch(Intent(this@LoginActivity,
                RegisterActivity::class.java))
        }
    }

    private fun observe(){
        viewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> handleState(state) }
            .launchIn(lifecycleScope)
    }

    private fun handleState(state : LoginActivityState){
        when(state){
            is LoginActivityState.ShowToast -> showToast(state.message)
            is LoginActivityState.IsLoading -> handleLoadingState(state.isLoading)
            is LoginActivityState.Init -> Unit
            is LoginActivityState.ErrorLogin -> handleErrorState(state.loginResponse)
            is LoginActivityState.SuccessLogin -> handleSuccessState(state.loginEntity)
        }
    }

    private fun handleLoadingState(isLoading : Boolean){
        binding.loginButton.isEnabled = !isLoading
        binding.registerButton.isEnabled = !isLoading
        binding.loadingProgressBar.isIndeterminate = isLoading

        if (!isLoading){
            binding.loadingProgressBar.progress = 0
        }
    }

    private fun handleSuccessState(loginEntity: LoginEntity){
        prefs.saveToken(loginEntity.token)
        goToMainActivity()
    }

    private fun goToMainActivity(){
        if (prefs.getToken().isNotEmpty()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun handleErrorState(rawResponse : WrappedResponse<LoginResponse>){
        showGenericAlertDialog(rawResponse.message)
    }

    private fun login() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            if (validate(email, password)) {
                val loginRequest = LoginRequest(email, password)
                viewModel.login(loginRequest)
            }
        }
    }

    private fun setEmailError(e: String? = null) {
        binding.emailInput.error = e
    }

    private fun setPasswordError(e: String? = null) {
        binding.passwordInput.error = e
    }

    private fun resetAllError() {
        setEmailError()
        setPasswordError()
    }

    private fun validate(email: String?, password: String?): Boolean {
        resetAllError()

        var isValid = true;

        if (email?.isEmail() == false) {
            setEmailError(getString(R.string.error_email_not_valid))
            isValid = false
        }

        if (password?.length ?:0 < 8) {
            setPasswordError(getString(R.string.error_password_not_valid))
            isValid = false
        }

        return isValid;
    }


}