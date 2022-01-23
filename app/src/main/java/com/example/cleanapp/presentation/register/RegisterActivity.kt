package com.example.cleanapp.presentation.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.example.cleanapp.data.register.remote.dto.RegisterRequest
import com.example.cleanapp.data.register.remote.dto.RegisterResponse
import com.example.cleanapp.databinding.ActivityLoginBinding
import com.example.cleanapp.databinding.ActivityRegisterBinding
import com.example.cleanapp.domain.common.utils.WrappedResponse
import com.example.cleanapp.domain.entity.LoginEntity
import com.example.cleanapp.domain.entity.RegisterEntity
import com.example.cleanapp.presentation.login.LoginActivityState
import com.example.cleanapp.presentation.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    @Inject
    lateinit var prefs : SharedPref


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        signUp()
        observe()
        goBack()
    }

    private fun goBack(){
        binding.backButton.setOnClickListener { finish() }
    }

    private fun observe(){
        viewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> handleState(state) }
            .launchIn(lifecycleScope)
    }

    private fun handleState(state : RegisterActivityState){
        when(state){
            is RegisterActivityState.ShowToast -> showToast(state.message)
            is RegisterActivityState.IsLoading -> handleLoadingState(state.isLoading)
            is RegisterActivityState.Init -> Unit
            is RegisterActivityState.ErrorRegister -> handleErrorState(state.registerResponse)
            is RegisterActivityState.SuccessRegister -> handleSuccessState(state.registerEntity)
        }
    }

    private fun handleLoadingState(isLoading : Boolean){
        binding.createAccountButton.isEnabled = !isLoading
        binding.backButton.isEnabled = !isLoading
        binding.loadingProgressBar.isIndeterminate = isLoading

        if (!isLoading){
            binding.loadingProgressBar.progress = 0
        }
    }

    private fun handleSuccessState(registerEntity: RegisterEntity){
        prefs.saveToken(registerEntity.token)
        setResult(RESULT_OK)
        finish()
    }

    private fun handleErrorState(rawResponse : WrappedResponse<RegisterResponse>){
        showGenericAlertDialog(rawResponse.message)
    }

    private fun signUp() {
        binding.createAccountButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            val name = binding.nameEditText.text.toString().trim()
            if (validate(email, password, name)) {
                val registerRequest = RegisterRequest(email, name, password)
                viewModel.register(registerRequest)
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

        var isValid = true;

        if (email?.isEmail() == false) {
            setEmailError(getString(R.string.error_email_not_valid))
            isValid = false
        }

        if (password?.length ?:0 < 8) {
            setPasswordError(getString(R.string.error_password_not_valid))
            isValid = false
        }

        if (name?.isNotEmpty() == false){
            setNameError(getString(R.string.error_name_not_valid))
            isValid = false
        }

        return isValid;
    }
}