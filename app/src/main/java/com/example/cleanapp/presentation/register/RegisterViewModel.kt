package com.example.cleanapp.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanapp.data.register.remote.dto.RegisterRequest
import com.example.cleanapp.data.register.remote.dto.RegisterResponse
import com.example.cleanapp.domain.common.base.BaseResult
import com.example.cleanapp.domain.common.utils.WrappedResponse
import com.example.cleanapp.domain.entity.RegisterEntity
import com.example.cleanapp.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val registerUseCase: RegisterUseCase): ViewModel(){
    private val _state = MutableStateFlow<RegisterActivityState>(RegisterActivityState.Init)

    val state : StateFlow<RegisterActivityState> get() = _state

    private fun setLoading(){
        _state.value = RegisterActivityState.IsLoading(true)
    }

    private fun hideLoading(){
        _state.value = RegisterActivityState.IsLoading(false)
    }

    private fun showToast(message: String){
        _state.value = RegisterActivityState.ShowToast(message)
    }

    private fun successRegister(registerEntity: RegisterEntity){
        _state.value = RegisterActivityState.SuccessRegister(registerEntity)
    }

    private fun errorRegister(response: WrappedResponse<RegisterResponse>){
        _state.value = RegisterActivityState.ErrorRegister(response)
    }

    private fun init(){
        _state.value = RegisterActivityState.Init
    }

    fun register(registerRequest: RegisterRequest){
        viewModelScope.launch {
            registerUseCase.invoke(registerRequest)
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
                        is BaseResult.Success -> successRegister(result.data)
                        is BaseResult.Error -> {
                            errorRegister(result.rawResponse)
                            init()
                        }
                    }
                }
        }
    }
}

sealed class RegisterActivityState {
    object Init : RegisterActivityState()
    data class IsLoading(val isLoading: Boolean) : RegisterActivityState()
    data class ShowToast(val message: String) : RegisterActivityState()
    data class SuccessRegister(val registerEntity: RegisterEntity) : RegisterActivityState()
    data class ErrorRegister(val loginResponse: WrappedResponse<RegisterResponse>) : RegisterActivityState()
}