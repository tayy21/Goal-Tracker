package com.example.goaltracker

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.goaltracker.R
import com.example.goaltracker.data.Goal
import com.example.goaltracker.databinding.ActivityDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.bumptech.glide.Glide
import android.widget.Toast
import android.util.Log

class Details : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth and Database
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        // Retrieve the goal ID passed from the previous activity
        val goalId = intent.getStringExtra("goalId")

        // Fetch the goal details from Firebase using the goal ID
        if (goalId != null) {
            databaseReference.child("Goal").child(auth.currentUser?.uid ?: "").child(goalId)
                .get().addOnSuccessListener { dataSnapshot ->
                    if (dataSnapshot.exists()) {
                        val goal = dataSnapshot.getValue(Goal::class.java)
                        if (goal != null) {
                            // Populate the views with goal details
                            binding.apply {
                                textgoal.setText(goal.title)
                                editCategory.setText(goal.category)
                                editTextDate.setText(goal.deadline)
                                goalDesc.setText(goal.description)

                                // Load the image using Glide
                                if (!goal.imageUrl.isNullOrEmpty()) {
                                    Glide.with(this@Details)
                                        .load(goal.imageUrl)
                                        .placeholder(R.drawable.placeholder_image)
                                        .into(imageGoal)
                                } else {
                                    // Set default placeholder image if imageUrl is null or empty
                                    imageGoal.setImageResource(R.drawable.placeholder_image)
                                }
                            }
                        }
                        binding.SaveButton.setOnClickListener {
                            val title = binding.textgoal.text.toString()
                            val desc = binding.goalDesc.text.toString()
                            val cata = binding.editCategory.text.toString()
                            val userId = auth.currentUser?.uid.toString()
                            val deadline = binding.editTextDate.text.toString()
                            val imageUrl = if (goal?.imageUrl.isNullOrEmpty()) {
                                ""
                            } else {
                                goal!!.imageUrl.toString()
                            }


                            if (title.isNotEmpty() && desc.isNotEmpty() && cata.isNotEmpty()) {
                                val goal = Goal(title, desc, deadline, cata, userId, imageUrl)
                                val newGoalRef = databaseReference.child("Goal").child(userId).child(goalId.toString())
                                newGoalRef.setValue(goal)


                                Toast.makeText(this, "Goal successfully saved", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, "Unsuccessful, please try again", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }.addOnFailureListener {
                    Toast.makeText(this@Details, "Failed to load goal details", Toast.LENGTH_SHORT).show()
                }
        }



    }
}