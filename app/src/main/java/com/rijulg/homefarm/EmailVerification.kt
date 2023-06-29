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

    private var _binding: FragmentEmailVerificationBinding? = null
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
        _binding = FragmentEmailVerificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentUser = auth.currentUser

        binding.resendEmail.setOnClickListener {
            currentUser?.sendEmailVerification()?.addOnSuccessListener {
                Toast.makeText(requireActivity(), "Email resent", Toast.LENGTH_SHORT).show()
            }
        }

        binding.checkVerificationStatus.setOnClickListener {
            currentUser?.reload()
            binding.verifyText.text = currentUser?.isEmailVerified.toString()
            val intent = Intent(requireActivity(), AppActivity::class.java)
            startActivity(intent)
        }

        binding.logout.setOnClickListener {
            auth.signOut()
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
        }

    }

}