package com.example.onppe_v1

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.NavController
import androidx.navigation.ui.NavigationUI
import com.example.onppe_v1.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController
    private val PHONE_MENU_DELAY = 4000L
    private val phoneMenuHandler = Handler()
    var isPhoneButtonVisible = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        var click = 1
        val view = binding.root
        setContentView(view)
        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(1).isEnabled = false
        val navHostFragment = supportFragmentManager. findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavigationView,navController)
//cacher le menu phone
        binding.root.setOnTouchListener { _, _ ->

                hidePhoneMenu()
                false

        }


        binding.phone.setOnClickListener {

            if (click == 1) {
                binding.phone.backgroundTintList =
                    ColorStateList.valueOf(Color.BLACK)
                binding.phone.setImageResource(R.drawable.ic_close)
                binding.onppe.show()
                binding.police.show()
                binding.protectioncivile.show()
                click = 0
                isPhoneButtonVisible = true
                phoneMenuHandler.removeCallbacksAndMessages(null)
                phoneMenuHandler.postDelayed({ hidePhoneMenu() }, PHONE_MENU_DELAY)


            } else {
                binding.phone.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#59C55E"))
                binding.phone.setImageResource(R.drawable.ic_phone)
                binding.onppe.hide()
                binding.police.hide()
                binding.protectioncivile.hide()
                click = 1
                isPhoneButtonVisible = false
            }

        }

        val onppe = "1111"
        binding.onppe.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$onppe")
            startActivity(intent)
        }
        val police = "17"
        binding.police.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$police")
            startActivity(intent)
        }
        val protection = "14"
        binding.protectioncivile.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$protection")
            startActivity(intent)
        }


        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true

            when (menuItem.itemId) {
                R.id.signalementFragment -> {


                    navController.popBackStack(R.id.signalementFragment, false)

                    true
                }
                R.id.placeholder -> {

                    true
                }
                R.id.aideFragment -> {

                    navController.popBackStack(R.id.aideFragment, false)
                    navController.navigate(R.id.aideFragment) // ouvrir le fragment AideFragment
                    true
                }
                else -> false
            }
        }
    }


    private fun hidePhoneMenu() {
        binding.phone.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#59C55E"))
        binding.phone.setImageResource(R.drawable.ic_phone)
        binding.onppe.hide()
        binding.police.hide()
        binding.protectioncivile.hide()
        isPhoneButtonVisible = false

    }


}


