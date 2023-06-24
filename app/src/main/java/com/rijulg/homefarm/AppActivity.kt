package com.rijulg.homefarm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.rijulg.homefarm.databinding.ActivityAppBinding

class AppActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAppBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomeFragment())

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