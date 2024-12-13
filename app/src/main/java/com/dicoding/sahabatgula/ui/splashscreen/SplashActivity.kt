package com.dicoding.sahabatgula.ui.splashscreen


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.dicoding.sahabatgula.R
import com.dicoding.sahabatgula.databinding.ActivitySplashBinding
import com.dicoding.sahabatgula.ui.auth.LoginActivity
import com.dicoding.sahabatgula.ui.profile.ProfileActivity
import com.dicoding.sahabatgula.ui.profile.UpgradeFragment


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }, 3000L)
    }


}