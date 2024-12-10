package com.dicoding.sahabatgula.ui.questionnaire

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.dicoding.sahabatgula.R
import com.dicoding.sahabatgula.data.local.entity.UserProfile
import com.dicoding.sahabatgula.databinding.FragmentLastInfoBinding
import com.dicoding.sahabatgula.di.Injection
import com.dicoding.sahabatgula.ui.auth.RegisterViewModel
import com.dicoding.sahabatgula.ui.auth.RegisterViewModelFactory
import com.dicoding.sahabatgula.ui.profile.ProfileFragment
import com.google.android.material.button.MaterialButton

class LastInfoFragment : Fragment() {

    private lateinit var binding: FragmentLastInfoBinding
    private val registerViewModel: RegisterViewModel by activityViewModels {
        RegisterViewModelFactory(Injection.provideRepository(requireContext()))
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentLastInfoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnInactive = binding.inactive
        val btnLight = binding.light
        val btnModerate = binding.moderate
        val btnHeavy = binding.heavy
        val btnVeryHeavy = binding.veryHeavy

        val btnYesFruitVegetables = binding.yesFruitVegetables
        val btnNoFruitVegetables = binding.noFruitVegetables

        val dailyActivityButtons = listOf(btnInactive, btnLight, btnModerate, btnHeavy, btnVeryHeavy)
        val fruitVegetablesButtons = listOf(btnYesFruitVegetables, btnNoFruitVegetables)

        // daily activity
        btnInactive.setOnClickListener{
            selectButton(btnInactive, dailyActivityButtons)
        }
        btnLight.setOnClickListener{
            selectButton(btnLight, dailyActivityButtons)
        }
        btnModerate.setOnClickListener {
            selectButton(btnModerate, dailyActivityButtons)
        }
        btnHeavy.setOnClickListener {
            selectButton(btnHeavy, dailyActivityButtons)
        }
        btnVeryHeavy.setOnClickListener {
            selectButton(btnVeryHeavy, dailyActivityButtons)
        }

        // fruit and vegetables
        btnYesFruitVegetables.setOnClickListener {
            selectButton(btnYesFruitVegetables, fruitVegetablesButtons)
        }
        btnNoFruitVegetables.setOnClickListener {
            selectButton(btnNoFruitVegetables, fruitVegetablesButtons)
        }



        val btnLastInfo = binding.btnLastInfo
        btnLastInfo.setOnClickListener {
            val dailyActivity = when {
                btnInactive.isSelected -> "none"
                btnLight.isSelected -> "low"
                btnModerate.isSelected -> "medium"
                btnHeavy.isSelected -> "high"
                btnVeryHeavy.isSelected -> "extreme"
                else -> {  Toast.makeText(requireContext(), "Pilih aktivitas harian", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            val fruitVegetables = if (btnYesFruitVegetables.isSelected) 1 else 0

            val partialProfile = UserProfile(

                tingkatAktivitas = dailyActivity,
                konsumsiBuah = fruitVegetables
            )

            registerViewModel.updateUserProfile(partialProfile)

            val userProfile = registerViewModel.userProfile.value
            if (userProfile != null) {
                registerViewModel.saveToDatabase(userProfile)
                registerViewModel.registerUserProfileToRemote(userProfile) { response ->
                    if (response?.error == false && response.status == "success") {
                        Toast.makeText(context, "Data berhasil dikirim!", Toast.LENGTH_SHORT).show()
                        Log.d("ID_AT_ROOM", "Id User at Room database: ${userProfile.id}")
                    } else {
                        Toast.makeText(context, "Gagal mengirim data.", Toast.LENGTH_SHORT).show()
                    }
                }

            }
            moveToNextFragment()

        }


    }



    private fun selectButton(selectedButton: MaterialButton, buttons: List<MaterialButton>) {

        for (button in buttons) {
            if(button == selectedButton) {
                button.isSelected = true
                button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green_selected))
            } else {
                button.isSelected = false
                button.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.white))
            }
        }
    }

    private fun moveToNextFragment() {
        val nextFragment = ProfileFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.activity_login, nextFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }



}