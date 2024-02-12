package com.example.goaltracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.goaltracker.R.layout.activity_main)

        // Find the start button by its ID
        val startButton = findViewById<Button>(R.id.start)

        // Set up click listener for the start button
        startButton.setOnClickListener { // When the button is clicked, start the SignInActivity
            val intent = Intent(
                this@MainActivity,
                SignIn::class.java
            )
            startActivity(intent)
        }
    }
}
