package com.example.onppe_v1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.inflate
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
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
        }
    }


}