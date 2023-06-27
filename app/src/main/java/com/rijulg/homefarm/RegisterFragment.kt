package com.rijulg.homefarm

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rijulg.homefarm.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.doneRegister.setOnClickListener {
            createUser()
        }

    }

    private fun createUser() {
        val email = binding.registerEmail.text.toString()
        val password = binding.registerPassword.text.toString()

        if (TextUtils.isEmpty(email)){
            binding.registerEmail.error = "Email cannot be empty"
            binding.registerEmail.requestFocus()
        } else if (TextUtils.isEmpty(password)) {
            binding.registerPassword.error = "Password cannot be empty"
            binding.registerPassword.requestFocus()
        } else {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireActivity(), "User registered successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(requireActivity(), AppActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(requireActivity(), "Registration error:" + task.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}