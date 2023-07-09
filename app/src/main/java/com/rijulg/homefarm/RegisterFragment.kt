package com.rijulg.homefarm

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.navigation.findNavController
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rijulg.homefarm.databinding.FragmentRegisterBinding
import org.w3c.dom.Text

class RegisterFragment : Fragment() {

    //View binding variables
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    // Firebase auth variable
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Firebase auth variable initialization
        auth = Firebase.auth
    }

    override fun onStart() {
        super.onStart()
        binding.progressCheck.isVisible = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // View binding implementation
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Checks email or password empty
        binding.registerEmail.setOnFocusChangeListener { _, _ ->
            val email = binding.registerEmail.text.toString()
            if (binding.registerEmail.hasFocus()) {
                binding.registerEmailText.error = null
                binding.doneRegister.isEnabled = true
            } else if (TextUtils.isEmpty(email)) {
                binding.registerEmailText.error = "Email cannot be empty"
            }
        }
        binding.registerPassword.setOnFocusChangeListener { _, _ ->
            val password = binding.registerPassword.text.toString()
            if (binding.registerPassword.hasFocus()) {
                binding.registerPasswordText.error = null
                binding.doneRegister.isEnabled = true
            } else if (TextUtils.isEmpty(password)) {
                binding.registerPasswordText.error = "Password cannot be empty"
            }
        }

        // Register button
        binding.doneRegister.setOnClickListener {
            binding.progressCheck.isVisible = true
            createUser()
        }

    }

    private fun createUser() {

        binding.doneRegister.isEnabled = false

        val email = binding.registerEmail.text.toString()
        val password = binding.registerPassword.text.toString()

        // Checks if email or password is empty
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            binding.progressCheck.isVisible = false
            if (TextUtils.isEmpty(email)) {
            binding.registerEmailText.error = "Email cannot be empty" }
            if (TextUtils.isEmpty(password)) {
                binding.registerPasswordText.error = "Password cannot be empty" }
            binding.registerHere.requestFocus()
        } else {

            // Register cases
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task: Task<AuthResult> ->
                    binding.doneRegister.isEnabled = true
                    binding.progressCheck.isVisible = false
                    if (task.isSuccessful) {
                        val currentUser = auth.currentUser
                        currentUser?.sendEmailVerification()
                        Toast.makeText(requireActivity(), "User registered successfully", Toast.LENGTH_SHORT).show()
                        fragmentManager?.beginTransaction()?.replace(R.id.nav_host_fragment, EmailVerification())?.commit()
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