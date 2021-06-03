package com.algebra.wheathertask.internet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.algebra.wheathertask.MainActivity
import com.algebra.wheathertask.R
import com.algebra.wheathertask.databinding.ActivityInternetConnectionBinding
import com.algebra.wheathertask.dialog.DialogExit


class InternetConnectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInternetConnectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityInternetConnectionBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        clickListener()
    }

    private fun clickListener(){
        binding.btnRetry.setOnClickListener {
            Log.d("IspisDialog", (isInternetAvailable() || verifyAvailableNetwork(this)).toString())
            if((isInternetAvailable() || verifyAvailableNetwork(this)))
                startActivity(Intent(this, MainActivity::class.java))
            else {
                val toast = Toast.makeText(this, "Check you internet connection again!", Toast.LENGTH_LONG)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            }
        }
    }

    override fun onBackPressed() {
        val dialog = DialogExit("Are you sure you want to exit?")
        dialog.show(supportFragmentManager, "Logout")
        dialog.listener = object: DialogExit.ListenerForDialog{
            override fun okPress(isPress: Boolean) {
                finishAffinity()
            }
        }
    }
}