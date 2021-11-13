package com.funnyautoreply.views

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import android.view.Menu
import android.view.MenuItem
import android.view.SubMenu
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.funnyautoreply.adapter.MessageAdapter
import com.funnyautoreply.data.Message
import com.funnyautoreply.data.SentMessagesDatabase
import com.funnyautoreply.databinding.ActivityMainBinding
import kotlin.concurrent.thread
import androidx.recyclerview.widget.DividerItemDecoration
import android.widget.*
import com.funnyautoreply.R


class HistoryViewActivity :  AppCompatActivity(), MessageAdapter.MessageSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: SentMessagesDatabase
    private lateinit var adapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        database = SentMessagesDatabase.getDatabase(applicationContext)
        requestNeededPermissions()

        initRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
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
            R.id.action_refresh -> {
                loadItemsInBackground()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initRecyclerView() {
        adapter = MessageAdapter(this)
        binding.rvMain.layoutManager = LinearLayoutManager(this)
        binding.rvMain.adapter = adapter
        val dividerItemDecoration = DividerItemDecoration(
            binding.rvMain.getContext(),
            (binding.rvMain.layoutManager as LinearLayoutManager).getOrientation()
        )
        binding.rvMain.addItemDecoration(dividerItemDecoration)

        loadItemsInBackground()
    }

    private fun loadItemsInBackground() {
        thread {
            var items = database.messageDao().getAll()
            runOnUiThread {
                items=items.sortedWith( compareBy { it.date }).reversed()
                adapter.update(items)
            }
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

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                Toast.makeText(this, "I need it for contacts", Toast.LENGTH_LONG).show()
            }
        }

        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CALL_LOG, Manifest.permission.SEND_SMS, Manifest.permission.READ_CONTACTS), 1)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        when (requestCode) {
            1  -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "READ_PHONE_STATE perm granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "READ_PHONE_STATE perm NOT granted", Toast.LENGTH_SHORT).show()
                    with (sharedPref.edit()) {
                        putBoolean("reply_on_off", false)
                        apply()
                    }
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
                if (grantResults.isNotEmpty() && grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "READ_CONTACTS perm granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "READ_CONTACTS perm NOT granted", Toast.LENGTH_SHORT).show()
                    // disable on/off switch
                }
            }

        }
    }

    override fun onItemSelected(item: Message?) {
        ShowJokeDialogFragment(item).show(supportFragmentManager, ShowJokeDialogFragment::class.java.simpleName)
    }


}