package com.example.cleanapp.presentation.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.cleanapp.R
import com.example.cleanapp.databinding.FragmentHomeMainBinding

class HomeMainFragment : Fragment(R.layout.fragment_home_main) {
    private var _binding: FragmentHomeMainBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHomeMainBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}