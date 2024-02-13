package com.example.goaltracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.goaltracker.databinding.ActivityHomePageBinding

class HomePage : AppCompatActivity() {
    private lateinit var binding: ActivityHomePageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.AddGoal.setOnClickListener {
            val intent = Intent(this@HomePage, AddTask::class.java)
            startActivity(intent)
        }
    }
}