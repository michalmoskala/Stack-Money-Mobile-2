package com.example.zbyszek.stackmoney2.activities

import android.support.v4.app.Fragment
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.zbyszek.stackmoney2.*
import com.example.zbyszek.stackmoney2.fragments.*
import com.example.zbyszek.stackmoney2.sql.AppDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import com.example.zbyszek.stackmoney2.helpers.Preferences
import kotlinx.android.synthetic.main.nav_header_main.view.*
import org.jetbrains.anko.runOnUiThread


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var database : AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        databaseConnection()

        val l = Preferences.getUserId(this)
        doAsync {
            val login = database.userDAO().getLoginById(l)
            uiThread {
                Toast.makeText(applicationContext,"Witaj " + login, Toast.LENGTH_SHORT).show()
                val hView = nav_view.getHeaderView(0)
                hView.textView.text = "Witaj $login!"
            }
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        val b = intent.extras
        var value = -1 // or other values
        if (b != null)
            value = b.getInt("key")

        if (value == -1)
            onNavigationItemSelected(nav_view.menu.getItem(0))
        else
            onNavigationItemSelected(nav_view.menu.getItem(value))
    }

    fun databaseConnection(){
        database = AppDatabase.getInMemoryDatabase(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            if (nav_view.menu.getItem(0).isChecked)
                finish()
            else
                onNavigationItemSelected(nav_view.menu.getItem(0))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun swapFragments(fragment: android.app.Fragment){
        fragmentManager
                .beginTransaction()
                .replace(R.id.fragment_frame, fragment)
                .addToBackStack(null)
                .commit()
    }

    private fun swapFragments(fragment: android.support.v4.app.Fragment){
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_frame, fragment)
                .addToBackStack(null)
                .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                swapFragments(OperationsFragment())
                nav_view.menu.getItem(0).isChecked = true
                supportActionBar!!.setTitle(R.string.title_operations)
            }
            R.id.nav_gallery -> {
                swapFragments(CategoriesFragment())
                nav_view.menu.getItem(1).isChecked = true
                supportActionBar!!.setTitle(R.string.title_categories)
            }
            R.id.nav_slideshow -> {
                swapFragments(StatisticsFragment())
                nav_view.menu.getItem(2).isChecked = true
                supportActionBar!!.setTitle(R.string.title_statistics)
            }
            R.id.nav_manage -> {
                swapFragments(PatternsFragment())
                nav_view.menu.getItem(3).isChecked = true
                supportActionBar!!.setTitle(R.string.title_patterns)
            }
            R.id.nav_share -> {
                swapFragments(AccountsFragment())
                nav_view.menu.getItem(4).isChecked = true
                supportActionBar!!.setTitle(R.string.title_accounts)
            }
            R.id.nav_send -> {
                swapFragments(PlannedFragment())
                nav_view.menu.getItem(5).isChecked = true
                supportActionBar!!.setTitle(R.string.title_planned)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
    }
}
