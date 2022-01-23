package com.example.cleanapp.presentation.main.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.cleanapp.R
import com.example.cleanapp.databinding.FragmentDetailMainBinding


class DetailMainFragment : Fragment(R.layout.fragment_detail_main) {

    private var _binding: FragmentDetailMainBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentDetailMainBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}