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
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.sahabatgula.R
import com.dicoding.sahabatgula.data.local.entity.UserProfile
import com.dicoding.sahabatgula.databinding.FragmentHomeBinding
import com.dicoding.sahabatgula.di.Injection
import com.dicoding.sahabatgula.helper.SharedPreferencesHelper
import com.dicoding.sahabatgula.ui.profile.ProfileActivity

@Suppress("DEPRECATION")
class HomeFragment : Fragment() {


    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: CatatanAdapter
    private val homeViewModel by viewModels<HomeViewModel> {
        HomeViewModelFactory(Injection.provideRepository(requireContext()))
    }
    private var totalKalori: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        // Cek apakah ada data di SharedPreferences
        val scanDataList = SharedPreferencesHelper.getScanData(requireContext())

        // Set RecyclerView
        binding.rvCatatanKonsumsi.layoutManager = LinearLayoutManager(context)



        // Jika data ada, set adapter
        if (scanDataList.isNotEmpty()) {


            // Hitung total karbo, lemak, gula, protein, dan total kalori
            var totalKarbo = 0
            var totalLemak = 0.0
            var totalGula = 0
            var totalProtein = 0
            var totalKaloriAkhir = 0.0

            scanDataList.forEach { data ->
                totalKarbo += data.karbo
                totalLemak += data.lemak
                totalGula += data.gula
                totalProtein += data.protein
                totalKaloriAkhir += data.totalKalori
            }

            // Tampilkan data total di UI
            binding.karboConsume.text = totalKarbo.toString()
            binding.lemakConsume.text = totalLemak.toInt().toString()
            binding.gulaConsume.text = totalGula.toString()
            binding.proteinConsume.text = totalProtein.toString()
            totalKalori = (totalProtein.toDouble()*4.0) + (totalKarbo.toDouble()*4.0) + (totalLemak*9.0)
            binding.kaloriHarianJumlah.text = totalKalori.toInt().toString()
            Log.d("TOTAL KALORI", "total kalori: $totalKalori")


            adapter = CatatanAdapter()
            binding.rvCatatanKonsumsi.adapter = adapter
            adapter.submitList(scanDataList)
            binding.catatanIsEmpty.visibility = View.GONE
            binding.rvCatatanKonsumsi.visibility = View.VISIBLE
        } else {
            binding.catatanIsEmpty.visibility = View.VISIBLE
            binding.rvCatatanKonsumsi.visibility = View.GONE
        }

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

        // catat konsumsi
        binding.btnCatatKonsumsi.setOnClickListener{
            findNavController().navigate(R.id.action_navigation_home_to_navigation_scan, null, NavOptions.Builder()
                .setPopUpTo(R.id.navigation_home, true)
                .build())
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

        binding.name.text = dataUser.name
        val percentage: Int = ((totalKalori / 2645.0) * 100.0).toInt()
        binding.progressIndicatoKalori.isIndeterminate = false
        binding.progressIndicatoKalori.progress = percentage

        val riskPoint = setDiabetesRisk(dataUser)
        Log.d("RISK_POINT", "Risk Point: $riskPoint")
        val circleGrren1 = binding.circle1
        val circleGrren2 = binding.circle2
        val circleGrren3 = binding.circle3
        val diabetesRisk = binding.diabetesRisk
        val textDiabetesRisk = binding.textDiabetesRisk

        when {
            riskPoint <= 12 -> {
                circleGrren1.background = ContextCompat.getDrawable(requireContext(), R.drawable.circle_yellow)
                circleGrren2.background = ContextCompat.getDrawable(requireContext(), R.drawable.circle_yellow)
                circleGrren3.background = ContextCompat.getDrawable(requireContext(), R.drawable.circle_yellow)

                diabetesRisk.setText(getString(R.string.risiko_diabetes_sedang))
                textDiabetesRisk.setText(getString(R.string.text_risiko_diabetes_sedang))
            }
            riskPoint >= 14 -> {
                circleGrren1.background = ContextCompat.getDrawable(requireContext(), R.drawable.circle_red)
                circleGrren2.background = ContextCompat.getDrawable(requireContext(), R.drawable.circle_red)
                circleGrren3.background = ContextCompat.getDrawable(requireContext(), R.drawable.circle_red)
                diabetesRisk.setText(getString(R.string.risiko_diabetes_tinggi))
                textDiabetesRisk.setText(getString(R.string.text_risiko_diabetes_tinggi))
            }
            else -> {
                circleGrren1.background = ContextCompat.getDrawable(requireContext(), R.drawable.circle_green)
                circleGrren2.background = ContextCompat.getDrawable(requireContext(), R.drawable.circle_green)
                circleGrren3.background = ContextCompat.getDrawable(requireContext(), R.drawable.circle_green)
                diabetesRisk.setText(getString(R.string.risiko_diabetes_rendah))
                textDiabetesRisk.setText(getString(R.string.text_risiko_diabetes_rendah))
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

        riskPoint += when {
            dataUser.umur in 35..44 -> 2
            dataUser.umur in 45..54 -> 3
            dataUser.umur!! >= 55 -> 4
            else -> 0
        }

        val bMI = dataUser.berat!!.toFloat() / ((dataUser.tinggi!!.toFloat() * dataUser.tinggi.toFloat()) * 0.0001F)
        riskPoint += when {
            bMI in 25.0..30.0 -> 1
            bMI > 30.0 -> 2
            else -> 0
        }

        riskPoint += when {
            dataUser.lingkarPinggang == "small" -> 0
            dataUser.lingkarPinggang == "medium" -> 1
            dataUser.lingkarPinggang == "large" -> 2
            else -> 0
        }

        riskPoint += when {
            dataUser.tingkatAktivitas == "none" -> 2
            else -> 0
        }

        riskPoint += when {
            dataUser.konsumsiBuah == 0 -> 1
            else -> 0
        }

        riskPoint += when {
            dataUser.tekananDarahTinggi == 1 -> 1
            else -> 0
        }

        riskPoint += when {
            dataUser.gulaDarahTinggi == 1 -> 1
            else -> 0
        }

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