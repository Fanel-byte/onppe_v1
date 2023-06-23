package com.example.onppe_v1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import com.example.onppe_v1.databinding.ActivityStartBinding
import android.content.SharedPreferences

class StartActivity : AppCompatActivity() {
    lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStartBinding.inflate(layoutInflater)
        val view = binding.root
        setTheme(R.style.AppTheme_CustomTitle)
        setCustomTitle(getString(R.string.app_name))
        setContentView(view)
        setContentView(view)

        val isFirstLaunch = getSharedPreferences("MyPrefs", MODE_PRIVATE)
            .getBoolean("isFirstLaunch", true)

        if (!isFirstLaunch) {
            launchFonctionnalitiesActivity()
            return
        }

        binding.getstarted.setOnClickListener {
            launchFonctionnalitiesActivity()
        }
    }

    private fun launchFonctionnalitiesActivity() {
        val intent = Intent(this, FonctionnalitiesActivity::class.java)
        startActivity(intent)

        // Enregistre l'indicateur d'installation dans les préférences partagées
        getSharedPreferences("MyPrefs", MODE_PRIVATE)
            .edit()
            .putBoolean("isFirstLaunch", false)
            .apply()

        finish()
    }
    private fun setCustomTitle(title: String) {
        supportActionBar?.apply {
            this.title = title

        }
    }
}
