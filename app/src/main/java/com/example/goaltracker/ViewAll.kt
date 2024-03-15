package com.example.goaltracker

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goaltracker.Adapter.GoalAdapter
import com.example.goaltracker.data.Goal
import com.example.goaltracker.databinding.ActivityViewAllBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ViewAll : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var goalAdapter: GoalAdapter
    private lateinit var binding: ActivityViewAllBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewAllBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth and Database
        databaseReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()


        // Setup RecyclerView
        getGoals { goalList ->
            goalAdapter = GoalAdapter(goalList) { goal ->
                val intent = Intent(this@ViewAll, Details::class.java)
                intent.putExtra("goalId", goal.id)
                startActivity(intent)
                Toast.makeText(this@ViewAll, "Clicked on goal: ${goal.title}", Toast.LENGTH_SHORT).show()
            }

            binding.recyclerViewMenu.adapter = goalAdapter
            binding.recyclerViewMenu.layoutManager = LinearLayoutManager(this@ViewAll)

            binding.GoBack.setOnClickListener {
                val intent = Intent(this@ViewAll, HomePage::class.java)
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

                // Invoke the callback with the goalList
                callback(goalList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
            }
        })
    }

}