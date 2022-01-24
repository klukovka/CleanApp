package com.example.cleanapp.presentation.main.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanapp.data.products.remote.dto.ProductCreateRequest
import com.example.cleanapp.domain.common.base.BaseResult
import com.example.cleanapp.domain.usecase.CreateProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CreateProductViewModel @Inject constructor(
    private val createProductUseCase: CreateProductUseCase
) : ViewModel(){
    private val _state = MutableStateFlow<CreateProductFragmentState>(CreateProductFragmentState.Init)
    val state : StateFlow<CreateProductFragmentState> get() = _state

    private fun setLoading(){
        _state.value = CreateProductFragmentState.IsLoading(true)
    }

    private fun hideLoading(){
        _state.value = CreateProductFragmentState.IsLoading(false)
    }

    private fun showToast(message: String){
        _state.value = CreateProductFragmentState.ShowToast(message)
    }

    fun create(productCreateRequest: ProductCreateRequest){
        viewModelScope.launch {
            createProductUseCase.invoke(productCreateRequest)
                .onStart {
                    setLoading()
                }
                .catch { exception ->
                    hideLoading()
                    showToast(exception.stackTraceToString())
                }.collect{
                        result ->
                    hideLoading()
                    when(result){
                        is BaseResult.Success -> {
                            _state.value = CreateProductFragmentState.Success
                        }
                        is BaseResult.Error -> {
                            showToast(result.rawResponse.message)
                        }
                    }
                }
        }
    }
}

sealed class CreateProductFragmentState{
    object Init : CreateProductFragmentState()
    data class IsLoading(val isLoading: Boolean) : CreateProductFragmentState()
    data class ShowToast(val message: String) : CreateProductFragmentState()
    object Success : CreateProductFragmentState()
}