package com.dicoding.sahabatgula.ui.navigation_ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dicoding.sahabatgula.R
import com.dicoding.sahabatgula.databinding.FragmentHomeBinding
import com.dicoding.sahabatgula.ui.profile.ProfileFragment

class HomeFragment : Fragment() {


    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appCompatImageView.setOnClickListener {
            val nextFragment = ProfileFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, nextFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

}