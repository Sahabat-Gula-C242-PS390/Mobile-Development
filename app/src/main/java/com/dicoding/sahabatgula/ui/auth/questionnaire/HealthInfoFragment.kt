package com.dicoding.sahabatgula.ui.auth.questionnaire

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.dicoding.sahabatgula.R
import com.dicoding.sahabatgula.data.local.entity.UserProfile
import com.dicoding.sahabatgula.databinding.FragmentHealthInfoBinding
import com.dicoding.sahabatgula.di.Injection
import com.dicoding.sahabatgula.ui.auth.RegisterViewModel
import com.dicoding.sahabatgula.ui.auth.RegisterViewModelFactory
import com.google.android.material.button.MaterialButton

class HealthInfoFragment : Fragment() {

    private lateinit var binding: FragmentHealthInfoBinding
    private val registerViewModel: RegisterViewModel by activityViewModels {
        RegisterViewModelFactory(Injection.provideRepository(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        binding = FragmentHealthInfoBinding.inflate(layoutInflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnYesDarahTinggi = binding.yesDarahTinggi
        val btnNoDarahTinggi = binding.noDarahTinggi

        val btnYesGulaDarah = binding.yesGulaDarah
        val btnNoGulaDarah = binding.noGulaDarah

        val btnYesDiabetes = binding.yesDiabetes
        val btnNoDiabetes = binding.noDiabetes

        btnYesDarahTinggi.setOnClickListener{
            selectButton(btnYesDarahTinggi, btnNoDarahTinggi)
        }

        btnNoDarahTinggi.setOnClickListener{
            selectButton(btnNoDarahTinggi, btnYesDarahTinggi)
        }

        btnYesDiabetes.setOnClickListener{
            selectButton(btnYesDiabetes, btnNoDiabetes)
        }

        btnNoDiabetes.setOnClickListener{
            selectButton(btnNoDiabetes, btnYesDiabetes)
        }

        btnYesGulaDarah.setOnClickListener{
            selectButton(btnYesGulaDarah, btnNoGulaDarah)
        }

        btnNoGulaDarah.setOnClickListener{
            selectButton(btnNoGulaDarah, btnYesGulaDarah)
        }

        val btnNextHealthInfo = binding.btnNextHealthInfo
        btnNextHealthInfo.setOnClickListener {
            val diabetes = if (btnYesDiabetes.isSelected) 1 else 0
            val gulaDarah = if (btnYesGulaDarah.isSelected) 1 else 0
            val darahTinggi = if (btnYesDarahTinggi.isSelected) 1 else 0


            val partialProfile = UserProfile(
//                id = SharedPreferencesHelper.getUserId(requireContext()),
                riwayatDiabetes = diabetes,
                gulaDarahTinggi = gulaDarah,
                tekananDarahTinggi = darahTinggi
            )

            registerViewModel.updateUserProfile(partialProfile)
            registerViewModel.saveToDatabase(partialProfile)

            moveToNextFragment()
        }

    }

    private fun moveToNextFragment() {
        val nextFragment = LastInfoFragment()
        val oldFragment = BasicInfoFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.hide(oldFragment)
        transaction.add(R.id.activity_login, nextFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun selectButton(selectedButton: MaterialButton, unSelectedButton: MaterialButton) {
        selectedButton.isSelected = true
        selectedButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green_selected))
        unSelectedButton.isSelected = false
        unSelectedButton.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.white))
    }

}