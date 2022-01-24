package com.example.cleanapp.presentation.main.create

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.cleanapp.R
import com.example.cleanapp.common.extension.showToast
import com.example.cleanapp.data.products.remote.dto.ProductCreateRequest
import com.example.cleanapp.data.products.remote.dto.ProductUpdateRequest
import com.example.cleanapp.databinding.FragmentCreateProductBinding
import com.example.cleanapp.databinding.FragmentDetailMainBinding
import com.example.cleanapp.presentation.main.detail.DataMainFragmentState
import com.example.cleanapp.presentation.main.detail.DetailMainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class CreateProductFragment : Fragment(R.layout.fragment_create_product)  {

    private var _binding: FragmentCreateProductBinding? = null
    private val binding get() = _binding!!

    private val viewModel : CreateProductViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentCreateProductBinding.bind(view)
        observe()
        create()
    }

    private fun observe(){
        viewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> handleState(state) }
            .launchIn(lifecycleScope)
    }

    private fun handleState(state: CreateProductFragmentState){
        when(state){
            is CreateProductFragmentState.Init -> Unit
            is CreateProductFragmentState.ShowToast -> requireActivity().showToast(state.message)
            is CreateProductFragmentState.IsLoading -> handleLoadingState(state.isLoading)
            is CreateProductFragmentState.Success -> findNavController().navigate(R.id.action_create_to_home)
        }
    }

    private fun create(){
        binding.createButton.setOnClickListener {
            val name = binding.productNameEditText.text.toString().trim()
            val price = binding.productPriceEditText.text.toString().trim()

            if (validate(name, price)){
                viewModel.create(ProductCreateRequest(name, price.toInt()))
            }
        }
    }


    private fun handleLoadingState(isLoading : Boolean){
        binding.createButton.isEnabled = !isLoading

        binding.loadingProgressBar.isIndeterminate = isLoading

        if (!isLoading){
            binding.loadingProgressBar.progress = 0
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