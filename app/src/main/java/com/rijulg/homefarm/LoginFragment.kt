package com.rijulg.homefarm

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rijulg.homefarm.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

 //   lateinit var binding: FragmentLoginBinding

    private lateinit var email: String
    private var _binding: FragmentLoginBinding? = null
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
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(activity, AppActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerButton.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            view.findNavController().navigate(action)
        }

        binding.loginButton.setOnClickListener {
            loginUser()
        }

    }

    private fun loginUser() {
        val email = binding.emailField.text.toString()
        val password = binding.passwordField.text.toString()

        if (TextUtils.isEmpty(email)){
            binding.emailField.error = "Email cannot be empty"
            binding.emailField.requestFocus()
        } else if (TextUtils.isEmpty(password)) {
            binding.passwordField.error = "Password cannot be empty"
            binding.passwordField.requestFocus()
        } else {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()){task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireActivity(), "User signed in successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(requireActivity(), AppActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(requireActivity(), "Sign in error:" + task.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

