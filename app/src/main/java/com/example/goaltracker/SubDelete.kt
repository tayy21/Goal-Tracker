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
import com.example.goaltracker.databinding.ActivitySubDeleteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SubDelete : AppCompatActivity() {
    private lateinit var binding: ActivitySubDeleteBinding
    private lateinit var firebaseData: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubDeleteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseData = FirebaseDatabase.getInstance().reference
        firebaseAuth = FirebaseAuth.getInstance()

        val goalId = intent.getStringExtra("goalId")
        val subGoalId = intent.getStringExtra("subGoalId")

        if (goalId != null) {
            firebaseData.child("SubGoal").child(firebaseAuth.currentUser?.uid ?: "").child(goalId).child(subGoalId.toString())
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
                    Toast.makeText(this@SubDelete, "Failed to load goal details", Toast.LENGTH_SHORT).show()
                }
        }

        binding.YesButton.setOnClickListener{
            val userId = firebaseAuth.currentUser?.uid.toString()
            val newDelRef = firebaseData.child("SubGoal").child(userId).child(goalId.toString()).child(subGoalId.toString())
            newDelRef.removeValue()

            val intent = Intent(this@SubDelete, ViewToday::class.java)
            startActivity(intent)
        }

        binding.NoButton.setOnClickListener{
            finish()
        }
    }
}