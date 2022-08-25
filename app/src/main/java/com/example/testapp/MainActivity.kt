package com.example.testapp

import android.content.ContentValues
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.testapp.databinding.ActivityMainBinding
import com.example.testapp.ui.gallery.GalleryFragment
import com.example.testapp.ui.gallery.User
import com.example.testapp.ui.home.HomeFragment
import com.google.firebase.database.FirebaseDatabase
import java.util.prefs.Preferences

interface Communicator{

    fun passDataCom(editTextInput: String)

}

class MainActivity : AppCompatActivity() , Communicator {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var list = listOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.Update.setOnClickListener {
            var localdb = Squerys()
            var rs = localdb.SelectDb(applicationContext,null)
            var move = rs.moveToNext()
            var fireDataBase = FirebaseDatabase.getInstance().getReference("Users")
            if(rs.moveToNext()) {
                while (move) {
                    if (fireDataBase != null) {
                        var user = rs.getString(1).toString()
                        fireDataBase.child(user).get().addOnSuccessListener {
                            if (it.exists()) {
                                val descrip = it.child("Descripcion").value
                                val name = it.child("Name").value
                                localdb.InsertTable(
                                    applicationContext,
                                    name.toString(),
                                    descrip.toString(),
                                    null
                                )
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    "User Don't Exist",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }.addOnFailureListener {
                            Toast.makeText(applicationContext, "FATAL ERROR", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                    move = rs.moveToNext()
                }
            }
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun passDataCom(editTextInput: String) {
        val bundle = Bundle()
        bundle.putString("input_txt", editTextInput)
        var aut = editTextInput.split(":")
        var win = aut[0].toString()
        var insert = Squerys()
        insert.InsertTable(applicationContext,win,aut[1],null)
    }


}