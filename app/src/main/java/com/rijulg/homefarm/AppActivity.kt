package com.rijulg.homefarm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.rijulg.homefarm.databinding.ActivityAppBinding

class AppActivity : AppCompatActivity() {

    // View binding variable
    private lateinit var binding : ActivityAppBinding

    // Firebase auth variable
//    private lateinit var auth: FirebaseAuth

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

    }

    private fun replaceFragment(fragment: Fragment) {

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .commit()

    }
}