package com.example.goaltracker

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.goaltracker.data.Goal
import com.example.goaltracker.data.SubGoal
import com.example.goaltracker.databinding.ActivityDetailsBinding
import com.example.goaltracker.databinding.ActivitySubDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SubDetails : AppCompatActivity() {
    private lateinit var binding: ActivitySubDetailsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        val goalId = intent.getStringExtra("goalId")
        val subGoalId = intent.getStringExtra("subGoalId")

        if (goalId != null) {
            databaseReference.child("SubGoal").child(auth.currentUser?.uid ?: "").child(goalId).child(subGoalId.toString())
                .get().addOnSuccessListener { dataSnapshot ->
                    if (dataSnapshot.exists()) {
                        val goal = dataSnapshot.getValue(SubGoal::class.java)
                        if (goal != null) {
                            // Populate the views with goal details
                            binding.apply {
                                textgoal.setText(goal.title)
                                editTextDate.setText(goal.deadline)
                                goalDesc.setText(goal.description)

                                // Load the image using Glide
                                if (!goal.imageUrl.isNullOrEmpty()) {
                                    Glide.with(this@SubDetails)
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
                            val userId = auth.currentUser?.uid.toString()
                            val deadline = binding.editTextDate.text.toString()
                            val imageUrl = if (goal?.imageUrl.isNullOrEmpty()) {
                                ""
                            } else {
                                goal!!.imageUrl.toString()
                            }


                            if (title.isNotEmpty() && desc.isNotEmpty()) {
                                val subgoal = SubGoal(title, desc, deadline, userId, imageUrl)
                                val newGoalRef = databaseReference.child("SubGoal").child(userId).child(goalId.toString()).child(subGoalId.toString())
                                newGoalRef.setValue(subgoal)


                                Toast.makeText(this, "Sub Goal successfully saved", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, "Unsuccessful, please try again", Toast.LENGTH_SHORT).show()
                            }
                        }

                        binding.DelButton.setOnClickListener{
                            val intent = Intent(this@SubDetails, SubDelete::class.java)
                            intent.putExtra("goalId", goalId)
                            intent.putExtra("subGoalId", subGoalId)
                            startActivity(intent)
                        }

                        binding.BackButton.setOnClickListener {
                            val intent = Intent(this@SubDetails, ViewSub::class.java)
                            intent.putExtra("goalId", goalId)
                            intent.putExtra("subGoalId", subGoalId)
                            startActivity(intent)
                        }


                    }
                }.addOnFailureListener {
                    Toast.makeText(this@SubDetails, "Failed to load goal details", Toast.LENGTH_SHORT).show()
                }
        }
    }
}