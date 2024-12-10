package com.dicoding.sahabatgula.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.sahabatgula.data.local.entity.UserProfile
import com.dicoding.sahabatgula.databinding.ActivityProfileBinding
import com.dicoding.sahabatgula.di.Injection
import com.dicoding.sahabatgula.helper.SharedPreferencesHelper
import java.text.DecimalFormat

class ProfileActivity : AppCompatActivity() {


    private val profileViewModel by viewModels<ProfileViewModel> {
        ProfileViewModelFactory(Injection.provideRepository(this))
    }
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActionBar()
        getDataUser()
    }

    private fun setActionBar() {
        supportActionBar?.title = "Kembali"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setUserData(dataUser: UserProfile) {
        var diabatesRisk: String = "diabetes rendah"
        var bmi: Float = 0.0F

        binding.apply {
            name.text = dataUser.name
            textDiabetes.text = diabatesRisk
            numTinggi.text = dataUser.tinggi.toString()
            numBerat.text = dataUser.berat.toString()

            bmi = dataUser.berat!!.toFloat() / ((dataUser.tinggi!!.toFloat() * dataUser.tinggi!!.toFloat()) * 0.0001F)
            val decimalFormat = DecimalFormat("#.##")
            val formattedBMI = decimalFormat.format(bmi)
            numBmi.text = formattedBMI.toString()
            setBMI(bmi)
        }
    }

    private fun getDataUser() {
        val userId = SharedPreferencesHelper.getCurrentUserId(this)
        Log.d("ID_SEKARANG", "Id User at Profile: $userId")

        if (!userId.isNullOrEmpty()) {
            profileViewModel.fetchUserProfile(userId)
            observeViewModel()
        } else {
            Log.e("ID_SEKARANG", "User ID is null or empty.")
        }
    }

    private fun observeViewModel() {
        profileViewModel.dataUser.observe(this) { dataUser ->
            dataUser?.let {
                setUserData(it)
            }
        }
    }

    private fun setBMI(bmi: Float) {
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
