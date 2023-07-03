package com.rijulg.homefarm

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

        // Resend email verification button
        val currentUser = auth.currentUser
        binding.resendEmail.setOnClickListener {
            currentUser?.sendEmailVerification()?.addOnSuccessListener {
                Toast.makeText(requireActivity(), "Email resent", Toast.LENGTH_SHORT).show()
            }
        }

        // Check verification status button
        binding.checkVerificationStatus.setOnClickListener {
            // TODO: add progress check indicator here
            Thread.sleep(1500L)
            currentUser?.reload()
            val intent = Intent(requireActivity(), AppActivity::class.java)
            startActivity(intent)
        }

        // Logout button
        binding.logout.setOnClickListener {
            auth.signOut()
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
        }

    }

}