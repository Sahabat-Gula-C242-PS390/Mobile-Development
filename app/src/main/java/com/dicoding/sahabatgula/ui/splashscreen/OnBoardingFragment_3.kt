package com.dicoding.sahabatgula.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dicoding.sahabatgula.R
import com.dicoding.sahabatgula.databinding.FragmentOnBoarding2Binding
import com.dicoding.sahabatgula.databinding.FragmentOnBoarding3Binding
import com.dicoding.sahabatgula.ui.auth.LoginActivity
import com.dicoding.sahabatgula.ui.auth.RegisterFragment
import com.dicoding.sahabatgula.ui.profile.ProfileActivity

class OnBoardingFragment_3 : Fragment() {

    private lateinit var binding: FragmentOnBoarding3Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnBoarding3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnJoin3.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
    }
}