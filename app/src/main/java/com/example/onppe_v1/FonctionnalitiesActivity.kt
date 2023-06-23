package com.example.onppe_v1

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.onppe_v1.databinding.ActivityFonctionnalitiesBinding
import com.example.onppe_v1.databinding.ActivityStartBinding
import java.util.*

class FonctionnalitiesActivity : AppCompatActivity() {
    lateinit var binding: ActivityFonctionnalitiesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFonctionnalitiesBinding.inflate(layoutInflater)
        val view = binding.root
        //   hideTitle() // Appel de la méthode pour masquer le titre
        setTheme(R.style.AppTheme_CustomTitle)
        setCustomTitle(getString(R.string.app_name))
        setContentView(view)

        binding.signalement.setOnClickListener {
            // instancier la synchronisation
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            // Planification du service
            val req = OneTimeWorkRequest.Builder(SynchroWorker::class.java)
                .setConstraints(constraints)
                .build()

            // Lancement du service
            val workManager = WorkManager.getInstance(this)
            workManager.enqueueUniqueWork("work", ExistingWorkPolicy.KEEP, req)

            // Aller à MainActivity
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("fragment", "signalement")
            startActivity(intent)
        }
        binding.aide.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("fragment", "aide")
            startActivity(intent)
        }
    }

    private fun hideTitle() {
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }
    private fun setCustomTitle(title: String) {
        supportActionBar?.apply {
            this.title = title

        }
    }
    // Language support
    override fun attachBaseContext(newBase: Context) {
        
        val pref = newBase.getSharedPreferences("langdata", MODE_PRIVATE)
        val language = pref.getString("language","AR")
        //val language = pref.getString("language","FR")
        val country = pref.getString("country","DZ")
        //val country = pref.getString("country","FR")
        val localeToSwitch = Locale(language,country)
        val localeUpdatedContext = ContextUtils.updateLocale(newBase, localeToSwitch)
        super.attachBaseContext(localeUpdatedContext)
    }
}