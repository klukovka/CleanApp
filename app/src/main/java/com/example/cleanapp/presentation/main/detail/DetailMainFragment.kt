package com.example.cleanapp.presentation.main.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.cleanapp.R
import com.example.cleanapp.common.extension.gone
import com.example.cleanapp.common.extension.showGenericAlertDialog
import com.example.cleanapp.common.extension.showToast
import com.example.cleanapp.common.extension.visible
import com.example.cleanapp.data.products.remote.dto.ProductUpdateRequest
import com.example.cleanapp.data.register.remote.dto.RegisterResponse
import com.example.cleanapp.databinding.FragmentDetailMainBinding
import com.example.cleanapp.domain.common.utils.WrappedResponse
import com.example.cleanapp.domain.entity.ProductEntity
import com.example.cleanapp.presentation.main.home.HomeMainFragmentState
import com.example.cleanapp.presentation.main.home.HomeProductAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class DetailMainFragment : Fragment(R.layout.fragment_detail_main) {

    private var _binding: FragmentDetailMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel : DetailMainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentDetailMainBinding.bind(view)
        update()
        delete()
        observe()
        fetchProductById()
    }

    private fun fetchProductById(){
        val id = arguments?.getInt("product_id")?:0
        viewModel.getProductById(id.toString())
    }

    private fun observe(){
        observeState()
        observeProducts()
    }

    private fun observeState(){
        viewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> handleState(state) }
            .launchIn(lifecycleScope)
    }

    private fun observeProducts(){
        viewModel.product.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { product -> handleProduct(product) }
            .launchIn(lifecycleScope)
    }

    private fun handleProduct(product: ProductEntity?){
        product?.let {
            binding.productNameEditText.setText(it.name)
            binding.productPriceEditText.setText(it.price.toString())
        }
    }

    private fun handleState(state: DataMainFragmentState){
        when(state){
            is DataMainFragmentState.Init -> Unit
            is DataMainFragmentState.ShowToast -> requireActivity().showToast(state.message)
            is DataMainFragmentState.IsLoading -> handleLoadingState(state.isLoading)
            is DataMainFragmentState.SuccessDelete -> findNavController().navigate(R.id.action_detail_to_home)
            is DataMainFragmentState.SuccessUpdate -> findNavController().navigate(R.id.action_detail_to_home)
        }
    }

    private fun handleLoadingState(isLoading : Boolean){
        binding.updateButton.isEnabled = !isLoading
        binding.deleteButton.isEnabled = !isLoading
    }

    private fun update(){
        binding.updateButton.setOnClickListener{
            val name = binding.productNameEditText.text.toString().trim()
            val price = binding.productPriceEditText.text.toString().trim()
            val id = arguments?.getInt("product_id")?:0

            if (validate(name, price) && id!=null){
                viewModel.update(ProductUpdateRequest(name, price.toInt()), id.toString())
            }
        }
    }

    private fun delete(){
        binding.deleteButton.setOnClickListener {
            val id = arguments?.getInt("product_id")?:0
            if (id!=null){
                viewModel.delete(id.toString())
            }
        }
    }

    private fun setProductNameError(e: String? = null){
        binding.productNameInput.error = e
    }

    private fun setProductPriceError(e: String? = null){
        binding.productPriceInput.error = e
    }

    private fun resetAllError(){
        setProductNameError()
        setProductPriceError()
    }

    private fun validate(name: String?, price: String?) : Boolean{
        resetAllError()

        var isValid = true

        if (name?.isEmpty() == true){
            setProductNameError(getString(R.string.product_name_not_valid))
            isValid = false
        }

        if (price?.toIntOrNull() == null){
            setProductPriceError(getString(R.string.product_price_not_valid))

            isValid = false
        }

        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}