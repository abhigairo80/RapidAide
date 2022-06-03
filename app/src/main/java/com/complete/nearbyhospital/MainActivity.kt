package com.complete.nearbyhospital

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {
    var t: Toolbar? = null
    var drawer: DrawerLayout? = null
    var nametext: EditText? = null
    var agetext: EditText? = null
    var enter: ImageView? = null
    var male: RadioButton? = null
    var female: RadioButton? = null
    var rg: RadioGroup? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawer = findViewById(R.id.draw_activity)
        t = findViewById<View>(R.id.toolbar) as Toolbar
        nametext = findViewById(R.id.nametext)
        agetext = findViewById(R.id.agetext)
        enter = findViewById(R.id.enter)
        male = findViewById(R.id.male)
        female = findViewById(R.id.female)
        rg = findViewById(R.id.rg1)
        val toggle = ActionBarDrawerToggle(this,
            drawer,
            t,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        val nav = findViewById<NavigationView>(R.id.nav_view)
        enter.setOnClickListener(View.OnClickListener {
            val name = nametext.getText().toString()
            val age = agetext.getText().toString()
            var gender = String()
            val id = rg.getCheckedRadioButtonId()
            when (id) {
                R.id.male -> gender = "Mr."
                R.id.female -> gender = "Ms."
            }
            val symp = Intent(this@MainActivity, SymptomsActivity::class.java)
            symp.putExtra("name", name)
            symp.putExtra("gender", gender)
            startActivity(symp)
        })
        nav.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_sos -> {
                    val `in` = Intent(this@MainActivity, CallActivity::class.java)
                    startActivity(`in`)
                }
                R.id.nav_share -> {
                    val myIntent = Intent(Intent.ACTION_SEND)
                    myIntent.type = "text/plain"
                    startActivity(Intent.createChooser(myIntent, "SHARE USING"))
                }
                R.id.nav_hosp -> {
                    val browserIntent = Intent(Intent.ACTION_VIEW)
                    browserIntent.data =
                        Uri.parse("https://www.google.com/maps/search/hospitals+near+me")
                    startActivity(browserIntent)
                }
                R.id.nav_cntus -> {
                    val c_us = Intent(this@MainActivity, ContactUs::class.java)
                    startActivity(c_us)
                }
            }
            drawer.closeDrawer(GravityCompat.START)
            true
        }
    }
}
