package com.example.cleanapp.presentation.main.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanapp.data.products.remote.dto.ProductUpdateRequest
import com.example.cleanapp.domain.common.base.BaseResult
import com.example.cleanapp.domain.entity.ProductEntity
import com.example.cleanapp.domain.usecase.DeleteProductUseCase
import com.example.cleanapp.domain.usecase.GetProductUseCase
import com.example.cleanapp.domain.usecase.UpdateProductUseCase
import com.example.cleanapp.presentation.main.home.HomeMainFragmentState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailMainViewModel @Inject constructor(
    private val updateProductUseCase: UpdateProductUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val getProductUseCase: GetProductUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow<DataMainFragmentState>(DataMainFragmentState.Init)
    val state : StateFlow<DataMainFragmentState> get() = _state

    private val _product = MutableStateFlow<ProductEntity?>(null)
    val product : StateFlow<ProductEntity?> get() = _product

    private fun setLoading(){
        _state.value = DataMainFragmentState.IsLoading(true)
    }

    private fun hideLoading(){
        _state.value = DataMainFragmentState.IsLoading(false)
    }

    private fun showToast(message: String){
        _state.value = DataMainFragmentState.ShowToast(message)
    }

    fun getProductById(id : String){
        viewModelScope.launch {
            getProductUseCase.invoke(id)
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
                            _product.value = result.data
                        }
                        is BaseResult.Error -> {
                            showToast(result.rawResponse.message)
                        }
                    }
                }
        }
    }

    fun update(productUpdateRequest: ProductUpdateRequest, id: String){
        viewModelScope.launch {
            updateProductUseCase.invoke(productUpdateRequest, id)
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
                            _state.value = DataMainFragmentState.SuccessUpdate
                        }
                        is BaseResult.Error -> {
                            showToast(result.rawResponse.message)
                        }
                    }
                }
        }
    }

    fun delete(id: String){
        viewModelScope.launch {
            deleteProductUseCase.invoke( id)
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
                            _state.value = DataMainFragmentState.SuccessDelete
                        }
                        is BaseResult.Error -> {
                            showToast(result.rawResponse.message)
                        }
                    }
                }
        }
    }
}

sealed class DataMainFragmentState{
    object Init : DataMainFragmentState()
    data class IsLoading(val isLoading: Boolean) : DataMainFragmentState()
    data class ShowToast(val message: String) : DataMainFragmentState()
    object SuccessDelete : DataMainFragmentState()
    object SuccessUpdate : DataMainFragmentState()

}