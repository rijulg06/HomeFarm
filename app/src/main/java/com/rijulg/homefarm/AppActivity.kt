package com.rijulg.homefarm

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rijulg.homefarm.databinding.ActivityAppBinding

class AppActivity : AppCompatActivity() {

    // View binding variable
    private lateinit var binding : ActivityAppBinding

    // Firebase auth variable
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // View binding implemented
        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Opens HomeFragment from start
        replaceFragment(HomeFragment())

        // Bottom nav bar button implementation
        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.home -> replaceFragment(HomeFragment())
                R.id.post -> replaceFragment(PostFragment())
                R.id.inbox -> replaceFragment(InboxFragment())
                R.id.account -> replaceFragment(AccountFragment())

                else -> {
                }
            }
            true
        }

        // Firebase auth variable initialized
        auth = Firebase.auth

    }

    override fun onStart() {
        super.onStart()

        // Email verification check
        val currentUser = auth.currentUser
        if (currentUser != null) {
            if (!currentUser.isEmailVerified){
                replaceFragment(EmailVerification())
                binding.bottomNavigation.visibility = View.GONE
                Toast.makeText(this, "Email has not been verified", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .commit()

    }
}