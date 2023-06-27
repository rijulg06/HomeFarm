package com.rijulg.homefarm

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rijulg.homefarm.databinding.FragmentRegisterBinding
import org.w3c.dom.Text

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

        binding.registerEmail.setOnFocusChangeListener { _, _ ->
            val email = binding.registerEmail.text.toString()
            if (binding.registerEmail.hasFocus()) {
                binding.registerEmailText.error = null
            } else if (TextUtils.isEmpty(email)) {
                binding.registerEmailText.error = "Email cannot be empty"
            }
        }

        binding.registerPassword.setOnFocusChangeListener { _, _ ->
            val password = binding.registerPassword.text.toString()
            if (binding.registerPassword.hasFocus()) {
                binding.registerPasswordText.error = null
            } else if (TextUtils.isEmpty(password)) {
                binding.registerPasswordText.error = "Password cannot be empty"
            }
        }

        binding.doneRegister.setOnClickListener {
            createUser()
        }

    }

    private fun createUser() {
        val email = binding.registerEmail.text.toString()
        val password = binding.registerPassword.text.toString()

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            if (TextUtils.isEmpty(email)) {
            binding.registerEmailText.error = "Email cannot be empty" }
            if (TextUtils.isEmpty(password)) {
                binding.registerPasswordText.error = "Password cannot be empty" }
            binding.doneRegister.requestFocus()
        } else {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireActivity(), "User registered successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(requireActivity(), AppActivity::class.java)
                        startActivity(intent)
                    } else {
                        binding.registerEmailText.error = "Registration error: " + task.exception?.message
                    }
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}