package com.example.cleanapp.presentation.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanapp.domain.common.base.BaseResult
import com.example.cleanapp.domain.entity.ProductEntity
import com.example.cleanapp.domain.usecase.GetAllProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeMainViewModel @Inject constructor(private val getAllProductsUseCase: GetAllProductsUseCase) : ViewModel(){
    private val _state = MutableStateFlow<HomeMainFragmentState>(HomeMainFragmentState.Init)
    val state : StateFlow<HomeMainFragmentState> get() = _state

    private val _products = MutableStateFlow<List<ProductEntity>>(mutableListOf())
    val products : StateFlow<List<ProductEntity>> get() = _products


    private fun setLoading(){
        _state.value = HomeMainFragmentState.IsLoading(true)
    }

    private fun hideLoading(){
        _state.value = HomeMainFragmentState.IsLoading(false)
    }

    private fun showToast(message: String){
        _state.value = HomeMainFragmentState.ShowToast(message)
    }

    fun fetchAllMyProducts(){
        viewModelScope.launch {
            getAllProductsUseCase.invoke()
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
                            _products.value = result.data
                        }
                        is BaseResult.Error -> {
                            showToast(result.rawResponse.message)
                        }
                    }
                }
        }
    }
}

sealed class HomeMainFragmentState {
    object Init : HomeMainFragmentState()
    data class IsLoading(val isLoading: Boolean) : HomeMainFragmentState()
    data class ShowToast(val message: String) : HomeMainFragmentState()
}