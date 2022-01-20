package com.example.cleanapp.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanapp.data.login.remote.dto.LoginRequest
import com.example.cleanapp.data.login.remote.dto.LoginResponse
import com.example.cleanapp.domain.common.base.BaseResult
import com.example.cleanapp.domain.common.utils.WrappedResponse
import com.example.cleanapp.domain.entity.LoginEntity
import com.example.cleanapp.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel(){
    private val _state = MutableStateFlow<LoginActivityState>(LoginActivityState.Init)

    val state : StateFlow<LoginActivityState> get() = _state

    private fun setLoading(){
        _state.value = LoginActivityState.IsLoading(true)
    }

    private fun hideLoading(){
        _state.value = LoginActivityState.IsLoading(false)
    }

    private fun showToast(message: String){
        _state.value = LoginActivityState.ShowToast(message)
    }

    private fun successLogin(loginEntity: LoginEntity){
        _state.value = LoginActivityState.SuccessLogin(loginEntity)
    }

    private fun errorLogin(loginResponse: WrappedResponse<LoginResponse>){
        _state.value = LoginActivityState.ErrorLogin(loginResponse)
    }

    fun login(loginRequest: LoginRequest){
        viewModelScope.launch {
            loginUseCase.invoke(loginRequest)
                .onStart {
                    setLoading()
                }
                .catch { exception ->
                    hideLoading()
                    showToast(exception.stackTraceToString())
                }
                .collect { result ->
                    hideLoading()
                    when(result){
                        is BaseResult.Success -> successLogin(result.data)
                        is BaseResult.Error -> errorLogin(result.rawResponse)
                    }
                }
        }
    }
}

sealed class LoginActivityState {
    object Init : LoginActivityState()
    data class IsLoading(val isLoading: Boolean) : LoginActivityState()
    data class ShowToast(val message: String) : LoginActivityState()
    data class SuccessLogin(val loginEntity: LoginEntity) : LoginActivityState()
    data class ErrorLogin(val loginResponse: WrappedResponse<LoginResponse>) : LoginActivityState()

}