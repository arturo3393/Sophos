package com.google.challengesophos.ui



import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.challengesophos.R
import com.google.challengesophos.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//Databinding initialized

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //sets the toolbar of the activity
        setSupportActionBar(binding.activitiyToolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        //Include the hamburger menu in the toolbar
        binding.activitiyToolbar.overflowIcon = getDrawable(R.drawable.ic_menu)

        //Changes the tittle color
        binding.activitiyToolbar.setTitleTextColor(resources.getColor(R.color.primaryLightColor))


        //Puts this icon as the Navigate up icon
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow_light)

    }

    //sets the back button
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()

    }

    //enables the menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    //navigate to each option in the menu
     override fun onOptionsItemSelected(item: MenuItem): Boolean {
          return when (item.itemId) {
              R.id.sendDocsMenu -> {
                  navController.navigate(R.id.action_welcomeFragment_to_sendDocsFragment)

                  true
              }
              R.id.seeDocsMenu -> {
                  navController.navigate(R.id.action_welcomeFragment_to_seeDocsFragment)
                  true
              }
              R.id.officesMenu -> {
                  navController.navigate(R.id.action_welcomeFragment_to_officesFragment)
                  true
              }

              R.id.logoutMenu -> {
                  navController.navigate(R.id.action_welcomeFragment_to_loginFragment)
                  true
              }
              //missing the dark and language menu
              /*R.id.darkModeMenu->view?.findNavController()?.navigate(R
                      R.id.languageMenu->view?.findNavController()?.navigate(R*/
              else -> {
                  super.onOptionsItemSelected(item)
              }
          }
      }




}