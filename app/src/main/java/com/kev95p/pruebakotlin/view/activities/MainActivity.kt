package com.kev95p.pruebakotlin.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kev95p.pruebakotlin.R
import com.kev95p.pruebakotlin.view.fragments.PokedexFragment
import com.kev95p.pruebakotlin.view.fragments.TeamsFragment

class MainActivity : AppCompatActivity() {

    private val pokedexFragment: PokedexFragment = PokedexFragment()
    private val teamsFragment: TeamsFragment = TeamsFragment()
    private val fm: FragmentManager = supportFragmentManager
    var fragmentActive: Fragment = pokedexFragment
    private var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

    }

    override fun onResume() {
        super.onResume()
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        if (!teamsFragment.isAdded) {
            fm.beginTransaction().add(R.id.nav_host_fragment, teamsFragment, "2")
                .hide(teamsFragment).commit()
        }

        if (!pokedexFragment.isAdded) {

            fm.beginTransaction().add(R.id.nav_host_fragment, pokedexFragment, "1").commit()
        }

        navView.setOnNavigationItemSelectedListener { item ->
            if (R.id.navigation_pokedex == item.itemId) {
                fm.beginTransaction().hide(fragmentActive).show(pokedexFragment).commit()
                fragmentActive = pokedexFragment
                (this as? AppCompatActivity)?.supportActionBar?.title = "Pokedex"
                invalidateOptionsMenu()
            } else if (R.id.navigation_teams == item.itemId) {
                fm.beginTransaction().hide(fragmentActive).show(teamsFragment).commit()
                fragmentActive = teamsFragment
                (this as? AppCompatActivity)?.supportActionBar?.title = "Teams"
                invalidateOptionsMenu()
            }
            false
        }

    }

    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
         menuInflater.inflate(R.menu.pokedex_toolbar_selection_menu,menu)
         return true
     }*/

}