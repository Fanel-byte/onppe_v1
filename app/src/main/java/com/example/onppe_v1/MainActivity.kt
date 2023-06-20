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
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.view.Menu
import android.view.MenuInflater
import androidx.navigation.ui.setupActionBarWithNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController
    private val PHONE_MENU_DELAY = 4000L
    private val phoneMenuHandler = Handler()
    var isPhoneButtonVisible = false
    var SYNCH = 0
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
        //NavigationUI.setupWithNavController(binding.bottomNavigationView,navController)
        setupActionBarWithNavController(navController)
        SYNCH = SYNCH+1
        //cacher le menu phone
        binding.fragmentContainerView.setOnTouchListener { _, _ ->
            hidePhoneMenu()
            click=1
            true
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
                //Desactiver le bouton appel apres 4 secondes
                phoneMenuHandler.postDelayed({
                    hidePhoneMenu()
                    click = 1
                }, PHONE_MENU_DELAY)


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


   /*
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private val networkStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = connectivityManager.activeNetworkInfo
            val isConnected = activeNetwork?.isConnected == true
            SYNCH = SYNCH+1
            if (isConnected) {
                coroutineScope.launch {
                    SYNCH = SYNCH+1
                    functionX()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(networkStateReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(networkStateReceiver)
    }

    private suspend fun functionX() {
        // Send to the server
        println("Function X called after connection established ...............")
    }
    */

    private fun hidePhoneMenu() {
        binding.phone.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#59C55E"))
        binding.phone.setImageResource(R.drawable.ic_phone)
        binding.onppe.hide()
        binding.police.hide()
        binding.protectioncivile.hide()
        isPhoneButtonVisible = false

    }


    // Overflow Menu

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.overflow_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.home-> {
                finish()
            }

        }
        return super.onOptionsItemSelected(item)

    }


    // Back Button
    override fun onSupportNavigateUp() = navController.navigateUp() || super.onSupportNavigateUp()

}

