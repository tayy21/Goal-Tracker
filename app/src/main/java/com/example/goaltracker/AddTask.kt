package com.example.goaltracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.goaltracker.data.Goal
import com.example.goaltracker.databinding.ActivityAddTaskBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class AddTask : AppCompatActivity() {
    private lateinit var binding: ActivityAddTaskBinding
    private lateinit var firebaseData: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseData = FirebaseDatabase.getInstance().reference
        firebaseAuth = FirebaseAuth.getInstance()

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val todayDate = dateFormat.format(calendar.time)


        binding.editTextDate.hint = todayDate

        binding.AddButton.setOnClickListener{
            val title = binding.goal.text.toString()
            val desc = binding.goalDesc.text.toString()
            val cata = binding.editCategory.text.toString()
            val userId = firebaseAuth.currentUser?.uid.toString()
            val deadline = if (binding.editTextDate.text.isEmpty()) {
                binding.editTextDate.hint.toString()
            } else {
                binding.editTextDate.text.toString()
            }

            if (title.isNotEmpty() && desc.isNotEmpty() && cata.isNotEmpty()){
                val goal = Goal(title,desc,deadline,cata,userId)
                val newGoalRef = firebaseData.child("Goal").child(userId).push()
                newGoalRef.setValue(goal)

                val intent = Intent(this@AddTask, HomePage::class.java)
                startActivity(intent)

                Toast.makeText(this, "Goal successfully saved", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Unsuccessful, please try again", Toast.LENGTH_SHORT).show()
            }

        }

    }
}