package com.dicoding.sahabatgula.ui.splashscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.dicoding.sahabatgula.R
import com.dicoding.sahabatgula.databinding.ActivitySplashBinding
import com.dicoding.sahabatgula.databinding.FragmentOnBoarding1Binding
import com.dicoding.sahabatgula.ui.auth.LoginActivity
import com.dicoding.sahabatgula.ui.auth.RegisterFragment
import com.dicoding.sahabatgula.ui.auth.questionnaire.BasicInfoFragment
import com.dicoding.sahabatgula.ui.auth.questionnaire.LastInfoFragment

class OnBoardingFragment_1 : Fragment() {

    private lateinit var binding: FragmentOnBoarding1Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnBoarding1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext1.setOnClickListener {
            val nextFragment = OnBoardingFragment_2()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.activity_splash, nextFragment)
            transaction.commit()
        }

        binding.buttonSkip1.setOnClickListener{
            val nextFragment = RegisterFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.activity_splash, nextFragment)
            transaction.commit()
        }
    }

}