package com.funnyautoreply

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import android.view.Menu
import android.view.MenuItem
import android.view.SubMenu
import com.funnyautoreply.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        //binding.toolbar.setBackgroundColor(Color.parseColor("#80000000"));
        requestNeededPermissions()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        val toolbarMenu: Menu = binding.toolbar.menu
        menuInflater.inflate(R.menu.toolbar_menu_main, toolbarMenu)
        for (i in 0 until toolbarMenu.size()) {
            val menuItem: MenuItem = toolbarMenu.getItem(i)
            menuItem.setOnMenuItemClickListener { item -> onOptionsItemSelected(item) }
            if (menuItem.hasSubMenu()) {
                val subMenu: SubMenu = menuItem.subMenu
                for (j in 0 until subMenu.size()) {
                    subMenu.getItem(j)
                        .setOnMenuItemClickListener { item -> onOptionsItemSelected(item) }
                }
            }
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val settingsIntent = Intent(this, SettingsActivity::class.java)
                startActivity(settingsIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun requestNeededPermissions(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                Toast.makeText(this, "I need it to reply to missed calls", Toast.LENGTH_SHORT).show()
            }
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CALL_LOG)) {
                Toast.makeText(this, "I need it to send the sms", Toast.LENGTH_SHORT).show()
            }
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
                Toast.makeText(this, "I need it to send the sms", Toast.LENGTH_LONG).show()
            }
        }

        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CALL_LOG, Manifest.permission.SEND_SMS), 1)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            1  -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "READ_PHONE_STATE perm granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "READ_PHONE_STATE perm NOT granted", Toast.LENGTH_SHORT).show()
                    // disable on/off switch
                }
                if (grantResults.isNotEmpty() && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "READ_CALL_LOG perm granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "READ_CALL_LOG perm NOT granted", Toast.LENGTH_SHORT).show()
                    // disable on/off switch
                }
                if (grantResults.isNotEmpty() && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "SEND_SMS perm granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "SEND_SMS perm NOT granted", Toast.LENGTH_SHORT).show()
                    // disable on/off switch
                }
            }

        }
    }


}