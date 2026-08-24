package com.ngaming.ngamingcase

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.ngaming.ngamingcase.core.ui.base.BaseActivity
import com.ngaming.ngamingcase.core.ui.ext.applySystemBarInsets
import com.ngaming.ngamingcase.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

/** Tek Activity. Toolbar ve ekranları taşıyan navigation host burada duruyor. */
@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
        super.onCreate(savedInstanceState)

        binding.appBar.applySystemBarInsets(top = true)
        setSupportActionBar(binding.toolbar)

        val navHost = supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        navController = navHost.navController
        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean =
        navController.navigateUp() || super.onSupportNavigateUp()
}
