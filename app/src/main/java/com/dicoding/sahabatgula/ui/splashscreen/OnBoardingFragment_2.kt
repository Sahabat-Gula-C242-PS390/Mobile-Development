package com.dicoding.sahabatgula.ui.splashscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dicoding.sahabatgula.R
import com.dicoding.sahabatgula.databinding.FragmentOnBoarding1Binding
import com.dicoding.sahabatgula.databinding.FragmentOnBoarding2Binding
import com.dicoding.sahabatgula.ui.auth.RegisterFragment

class OnBoardingFragment_2 : Fragment() {
    private lateinit var binding: FragmentOnBoarding2Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnBoarding2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext3 .setOnClickListener {
            val nextFragment = OnBoardingFragment_3()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.activity_splash, nextFragment)
            transaction.commit()
        }

        binding.buttonSkip3.setOnClickListener{
            val nextFragment = RegisterFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.activity_splash, nextFragment)
            transaction.commit()
        }
    }
}