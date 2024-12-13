package com.dicoding.sahabatgula.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dicoding.sahabatgula.R
import com.dicoding.sahabatgula.databinding.FragmentUpgradeBinding


class UpgradeFragment : Fragment() {

    private lateinit var binding: FragmentUpgradeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentUpgradeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnUpgrade.setOnClickListener {
            Toast.makeText(requireContext(), "Dalam pengembangan", Toast.LENGTH_LONG).show()
        }
    }


}