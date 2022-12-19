package com.example.wifitrilateration.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.glebalekseevjk.wifitrilateration.R

class PermissionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)
    println("^^^^^^^^^^^ PermissionActivity onCreate")
        val extras = intent.extras
        val permission = extras?.getString(PERMISSION_TYPE)
        if (permission != null){
            ActivityCompat.requestPermissions(this,
                arrayOf(permission),
                1)
            finish()
        } else {
            throw RuntimeException("PermissionActivity bad extras")
        }
    }

    companion object {
        fun getIntent(context: Context, permission: String): Intent {
            val intent = Intent(context, PermissionActivity::class.java)
            intent.putExtra(PERMISSION_TYPE, permission)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            return intent
        }

        private const val PERMISSION_TYPE = "permission_type"
    }
}