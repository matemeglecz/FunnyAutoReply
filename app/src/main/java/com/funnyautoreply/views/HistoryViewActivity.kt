package com.funnyautoreply.views

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.view.Menu
import android.view.MenuItem
import android.view.SubMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.funnyautoreply.adapter.MessageAdapter
import com.funnyautoreply.data.Message
import com.funnyautoreply.data.SentMessagesDatabase
import com.funnyautoreply.databinding.HistoryActivityBinding
import kotlin.concurrent.thread
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.preference.PreferenceManager
import com.funnyautoreply.R
import com.google.android.material.snackbar.Snackbar

class HistoryViewActivity :  AppCompatActivity(), MessageAdapter.MessageSelectedListener {

    private lateinit var binding: HistoryActivityBinding
    private lateinit var database: SentMessagesDatabase
    private lateinit var adapter: MessageAdapter

    private var permissionsGranted = false
    private var activePermissionAlerts = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= HistoryActivityBinding.inflate(layoutInflater)
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
                if(!permissionsGranted) {
                    makeMissingPermissionSnackbar()
                } else {
                    val settingsIntent = Intent(this, SettingsActivity::class.java)
                    startActivity(settingsIntent)
                }
                true
            }
            R.id.action_refresh -> {
                loadItemsInBackground()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun makeMissingPermissionSnackbar(){
        Snackbar.make(binding.root, "Missing Permission(s)", Snackbar.LENGTH_INDEFINITE)
            .setAction("TRY AGAIN") { requestNeededPermissions() }
            .setActionTextColor(Color.RED)
            .show()
    }

    private fun initRecyclerView() {
        adapter = MessageAdapter(this)
        binding.rvMain.layoutManager = LinearLayoutManager(this)
        binding.rvMain.adapter = adapter
        val dividerItemDecoration = DividerItemDecoration(
            binding.rvMain.context,
            (binding.rvMain.layoutManager as LinearLayoutManager).orientation
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
        checkPermission(Manifest.permission.READ_PHONE_STATE, getString(R.string.read_phone_state_rationale))
        checkPermission(Manifest.permission.READ_CALL_LOG, getString(R.string.read_call_log_rationale))
        checkPermission(Manifest.permission.SEND_SMS, getString(R.string.send_sms_rationale))
        checkPermission(Manifest.permission.READ_CONTACTS, getString(R.string.read_contacts_rationale))

        if(activePermissionAlerts == 0)
            requestAllPermissions()
    }

    private fun requestAllPermissions(){
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CALL_LOG, Manifest.permission.SEND_SMS, Manifest.permission.READ_CONTACTS), 1)
    }

    private fun checkPermission(permission : String, rationaleText : String){
        if (ContextCompat.checkSelfPermission(this,
                permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                activePermissionAlerts++
                AlertDialog.Builder(this)
                    .setTitle(permission.split('.')[2])
                    .setMessage(rationaleText)
                    .setPositiveButton("OK"
                    ) { _, _ ->
                        activePermissionAlerts--
                        if (activePermissionAlerts == 0)
                            requestAllPermissions()
                    }
                    .setCancelable(false)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)

        when (requestCode) {
            1 -> {
                for(result in grantResults){
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        permissionsGranted = false
                        with (sharedPref.edit()) {
                            putBoolean("reply_on_off", false)
                            apply()
                        }
                        makeMissingPermissionSnackbar()
                        return
                    }
                }
                if(grantResults.isNotEmpty()) {
                    permissionsGranted = true
                }
            }
        }
    }

    override fun onItemSelected(item: Message?) {
        ShowJokeDialogFragment(item).show(supportFragmentManager, ShowJokeDialogFragment::class.java.simpleName)
    }


}