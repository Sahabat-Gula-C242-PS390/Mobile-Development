package com.dicoding.sahabatgula.ui.profile

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.dicoding.sahabatgula.R
import com.dicoding.sahabatgula.data.local.entity.UserProfile
import com.dicoding.sahabatgula.databinding.ActivityProfileBinding
import com.dicoding.sahabatgula.di.Injection
import com.dicoding.sahabatgula.helper.SharedPreferencesHelper
import com.dicoding.sahabatgula.ui.auth.LoginActivity
import com.dicoding.sahabatgula.ui.auth.RegisterFragment
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
        goToUpgrade()
        changePass()

        binding.logout.setOnClickListener {

            SharedPreferencesHelper.clearUserSession(this)
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            Toast.makeText(this, "Anda telah berhasil logout", Toast.LENGTH_SHORT).show()
        }
    }

    private fun changePass() {
        binding.optionChangePass.setOnClickListener {
            Toast.makeText(this, "Dalam pengembangan", Toast.LENGTH_LONG).show()
        }
    }

    private fun setActionBar() {
        supportActionBar?.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.white_bg_action_bar))
        supportActionBar?.elevation = 0f
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.apply {
            val textView = TextView(this@ProfileActivity).apply {
                text = context.getString(R.string.kembali)
                textSize = 16f
                setTypeface(typeface, Typeface.NORMAL)
                setTextColor(ContextCompat.getColor(context, R.color.black))
                setBackgroundColor(ContextCompat.getColor(context, R.color.white))
                gravity = Gravity.START
            }
            setCustomView(
                textView,
                ActionBar.LayoutParams(
                    ActionBar.LayoutParams.MATCH_PARENT,
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    Gravity.START
                )

            )
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setUserData(dataUser: UserProfile) {
        var bmi: Float = 0.0F

        val mRiskPoint = setDiabetesRisk(dataUser)

        binding.apply {
            name.text = dataUser.name
            riskPoint.text = mRiskPoint.toString()
            numTinggi.text = dataUser.tinggi.toString()
            numBerat.text = dataUser.berat.toString()

            bmi = dataUser.berat!!.toFloat() / ((dataUser.tinggi!!.toFloat() * dataUser.tinggi!!.toFloat()) * 0.0001F)
            val decimalFormat = DecimalFormat("#.##")
            val formattedBMI = decimalFormat.format(bmi)
            numBmi.text = formattedBMI.toString()
            setBMI(bmi)
        }

        val diabetesRisk = binding.diabetesRiskCard
        val textDiabetesRisk = binding.textDiabetesRiskCard
        val diabetesCard = binding.diabetesCard
        val indicatorPoint = binding.indicatorPointDiabetes
        val textRiskPoint = binding.riskPoint

        when {
            mRiskPoint <= 12 -> {
                textRiskPoint.setTextColor(ContextCompat.getColor(this, R.color.yellow_mid_risk))
                indicatorPoint.setIndicatorColor(ContextCompat.getColor(this, R.color.yellow_mid_risk))
                diabetesCard.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.yellow_fill_risk))
                diabetesCard.setStrokeColor(ContextCompat.getColor(this, R.color.yellow_mid_risk))
                diabetesRisk.setText(R.string.risiko_diabetes_sedang)
                textDiabetesRisk.setText(R.string.text_risiko_diabetes_sedang)
            }
            mRiskPoint >= 14 -> {
                textRiskPoint.setTextColor(ContextCompat.getColor(this, R.color.red_high_risk))
                indicatorPoint.setIndicatorColor(ContextCompat.getColor(this, R.color.red_high_risk))
                diabetesCard.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red_fill_risk))
                diabetesCard.setStrokeColor(ContextCompat.getColor(this, R.color.red_high_risk))
                diabetesRisk.setText(R.string.risiko_diabetes_tinggi)
                textDiabetesRisk.setText(R.string.text_risiko_diabetes_tinggi)
            }
            else -> {
                textRiskPoint.setTextColor(ContextCompat.getColor(this, R.color.green_low_risk))
                indicatorPoint.setIndicatorColor(ContextCompat.getColor(this, R.color.green_low_risk))
                diabetesCard.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.green_fill_risk))
                diabetesCard.setStrokeColor(ContextCompat.getColor(this, R.color.green_low_risk))
                diabetesRisk.setText(R.string.risiko_diabetes_rendah)
                textDiabetesRisk.setText(R.string.text_risiko_diabetes_rendah)
            }
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

    private fun setDiabetesRisk(dataUser: UserProfile): Int{
        var riskPoint: Int = 0

        riskPoint += when {
            dataUser.umur in 35..44 -> 2
            dataUser.umur in 45..54 -> 3
            dataUser.umur!! >= 55 -> 4
            else -> 0
        }

        val bMI = dataUser.berat!!.toFloat() / ((dataUser.tinggi!!.toFloat() * dataUser.tinggi!!.toFloat()) * 0.0001F)
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

    private fun goToUpgrade() {
        binding.optionUpgrade.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.activity_profile, UpgradeFragment())
                .addToBackStack(ProfileActivity::class.java.simpleName)
                .commit()
        }
    }

}
