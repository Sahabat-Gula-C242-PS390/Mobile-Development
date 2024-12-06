package com.dicoding.sahabatgula.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dicoding.sahabatgula.R
import com.dicoding.sahabatgula.data.local.entity.UserProfile
import com.dicoding.sahabatgula.databinding.FragmentRegisterBinding
import com.dicoding.sahabatgula.di.Injection
import com.dicoding.sahabatgula.ui.questionnaire.BasicInfoFragment

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels {
        RegisterViewModelFactory(Injection.provideRepository(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvLoginNow = binding.loginNow
        tvLoginNow.setOnClickListener{
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
        }

        val btnRegister = binding.btnRegister
        btnRegister.setOnClickListener {
            val username = binding.editInputUsername.text.toString()
            val email = binding.editInputEmailRegister.text.toString()
            val password = binding.editInputPasswordRegister.text.toString()

            val userProfile = UserProfile(
                name = username,
                email = email,
                password = password
            )

            // menyimpan data ke view model
            registerViewModel.saveUserProfile(userProfile)
            // menyimpan data ke lokal
            registerViewModel.saveToDatabase(userProfile)

            // lanjut ke halaman selanjutnya
            moveToNextFragment()
        }
    }

    private fun moveToNextFragment() {
        val nextFragment = BasicInfoFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.activity_login, nextFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}
