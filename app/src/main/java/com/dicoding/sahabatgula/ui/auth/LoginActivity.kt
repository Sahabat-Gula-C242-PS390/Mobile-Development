package com.dicoding.sahabatgula.ui.auth

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.sahabatgula.R
import com.dicoding.sahabatgula.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity(){

    private lateinit var binding: ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tvRegisterNow: TextView = binding.registerNow
        tvRegisterNow.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.activity_login, RegisterFragment())
                .addToBackStack(LoginActivity::class.java.simpleName)
                .commit()
        }


    }

    private fun toRegIsterNow() {

    }

//    private fun forgotPass()


}