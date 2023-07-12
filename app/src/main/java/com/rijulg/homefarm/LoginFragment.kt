package com.rijulg.homefarm

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rijulg.homefarm.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    // View binding (easier call to UI elements)
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    // Firebase auth variable
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initializing auth variable
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // View binding
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        // Check if user is already signed in
        val currentUser = auth.currentUser
        if (currentUser != null) {
            if (currentUser.isEmailVerified) {
                val intent = Intent(activity, AppActivity::class.java)
                startActivity(intent)
            } else {
                currentUser.sendEmailVerification()
                replaceFragment()
                Toast.makeText(requireActivity(), "Email has not been verified", Toast.LENGTH_SHORT).show()
            }
        }

        binding.progressCheck.isVisible = false

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Nav to register frag
        binding.registerButton.setOnClickListener {
            binding.registerButton.isEnabled = false
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            view.findNavController().navigate(action)
            binding.registerButton.isEnabled = true
        }

        // Checks if email or password field is empty
        binding.emailField.setOnFocusChangeListener { _, _ ->
            val email = binding.emailField.text.toString()
            if (binding.emailField.hasFocus()) {
                binding.emailFieldText.error = null
                binding.loginButton.isEnabled = true
            } else if (TextUtils.isEmpty(email)) {
                binding.emailFieldText.error = "Email cannot be empty"
            }
        }
        binding.passwordField.setOnFocusChangeListener { _, _ ->
            val password = binding.passwordField.text.toString()
            if (binding.passwordField.hasFocus()) {
                binding.passwordFieldText.error = null
                binding.loginButton.isEnabled = true
            } else if (TextUtils.isEmpty(password)) {
                binding.passwordFieldText.error = "Password cannot be empty"
            }
        }

        binding.loginButton.setOnClickListener {
            binding.progressCheck.isVisible = true
            loginUser()
        }

    }

    private fun loginUser() {

        binding.loginButton.isEnabled = false

        val email = binding.emailField.text.toString()
        val password = binding.passwordField.text.toString()

        // Checks if email or password is empty
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            binding.progressCheck.isVisible = false
            if (TextUtils.isEmpty(email)) {
                binding.emailFieldText.error = "Email cannot be empty" }
            if (TextUtils.isEmpty(password)) {
                binding.passwordFieldText.error = "Password cannot be empty" }
            binding.description.requestFocus()

        } else {
            // Sign in cases
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()){task: Task<AuthResult> ->
                    val currentUser = auth.currentUser
                    binding.loginButton.isEnabled = true
                    binding.progressCheck.isVisible = false
                    if (task.isSuccessful) {
                        Toast.makeText(requireActivity(), "User signed in successfully", Toast.LENGTH_SHORT).show()
                        if (currentUser != null) {
                            if (currentUser.isEmailVerified) {
                                val intent = Intent(activity, AppActivity::class.java)
                                startActivity(intent)
                            } else {
                                currentUser.sendEmailVerification()
                                replaceFragment()
                                Toast.makeText(requireActivity(), "Email has not been verified", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        binding.emailFieldText.error = "Sign in error: " + task.exception?.message
                    }
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun replaceFragment() {

        parentFragmentManager
            .beginTransaction()
            .replace(R.id.nav_host_fragment, EmailVerification())
            .commit()

    }

}

