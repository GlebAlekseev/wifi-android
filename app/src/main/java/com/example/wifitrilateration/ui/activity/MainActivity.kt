package com.example.wifitrilateration.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.wifitrilateration.di.FromViewModelFactory
import com.example.wifitrilateration.domain.entity.ResultStatus
import com.example.wifitrilateration.ui.viewmodel.MainViewModel
import com.example.wifitrilateration.utils.appComponent
import com.glebalekseevjk.wifitrilateration.R
import com.glebalekseevjk.wifitrilateration.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @FromViewModelFactory
    @Inject
    lateinit var mainViewModel: MainViewModel

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding ?: throw RuntimeException("ActivityMainBinding is null")


    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.createMainActivitySubcomponent().inject(this)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNavigationUI()
        initListeners()
        checkAuth()
    }

    private fun checkAuth() {
        if (mainViewModel.isAuth) {
            navController.navigate(R.id.action_authFragment_to_mapsFragment)
        }
    }

    override fun onStop() {
        super.onStop()
        _binding = null
    }

    private fun initNavigationUI() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_fcv) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.mainNv, navController)

//        val appBarConfiguration = AppBarConfiguration(navController.graph)
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    private fun initListeners() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.authFragment -> {
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }
                else -> {
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                }
            }
        }

        binding.mainNv.setNavigationItemSelectedListener {
            onDrawerItemSelected(it)
        }
    }


    private fun onDrawerItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exit_menu_item -> {
                mainViewModel.logout {
                    if (navController.currentDestination?.id == R.id.mapsFragment) {
                        navController.navigate(R.id.action_mapsFragment_to_authFragment)
                    }
                }
                return true
            }
            R.id.set_ap_location_menu_item -> {
                if (navController.currentDestination?.id == R.id.mapsFragment) {
                    navController.navigate(R.id.action_mapsFragment_to_routersConfigFragment)
                }
            }
            else -> {}
        }
        return false
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        println("^^^^^^^^^^^^^6 onNewIntent")
        val code = intent?.data?.getQueryParameter("code")
        code?.let {
            mainViewModel.updateTokenPair(code) { result ->
                println("^^^^^^^^^^ mainViewModel.isAuth 0")

                when (result.status) {
                    ResultStatus.SUCCESS -> {
                        println("^^^^^^^^^^ mainViewModel.isAuth 1")
                        if (mainViewModel.isAuth) {
                            println("^^^^^^^^^^ mainViewModel.isAuth 2")

                            navController.navigate(R.id.action_authFragment_to_mapsFragment)
                        }
                    }
                    ResultStatus.LOADING -> {
                    }
                    ResultStatus.FAILURE -> {
                        Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.main_fcv, fragment)
            .commit()
    }
}

