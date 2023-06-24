package com.example.onppe_v1

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
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
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = ActivityFonctionnalitiesBinding.inflate(layoutInflater)
        val view = binding.root
        setTheme(R.style.AppTheme_CustomTitle)
        setCustomTitle("")
        setContentView(view)

        binding.signalement.setOnClickListener {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val req = OneTimeWorkRequest.Builder(SynchroWorker::class.java)
                .setConstraints(constraints)
                .build()

            val workManager = WorkManager.getInstance(this)
            workManager.enqueueUniqueWork("work", ExistingWorkPolicy.KEEP, req)

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("fragment", "signalement")
            startActivity(intent)
        }

        binding.aide.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("fragment", "aide")
            startActivity(intent)
        }

        binding.langue.setOnClickListener {
            val languages = arrayOf(getString(R.string.arabe), getString(R.string.francais)) // Langues disponibles
            val dialog = AlertDialog.Builder(this)
                .setTitle(R.string.select_language)
                .setItems(languages) { _, which ->
                    val selectedLanguage = languages[which]
                    val pref = getSharedPreferences("langdata", MODE_PRIVATE)
                    val editor = pref.edit()
                    if( selectedLanguage==languages[0]) {
                        editor.putString("language", "AR")
                        editor.apply()
                    }
                   else {
                        editor.putString("language", "FR")
                        editor.apply()
                    }
                    // Mettre à jour la langue immédiatement
                    val newLocale = Locale(selectedLanguage, pref.getString("country", "DZ"))
                    val updatedContext = ContextUtils.updateLocale(this, newLocale)
                    recreate() // Redémarrer l'activité pour appliquer la nouvelle langue
                }
                .create()
            dialog.show()
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
        val language = pref.getString("language", "AR")
        val country = pref.getString("country", "DZ")
        val localeToSwitch = Locale(language, country)
        val localeUpdatedContext = ContextUtils.updateLocale(newBase, localeToSwitch)
        super.attachBaseContext(localeUpdatedContext)
    }
    object ContextUtils {
        fun updateLocale(context: Context, locale: Locale): Context {
            val config = context.resources.configuration
            config.setLocale(locale)
            return context.createConfigurationContext(config)
        }
    }

}