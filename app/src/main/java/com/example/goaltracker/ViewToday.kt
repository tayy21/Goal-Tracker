package com.example.goaltracker

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goaltracker.Adapter.SGoalAdapter
import com.example.goaltracker.data.Goal
import com.example.goaltracker.databinding.ActivityViewAllBinding
import com.example.goaltracker.databinding.ActivityViewTodayBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import java.text.SimpleDateFormat

class ViewToday : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var goalAdapter: SGoalAdapter
    private lateinit var binding: ActivityViewTodayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewTodayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth and Database
        databaseReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        // Setup RecyclerView
        getGoals { goalList ->
                goalAdapter = SGoalAdapter(goalList) { goal ->
                    val intent = Intent(this@ViewToday, ViewSub::class.java)
                    intent.putExtra("goalId", goal.id)
                    startActivity(intent)
                    Toast.makeText(this@ViewToday, "Clicked on goal: ${goal.title}", Toast.LENGTH_SHORT).show()
                }

                binding.recyclerViewMenu.adapter = goalAdapter
                binding.recyclerViewMenu.layoutManager = LinearLayoutManager(this@ViewToday)

                binding.GoBack.setOnClickListener {
                    val intent = Intent(this@ViewToday, HomePage::class.java)
                    startActivity(intent)
                }
        }
    }

    private fun getGoals(callback: (List<Goal>) -> Unit) {
        val goalList = mutableListOf<Goal>()

        databaseReference.child("Goal").child(auth.currentUser?.uid ?: "").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (goalSnapshot in dataSnapshot.children) {
                    val key = goalSnapshot.key
                    val goal = goalSnapshot.getValue(Goal::class.java)
                    goal?.let {
                        it.id = key
                        goalList.add(it)
                    }
                }
                callback(goalList)
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }



}