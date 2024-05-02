package com.example.goaltracker

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.goaltracker.databinding.ActivityDeleteBinding
import com.example.goaltracker.databinding.ActivityFiltersBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class Filters : AppCompatActivity() {
    private lateinit var binding: ActivityFiltersBinding
    private lateinit var firebaseData: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFiltersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sortId = intent.getIntExtra("sortId", 0)
        var filterId: Int = 0

        binding.VallButton.setOnClickListener {
            val intent = Intent(this@Filters, ViewAll::class.java)
            intent.putExtra("filterId", 1)
            startActivity(intent)
        }

        binding.VTodButton.setOnClickListener {
            val intent = Intent(this@Filters, ViewAll::class.java)
            intent.putExtra("filterId", 2)
            startActivity(intent)
        }

        binding.VFutButton.setOnClickListener {
            val intent = Intent(this@Filters, ViewAll::class.java)
            intent.putExtra("filterId", 3)
            startActivity(intent)
        }

        binding.AlphButton.setOnClickListener {
            if (sortId <= 1){
                filterId = 11
            }else if (sortId ==2) {
                filterId = 12
            }else if (sortId ==3) {
                filterId = 13
            }
            val intent = Intent(this@Filters, ViewAll::class.java)
            intent.putExtra("filterId", filterId)
            startActivity(intent)
        }

        binding.DateButton.setOnClickListener {
            if (sortId <= 1){
                filterId = 21
            }else if (sortId ==2) {
                filterId = 22
            }else if (sortId ==3) {
                filterId = 23
            }
            val intent = Intent(this@Filters, ViewAll::class.java)
            intent.putExtra("filterId", filterId)
            startActivity(intent)
        }

    }
}