package com.example.goaltracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.goaltracker.databinding.ActivityAddTaskBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class AddTask : AppCompatActivity() {
    private lateinit var binding: ActivityAddTaskBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val todayDate = dateFormat.format(calendar.time)

        binding.editTextDate.hint = todayDate

    }
}