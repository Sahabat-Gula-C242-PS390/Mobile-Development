package com.dicoding.sahabatgula.ui.auth

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.sahabatgula.R
import com.dicoding.sahabatgula.databinding.ActivityLoginBinding
import com.dicoding.sahabatgula.di.Injection
import com.dicoding.sahabatgula.ui.profile.ProfileFragment

class LoginActivity : AppCompatActivity(){

    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(Injection.provideRepository(this))
    }
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

        val btnLogin: Button = binding.btnLogin
        btnLogin.setOnClickListener{
            val email = binding.editInputEmail.text.toString().trim()
            val password = binding.editInputPassword.text.toString().trim()
            if(email.isNotEmpty() && password.isNotEmpty()){
                loginViewModel.login(email, password)
            } else {
                Toast.makeText(this, "Email dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }

        observeLoginResult()
    }

    private fun observeLoginResult() {
        loginViewModel.loginResult.observe(this) { result ->
            result.onSuccess { _ ->
                Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()
                gotoMainAcivity()
            }
            result.onFailure { exception ->
                Toast.makeText(this, "Login gagal: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun gotoMainAcivity() {
//        Intent(this, MainActivity::class.java).also { intent ->
//            startActivity(intent)
//            finish()
//        }


        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_login, ProfileFragment())
            .addToBackStack(LoginActivity::class.java.simpleName)
            .commit()

    }

//    private fun toRegIsterNow() {
//
//    }

//    private fun forgotPass()

//    private lateinit var binding: FragmentHomeBinding
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = FragmentHomeBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//    }

}