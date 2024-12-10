package com.dicoding.sahabatgula.ui.navigation_ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dicoding.sahabatgula.R
import com.dicoding.sahabatgula.data.local.entity.UserProfile
import com.dicoding.sahabatgula.databinding.FragmentHomeBinding
import com.dicoding.sahabatgula.di.Injection
import com.dicoding.sahabatgula.helper.SharedPreferencesHelper
import com.dicoding.sahabatgula.ui.profile.ProfileActivity

@Suppress("DEPRECATION")
class HomeFragment : Fragment() {


    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel by viewModels<HomeViewModel> {
        HomeViewModelFactory(Injection.provideRepository(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = SharedPreferencesHelper.getCurrentUserId(requireContext())


        getDataUser(userId)

        // go to profile activity
        binding.appCompatImageView.setOnClickListener {
            Intent(requireContext(), ProfileActivity::class.java).also {
                startActivity(it)
                requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        }
    }

    private fun observeViewModel() {
        homeViewModel.dataUser.observe(viewLifecycleOwner) { dataUser ->
            dataUser?.let {
                setUserData(it)
            }
        }
    }

    private fun setUserData(dataUser: UserProfile) {

//        binding.apply{
//            name.text = dataUser.name
//            karboConsume.text = dataUser.karbohidratHarian.toString()
//            kaloriHarianJumlah.text = kaloriHarian.toString()
//            proteinConsume.text = dataUser.proteinHarian.toString()
//            lemakConsume.text = dataUser.lemakHarian.toString()
//            gulaConsume.text = dataUser.gulaHarian.toString()
//            progressIndicatoKalori.setProgress(percentage, true)
//        }

        val dummyKarbo: Int = 65
        val dummyProtein: Int = 55
        val dummyLemak: Int = 70

//        val kaloriHarian = setKaloriHarian(dataUser)
//        val percentage:Int = (kaloriHarian/100)*2645
        val dummyKalori = dummyKarbo*4 + dummyProtein*4 + dummyLemak*9
        val dummyPercentage:Int = (dummyKalori.div(2645)).times(100)

        binding.apply{
            name.text = dataUser.name
            karboConsume.text = dummyKarbo.toString()
            kaloriHarianJumlah.text = dummyKalori.toString()
            proteinConsume.text = dummyProtein.toString()
            lemakConsume.text = dummyLemak.toString()
            gulaConsume.text = dataUser.gulaHarian.toString()
            progressIndicatoKalori.isIndeterminate = false
//            progressIndicatoKalori.progress = dummyPercentage
            progressIndicatoKalori.post {
                progressIndicatoKalori.progress = dummyPercentage
            }
        }



        val riskPoint = setDiabetesRisk(dataUser)
        Log.d("RISK_POINT", "Risk Point: $riskPoint")
        val circleGrren1 = binding.circle1
        val circleGrren2 = binding.circle2
        val circleGrren3 = binding.circle3
        val diabetesRisk = binding.diabetesRisk
        val textDiabetesRisk = binding.textDiabetesRisk

        if (riskPoint != null) {
            when {
                riskPoint <= 12 -> {
                    circleGrren1.background = ContextCompat.getDrawable(requireContext(), R.drawable.circle_yellow)
                    circleGrren2.background = ContextCompat.getDrawable(requireContext(), R.drawable.circle_yellow)
                    circleGrren3.background = ContextCompat.getDrawable(requireContext(), R.drawable.circle_yellow)
                    diabetesRisk.setText("Risiko Diabetes Sedang")
                    textDiabetesRisk.setText("Mengurangi konsumsi gula dan makanan olahan akan membantu menurunkan risiko.")
                }
                riskPoint >= 14 -> {
                    circleGrren1.background = ContextCompat.getDrawable(requireContext(), R.drawable.circle_red)
                    circleGrren2.background = ContextCompat.getDrawable(requireContext(), R.drawable.circle_red)
                    circleGrren3.background = ContextCompat.getDrawable(requireContext(), R.drawable.circle_red)
                    diabetesRisk.setText("Risiko Diabetes Tinggi")
                    textDiabetesRisk.setText("Segera konsultasikan dengan profesional kesehatan untuk pemeriksaan lebih lanjut.")
                }
                else -> {
                    circleGrren1.background = ContextCompat.getDrawable(requireContext(), R.drawable.circle_green)
                    circleGrren2.background = ContextCompat.getDrawable(requireContext(), R.drawable.circle_green)
                    circleGrren3.background = ContextCompat.getDrawable(requireContext(), R.drawable.circle_green)
                    diabetesRisk.setText("Risiko Diabetes Rendah")
                    textDiabetesRisk.setText("Pertahankan pola makan seimbang dan aktivitas fisik untuk menjaga kesehatan Anda.")
                }
            }
        }
    }

    private fun getDataUser(userId: String?) {
        if(!userId.isNullOrEmpty()) {
            homeViewModel.fetchUserData(userId)
            observeViewModel()
        } else {
            // add logic later
        }
    }

    private fun setDiabetesRisk(dataUser: UserProfile): Int{
        var riskPoint: Int = 0

        // usia
        riskPoint += when {
            dataUser.umur in 35..44 -> 2
            dataUser.umur in 45..54 -> 3
            dataUser.umur!! >= 55 -> 4
            else -> 0
        }

        // BMI
        val bMI = dataUser.berat!!.toFloat() / ((dataUser.tinggi!!.toFloat() * dataUser.tinggi!!.toFloat()) * 0.0001F)
        riskPoint += when {
            bMI in 25.0..30.0 -> 1
            bMI > 30.0 -> 2
            else -> 0
        }

        // Lingkar Pinggang
        riskPoint += when {
            dataUser.lingkarPinggang == "small" -> 0
            dataUser.lingkarPinggang == "medium" -> 1
            dataUser.lingkarPinggang == "large" -> 2
            else -> 0
        }

        // Aktivitas Harian
        riskPoint += when {
            dataUser.tingkatAktivitas == "none" -> 2
            else -> 0
        }

        // Konsumsi Buah dan Sayur
        riskPoint += when {
            dataUser.konsumsiBuah == 0 -> 1
            else -> 0
        }

        // Tekanan Darah Tinggi
        riskPoint += when {
            dataUser.tekananDarahTinggi == 1 -> 1
            else -> 0
        }

        // Gula Darah Tinggi
        riskPoint += when {
            dataUser.gulaDarahTinggi == 1 -> 1
            else -> 0
        }

        // Riwayat Diabetes Keluarga
        riskPoint += when {
            dataUser.riwayatDiabetes == 1 -> 1
            else -> 0
        }

        return riskPoint
    }

    private fun setKaloriHarian(dataUser: UserProfile): Int {
        var kaloriHarian: Int = 0
        kaloriHarian = (dataUser.proteinHarian*4) + (dataUser.karbohidratHarian*4) + (dataUser.lemakHarian*9)
        return kaloriHarian
    }
}