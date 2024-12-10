package com.dicoding.sahabatgula.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dicoding.sahabatgula.data.local.entity.UserProfile
import com.dicoding.sahabatgula.data.local.room.UserProfileDatabase
import com.dicoding.sahabatgula.databinding.FragmentProfileBinding
import com.dicoding.sahabatgula.helper.SharedPreferencesHelper
import java.text.DecimalFormat

class ProfileFragment : Fragment() {

    private val profileViewModel by viewModels<ProfileViewModel> {
        ProfileViewModelFactory(UserProfileDatabase.getInstance(requireContext()).userProfileDao()) // Factory untuk ViewModel
    }
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ambil userId dari SharedPreferences
        val userId = SharedPreferencesHelper.getCurrentUserId(requireContext())

        // Jika userId ada, panggil ViewModel untuk mengambil data pengguna
        if (userId!!.isNotEmpty()) {
            // Ambil data pengguna berdasarkan userId
            profileViewModel.fetchUserProfile(userId)

            // Observe dataUser di ViewModel
            profileViewModel.dataUser.observe(viewLifecycleOwner) { dataUser ->
                dataUser?.let {
                    setUserData(it)
                }
            }
        }
    }

    private fun setUserData(dataUser: UserProfile) {
        var diabatesRisk: String = "diabetes rendah"
        var bmi: Float = 0.0F

        binding.apply {
            name.text = dataUser.name
            textDiabetes.text = diabatesRisk
            numTinggi.text = dataUser.tinggi.toString()
            numBerat.text = dataUser.berat.toString()

            // Hitung BMI
            bmi = dataUser.berat!!.toFloat() / ((dataUser.tinggi!!.toFloat() * dataUser.tinggi!!.toFloat()) * 0.0001F)
            val decimalFormat = DecimalFormat("#.##")
            val formattedBMI = decimalFormat.format(bmi)
            numBmi.text = formattedBMI.toString()

            // Tentukan kategori berat badan berdasarkan BMI
            when {
                bmi < 18.5 -> {
                    binding.text1BeratDiabetes.visibility = View.VISIBLE
                    binding.text2BeratDiabetes.visibility = View.GONE
                    binding.text3BeratDiabetes.visibility = View.GONE
                    binding.text4BeratDiabetes.visibility = View.GONE
                }
                bmi in 18.5..24.9 -> {
                    binding.text1BeratDiabetes.visibility = View.GONE
                    binding.text2BeratDiabetes.visibility = View.VISIBLE
                    binding.text3BeratDiabetes.visibility = View.GONE
                    binding.text4BeratDiabetes.visibility = View.GONE
                }
                bmi in 25.0..29.9 -> {
                    binding.text1BeratDiabetes.visibility = View.GONE
                    binding.text2BeratDiabetes.visibility = View.GONE
                    binding.text3BeratDiabetes.visibility = View.VISIBLE
                    binding.text4BeratDiabetes.visibility = View.GONE
                }
                else -> {
                    binding.text1BeratDiabetes.visibility = View.GONE
                    binding.text2BeratDiabetes.visibility = View.GONE
                    binding.text3BeratDiabetes.visibility = View.GONE
                    binding.text4BeratDiabetes.visibility = View.VISIBLE
                }
            }
        }
    }
}
