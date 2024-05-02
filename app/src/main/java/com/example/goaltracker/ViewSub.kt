package com.example.goaltracker

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goaltracker.Adapter.SGoalAdapter
import com.example.goaltracker.Adapter.SubGoalAdapter
import com.example.goaltracker.data.Goal
import com.example.goaltracker.databinding.ActivityViewSubBinding
import com.example.goaltracker.databinding.ActivityViewTodayBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.goaltracker.data.SubGoal
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

class ViewSub : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var goalAdapter: SubGoalAdapter
    private lateinit var binding: ActivityViewSubBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewSubBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        val goalId = intent.getStringExtra("goalId")

        if (goalId != null) {
            databaseReference.child("Goal").child(auth.currentUser?.uid ?: "").child(goalId)
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
                    Toast.makeText(this@ViewSub, "Failed to load goal details", Toast.LENGTH_SHORT).show()
                }
        }

        getSubGoals { goalList ->
            goalAdapter = SubGoalAdapter(goalList) { goal ->
                val intent = Intent(this@ViewSub, SubDetails::class.java)
                intent.putExtra("goalId", goalId)
                intent.putExtra("subGoalId", goal.id)
                startActivity(intent)
                Toast.makeText(this@ViewSub, "Clicked on goal: ${goal.title}", Toast.LENGTH_SHORT).show()
            }

            binding.recyclerViewMenu.adapter = goalAdapter
            binding.recyclerViewMenu.layoutManager = LinearLayoutManager(this@ViewSub)

            binding.GoBack.setOnClickListener {
                val intent = Intent(this@ViewSub, ViewToday::class.java)
                startActivity(intent)
            }

            binding.AddGoal.setOnClickListener {
                val intent = Intent(this@ViewSub, AddSubGoal::class.java)
                intent.putExtra("goalId", goalId)
                startActivity(intent)
            }
        }

    }

    private fun getSubGoals(callback: (List<SubGoal>) -> Unit) {
        val subGoalList = mutableListOf<SubGoal>()
        val goalId = intent.getStringExtra("goalId")

        databaseReference.child("SubGoal").child(auth.currentUser?.uid ?: "").child(goalId.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (subGoalSnapshot in dataSnapshot.children) {
                        val subGoal = subGoalSnapshot.getValue(SubGoal::class.java)
                        val key = subGoalSnapshot.key
                        subGoal?.let {
                            it.id = key
                            subGoalList.add(it)
                        }
                    }
                    if (subGoalList.isEmpty()) {
                        val intent = Intent(this@ViewSub, Sub::class.java)
                        intent.putExtra("goalId", goalId)
                        startActivity(intent)
                    } else {
                        // If there are sub-goals, invoke callback with the sub-goal list
                        callback(subGoalList)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error
                }
            })
    }
}