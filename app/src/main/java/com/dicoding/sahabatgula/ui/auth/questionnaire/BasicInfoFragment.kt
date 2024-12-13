package com.dicoding.sahabatgula.ui.auth.questionnaire

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.dicoding.sahabatgula.R
import com.dicoding.sahabatgula.data.local.entity.UserProfile
import com.dicoding.sahabatgula.databinding.FragmentBasicInfoBinding
import com.dicoding.sahabatgula.di.Injection
import com.dicoding.sahabatgula.ui.auth.RegisterFragment
import com.dicoding.sahabatgula.ui.auth.RegisterViewModel
import com.dicoding.sahabatgula.ui.auth.RegisterViewModelFactory
import com.google.android.material.button.MaterialButton

class BasicInfoFragment : Fragment() {

    private lateinit var binding: FragmentBasicInfoBinding
    private val registerViewModel: RegisterViewModel by activityViewModels {
        RegisterViewModelFactory(Injection.provideRepository(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBasicInfoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnMale = binding.male
        val btnFemale = binding.female
        val btnlingkarpinggang_1 = binding.lessThan90
        val btnlingkarpinggang_2 = binding.from90To102
        val btnlingkarpinggang_3 = binding.moreThan102

        val genderButtons = listOf(btnMale, btnFemale)
        val lingkarPinggangButtons = listOf(btnlingkarpinggang_1, btnlingkarpinggang_2, btnlingkarpinggang_3)

        // gender
        btnMale.setOnClickListener{
            selectButton(btnMale, genderButtons)
        }
        btnFemale.setOnClickListener{
            selectButton(btnFemale, genderButtons)
        }

        // lingkar pinggang
        btnlingkarpinggang_1.setOnClickListener{
            selectButton(btnlingkarpinggang_1, lingkarPinggangButtons)
        }
        btnlingkarpinggang_2.setOnClickListener{
            selectButton(btnlingkarpinggang_2, lingkarPinggangButtons)
        }
        btnlingkarpinggang_3.setOnClickListener{
            selectButton(btnlingkarpinggang_3, lingkarPinggangButtons)
        }

        val btnNextBasicInfo = binding.btnNextBasicInfo
        btnNextBasicInfo.setOnClickListener {

            val umur = binding.editInputUsia.text.toString().toInt()
            val berat = binding.editInputBerat.text.toString().toInt()
            val tinggi = binding.editInputTinggi.text.toString().toInt()
            val gender = if (btnMale.isSelected) "male" else "female"
            val lingkarPinggang = when {
                btnlingkarpinggang_1.isSelected -> "small"
                btnlingkarpinggang_2.isSelected -> "medium"
                btnlingkarpinggang_3.isSelected -> "large"
                else -> { Toast.makeText(requireContext(), "Pilih ukuran lingkar pinggang", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }


            val partialProfile = UserProfile(
//                id = SharedPreferencesHelper.getUserId(requireContext()),
                umur = umur,
                berat = berat,
                tinggi = tinggi,
                gender = gender,
                lingkarPinggang = lingkarPinggang
            )



            // menyimpan data ke view model
            registerViewModel.updateUserProfile(partialProfile)
            registerViewModel.saveToDatabase(partialProfile)

            // lanjut ke halaman selanjutnya
            moveToNextFragment()
        }
    }

    private fun moveToNextFragment() {
        val nextFragment = HealthInfoFragment()
        val oldFragment = RegisterFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.hide(oldFragment)
        transaction.add(R.id.activity_login, nextFragment)
        transaction.addToBackStack(null)
        transaction.commit()
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

}
