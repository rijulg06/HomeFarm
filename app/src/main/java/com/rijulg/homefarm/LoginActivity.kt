package com.rijulg.homefarm

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.rijulg.homefarm.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    companion object {
        const val TAG = "LoginActivity"
    }

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

    //    val button = findViewById<Button>(R.id.loginButton)

      //  button.setOnClickListener {
      //          Log.i(TAG, "tag")
     //   }

    }

    private fun startAppActivity() {
        val intent = Intent(this, AppActivity::class.java)
        startActivity(intent)
    }

}