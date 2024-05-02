package com.example.goaltracker

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.goaltracker.data.Goal
import com.example.goaltracker.databinding.ActivityDeleteBinding
import com.example.goaltracker.databinding.ActivitySubBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Sub : AppCompatActivity() {
    private lateinit var binding: ActivitySubBinding
    private lateinit var firebaseData: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseData = FirebaseDatabase.getInstance().reference
        firebaseAuth = FirebaseAuth.getInstance()

        val goalId = intent.getStringExtra("goalId")

        if (goalId != null) {
            firebaseData.child("Goal").child(firebaseAuth.currentUser?.uid ?: "").child(goalId)
                .get().addOnSuccessListener { dataSnapshot ->
                    if (dataSnapshot.exists()) {
                        val goal = dataSnapshot.getValue(Goal::class.java)
                        if (goal != null) {
                            // Populate the views with goal details
                            binding.apply {
                                textgoal.setText(goal.title)
                            }
                        }
                    }
                }.addOnFailureListener {
                    Toast.makeText(this@Sub, "Failed to load goal details", Toast.LENGTH_SHORT).show()
                }
        }

        binding.YesButton.setOnClickListener{
            val intent = Intent(this@Sub, AddSubGoal::class.java)
            intent.putExtra("goalId", goalId)
            startActivity(intent)

        }

        binding.NoButton.setOnClickListener{
            val intent = Intent(this@Sub, ViewToday::class.java)
            startActivity(intent)
        }
    }
}