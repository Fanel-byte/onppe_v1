package com.example.onppe_v1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.inflate
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.onppe_v1.databinding.ActivityFonctionnalitiesBinding
import com.example.onppe_v1.databinding.ActivityStartBinding



class FonctionnalitiesActivity : AppCompatActivity() {
    lateinit var binding: ActivityFonctionnalitiesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFonctionnalitiesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.signalement.setOnClickListener {
            // instancier la synchronization
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(
                    NetworkType.CONNECTED).build()
            // Planification du service
            val req = OneTimeWorkRequest.Builder(SynchroWorker::class.java)
                .setConstraints(constraints).build()
            // Lancement du service
            val workManager = WorkManager.getInstance(this)
            workManager.enqueueUniqueWork("work", ExistingWorkPolicy.KEEP,req)


            // aller a main activity
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
        }
    }


}