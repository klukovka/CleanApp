package com.example.cleanapp.presentation.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cleanapp.R
import com.example.cleanapp.common.extension.gone
import com.example.cleanapp.common.extension.showToast
import com.example.cleanapp.common.extension.visible
import com.example.cleanapp.databinding.FragmentHomeMainBinding
import com.example.cleanapp.domain.entity.ProductEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class HomeMainFragment : Fragment(R.layout.fragment_home_main) {
    private var _binding: FragmentHomeMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeMainViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHomeMainBinding.bind(view)
        setupRecyclerView()
        observe()
        fetchMyProducts()
    }

    private fun fetchMyProducts(){
        viewModel.fetchAllMyProducts()
    }

    private fun setupRecyclerView(){
        val mAdapter = HomeProductAdapter(mutableListOf())
        mAdapter.setOnItemTap(object : HomeProductAdapter.OnItemTap{
            override fun onTap(product: ProductEntity) {
                val bundle = bundleOf("product_id" to product.id)
                findNavController().navigate(R.id.action_home_to_detail, bundle)
            }
        })
        binding.productRecyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }
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
        viewModel.products.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { products -> handleProducts(products) }
            .launchIn(lifecycleScope)
    }

    private fun handleProducts(products: List<ProductEntity>){
        binding.productRecyclerView.adapter?.let { adapter ->
            if (adapter is HomeProductAdapter){
                adapter.updateList(products)
            }
        }
    }


    private fun handleState(state: HomeMainFragmentState){
        when(state){
            is HomeMainFragmentState.Init -> Unit
            is HomeMainFragmentState.ShowToast -> requireActivity().showToast(state.message)
            is HomeMainFragmentState.IsLoading -> handleLoadingState(state.isLoading)
        }
    }

    private fun handleLoadingState(isLoading : Boolean){
        if (isLoading){
            binding.loadingProgressIndicator.visible()
        } else {
            binding.loadingProgressIndicator.gone()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}