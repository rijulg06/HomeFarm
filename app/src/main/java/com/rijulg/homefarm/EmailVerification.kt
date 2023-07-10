package com.rijulg.homefarm

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rijulg.homefarm.databinding.FragmentEmailVerificationBinding

class EmailVerification : Fragment() {

    // Binding variables
    private var _binding: FragmentEmailVerificationBinding? = null
    private val binding get() = _binding!!

    // Firebase auth variable
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Firebase auth variable initialized
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // View binding implemented
        _binding = FragmentEmailVerificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentUser = auth.currentUser

        binding.progressCheck.isVisible = false

        // Resend email verification button
        binding.resendEmail.setOnClickListener {
            binding.resendEmail.isEnabled = false
            currentUser?.sendEmailVerification()?.addOnSuccessListener {
                Toast.makeText(requireActivity(), "Email resent", Toast.LENGTH_SHORT).show()
            }
                ?.addOnFailureListener {
                    Toast.makeText(requireActivity(), "Please check your email for a verification link.", Toast.LENGTH_LONG).show()
                }
            binding.resendEmail.isEnabled = true
        }

        // Check verification status button
        binding.checkVerificationStatus.setOnClickListener {
            binding.progressCheck.isVisible = true
            binding.checkVerificationStatus.isEnabled = false
            Thread.sleep(5000L)
            currentUser?.reload()
            if (currentUser != null) {
                if (currentUser.isEmailVerified) {
                    fragmentManager?.beginTransaction()?.replace(R.id.nav_host_fragment, UserNameFragment())?.commit()
                } else {
                    Toast.makeText(requireActivity(), "Email has not been verified", Toast.LENGTH_SHORT).show()
                }
            }
            binding.checkVerificationStatus.isEnabled = true
        }

        // Logout button
        binding.logout.setOnClickListener {
            binding.logout.isEnabled = false
            auth.signOut()
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            binding.logout.isEnabled = true
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}