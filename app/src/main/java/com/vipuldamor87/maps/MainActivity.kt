package com.vipuldamor87.maps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var button = findViewById<Button>(R.id.btnMaps)
        button.setOnClickListener {
            var intent = Intent(this,MapsActivity::class.java)
            startActivity(intent)
        }
    }
}