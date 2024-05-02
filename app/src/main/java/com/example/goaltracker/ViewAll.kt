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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
        val filterId = intent.getIntExtra("filterId", 0)
        var sortId: Int = 0


        val goalListFunction: (List<Goal>) -> Unit = { goalList ->
            goalAdapter = GoalAdapter(goalList) { goal ->
                val intent = Intent(this@ViewAll, Details::class.java)
                intent.putExtra("goalId", goal.id)
                startActivity(intent)
                Toast.makeText(
                    this@ViewAll,
                    "Clicked on goal: ${goal.title}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            binding.recyclerViewMenu.adapter = goalAdapter
            binding.recyclerViewMenu.layoutManager = LinearLayoutManager(this@ViewAll)
        }

        if (filterId <= 1) {
            getGoals(goalListFunction)
            sortId = 1
            Toast.makeText(this@ViewAll, "showing all goals", Toast.LENGTH_SHORT).show()
        } else if (filterId == 2) {
            getTodayGoals(goalListFunction)
            sortId = 2
            Toast.makeText(this@ViewAll, "showing todays goals", Toast.LENGTH_SHORT).show()
        } else if (filterId == 3) {
            getFutureGoals(goalListFunction)
            sortId = 3
            Toast.makeText(this@ViewAll, "showing future goals", Toast.LENGTH_SHORT).show()
        } else if (filterId == 11) {
            getSortGoals(goalListFunction)
            sortId = 1
            Toast.makeText(this@ViewAll, "showing all goals/alphabetically", Toast.LENGTH_SHORT).show()
        } else if (filterId == 12) {
            getSortTodayGoals(goalListFunction)
            sortId = 2
            Toast.makeText(this@ViewAll, "showing todays goals/alphabetically", Toast.LENGTH_SHORT).show()
        } else if (filterId == 13) {
            getSortFutureGoals(goalListFunction)
            sortId = 3
            Toast.makeText(this@ViewAll, "showing future goals/alphabetically", Toast.LENGTH_SHORT).show()
        } else if (filterId == 21) {
            getSortDGoals(goalListFunction)
            sortId = 1
            Toast.makeText(this@ViewAll, "showing all goals/date", Toast.LENGTH_SHORT).show()
        } else if (filterId == 22) {
            getSortDTodayGoals(goalListFunction)
            sortId = 2
            Toast.makeText(this@ViewAll, "showing todays goals/date", Toast.LENGTH_SHORT).show()
        } else if (filterId == 23) {
            getSortDFutureGoals(goalListFunction)
            sortId = 3
            Toast.makeText(this@ViewAll, "showing future goals/date", Toast.LENGTH_SHORT).show()
        }

        binding.FilterButton.setOnClickListener {
            val intent = Intent(this@ViewAll, Filters::class.java)
            intent.putExtra("sortId", sortId)
            startActivity(intent)
        }

        binding.GoBack.setOnClickListener {
            val intent = Intent(this@ViewAll, HomePage::class.java)
            startActivity(intent)
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

    private fun getSortGoals(callback: (List<Goal>) -> Unit) {
        val goalList = mutableListOf<Goal>()

        databaseReference.child("Goal").child(auth.currentUser?.uid ?: "").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (goalSnapshot in dataSnapshot.children) {
                    val key = goalSnapshot.key
                    val goal = goalSnapshot.getValue(Goal::class.java)
                    goal?.let {
                        it.id = key
                        goalList.add(it)
                    }
                }
                val sortedList = goalList.sortedBy { it.title } // Sort the list alphabetically by goal title
                callback(sortedList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
            }
        })
    }

    private fun getSortDGoals(callback: (List<Goal>) -> Unit) {
        val goalList = mutableListOf<Goal>()

        databaseReference.child("Goal").child(auth.currentUser?.uid ?: "").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (goalSnapshot in dataSnapshot.children) {
                    val key = goalSnapshot.key
                    val goal = goalSnapshot.getValue(Goal::class.java)
                    goal?.let {
                        it.id = key
                        goalList.add(it)
                    }
                }
                val sortedList = goalList.sortedBy { it.deadline } // Sort the list alphabetically by goal title
                callback(sortedList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
            }
        })
    }

    private fun getTodayGoals(callback: (List<Goal>) -> Unit) {
        val goalList = mutableListOf<Goal>()

        databaseReference.child("Goal").child(auth.currentUser?.uid ?: "").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (goalSnapshot in dataSnapshot.children) {
                    val goal = goalSnapshot.getValue(Goal::class.java)
                    val key = goalSnapshot.key
                    if (goal != null) {
                        // Filter tasks for today
                        if (goal.deadline == getCurrentDate()) {
                            goal?.let {
                                it.id = key
                                goalList.add(it)
                            }
                        }
                    }
                }
                callback(goalList)
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    private fun getSortTodayGoals(callback: (List<Goal>) -> Unit) {
        val goalList = mutableListOf<Goal>()

        databaseReference.child("Goal").child(auth.currentUser?.uid ?: "").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (goalSnapshot in dataSnapshot.children) {
                    val goal = goalSnapshot.getValue(Goal::class.java)
                    val key = goalSnapshot.key
                    if (goal != null) {
                        // Filter tasks for today
                        if (goal.deadline == getCurrentDate()) {
                            goal?.let {
                                it.id = key
                                goalList.add(it)
                            }
                        }
                    }
                }
                val sortedList = goalList.sortedBy { it.title } // Sort the list alphabetically by goal title
                callback(sortedList)
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    private fun getSortDTodayGoals(callback: (List<Goal>) -> Unit) {
        val goalList = mutableListOf<Goal>()

        databaseReference.child("Goal").child(auth.currentUser?.uid ?: "").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (goalSnapshot in dataSnapshot.children) {
                    val goal = goalSnapshot.getValue(Goal::class.java)
                    val key = goalSnapshot.key
                    if (goal != null) {
                        // Filter tasks for today
                        if (goal.deadline == getCurrentDate()) {
                            goal?.let {
                                it.id = key
                                goalList.add(it)
                            }
                        }
                    }
                }
                val sortedList = goalList.sortedBy { it.deadline } // Sort the list alphabetically by goal title
                callback(sortedList)
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    private fun getFutureGoals(callback: (List<Goal>) -> Unit) {
        val goalList = mutableListOf<Goal>()

        databaseReference.child("Goal").child(auth.currentUser?.uid ?: "").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val currentDate = getCurrentDate()

                for (goalSnapshot in dataSnapshot.children) {
                    val goal = goalSnapshot.getValue(Goal::class.java)
                    val key = goalSnapshot.key

                    if (goal != null && goal.deadline > currentDate) {
                        goal?.let {
                            it.id = key
                            goalList.add(it)
                        }
                    }
                }
                callback(goalList)
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    private fun getSortFutureGoals(callback: (List<Goal>) -> Unit) {
        val goalList = mutableListOf<Goal>()

        databaseReference.child("Goal").child(auth.currentUser?.uid ?: "").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val currentDate = getCurrentDate()

                for (goalSnapshot in dataSnapshot.children) {
                    val goal = goalSnapshot.getValue(Goal::class.java)
                    val key = goalSnapshot.key

                    if (goal != null && goal.deadline > currentDate) {
                        goal?.let {
                            it.id = key
                            goalList.add(it)
                        }
                    }
                }
                val sortedList = goalList.sortedBy { it.title } // Sort the list alphabetically by goal title
                callback(sortedList)
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    private fun getSortDFutureGoals(callback: (List<Goal>) -> Unit) {
        val goalList = mutableListOf<Goal>()

        databaseReference.child("Goal").child(auth.currentUser?.uid ?: "").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val currentDate = getCurrentDate()

                for (goalSnapshot in dataSnapshot.children) {
                    val goal = goalSnapshot.getValue(Goal::class.java)
                    val key = goalSnapshot.key

                    if (goal != null && goal.deadline > currentDate) {
                        goal?.let {
                            it.id = key
                            goalList.add(it)
                        }
                    }
                }
                val sortedList = goalList.sortedBy { it.deadline }
                callback(sortedList)
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }


    // Utility function to get current date in yyyy-MM-dd format
    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

}