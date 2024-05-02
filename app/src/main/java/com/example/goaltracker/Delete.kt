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
import com.example.goaltracker.databinding.ActivityTrackBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Delete : AppCompatActivity() {
    private lateinit var binding: ActivityDeleteBinding
    private lateinit var firebaseData: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteBinding.inflate(layoutInflater)
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
                    Toast.makeText(this@Delete, "Failed to load goal details", Toast.LENGTH_SHORT).show()
                }
        }

        binding.YesButton.setOnClickListener{
            val userId = firebaseAuth.currentUser?.uid.toString()
            val newDelRef = firebaseData.child("Goal").child(userId).child(goalId.toString())
            newDelRef.removeValue()

            val intent = Intent(this@Delete, ViewAll::class.java)
            startActivity(intent)
        }

        binding.NoButton.setOnClickListener{
            finish()
        }
    }
}