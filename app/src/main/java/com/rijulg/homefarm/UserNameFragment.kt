package com.rijulg.homefarm

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.rijulg.homefarm.databinding.FragmentUserInfoBinding
import com.rijulg.homefarm.models.User

class UserNameFragment : Fragment() {

    // View binding variables
    private var _binding: FragmentUserInfoBinding? = null
    private val binding get() = _binding!!

    // Firebase auth variable
    private lateinit var auth: FirebaseAuth

    // Firestore variable
    private lateinit var firestoreDb: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Firestore variable initialized
        firestoreDb = FirebaseFirestore.getInstance()

        // Firebase auth variable initialized
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // View binding implemented
        _binding = FragmentUserInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerName.setOnFocusChangeListener { _, _ ->
            val email = binding.registerName.text.toString()
            if (binding.registerName.hasFocus()) {
                binding.registerNameText.error = null
            } else if (TextUtils.isEmpty(email)) {
                binding.registerNameText.error = "Email cannot be empty"
            }
        }

        binding.nextButton.setOnClickListener {
            val name = binding.registerName.text.toString()
            if (TextUtils.isEmpty(name)) {
                binding.registerNameText.error = "Email cannot be empty"
                binding.finishAccount.requestFocus()
            } else {
                binding.progressCheck.progress = 100
                val user = User(name = name)
                auth.currentUser?.let { it1 -> firestoreDb.collection("users").document(it1.uid).set(user) }
                parentFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, UserLocationFragment()).commit()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}