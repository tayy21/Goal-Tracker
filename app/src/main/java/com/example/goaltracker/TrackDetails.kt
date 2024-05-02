package com.example.goaltracker

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.goaltracker.data.Goal
import com.example.goaltracker.data.ProgressLog
import com.example.goaltracker.databinding.ActivityTrackDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.*
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.components.Description
import android.util.Log


class TrackDetails : AppCompatActivity() {
    private lateinit var binding: ActivityTrackDetailsBinding
    private lateinit var firebaseData: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseData = FirebaseDatabase.getInstance().reference
        firebaseAuth = FirebaseAuth.getInstance()


        val goalId = intent.getStringExtra("goalId")
        val userId = firebaseAuth.currentUser?.uid.toString()

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
                            getProgressForGoal()
                        }
                    }
                }.addOnFailureListener {
                    Toast.makeText(this@TrackDetails, "Failed to load goal details", Toast.LENGTH_SHORT).show()
                }
        }

        binding.BackButton.setOnClickListener {
            val intent = Intent(this@TrackDetails, TrackAll::class.java)
            intent.putExtra("goalId", goalId)
            startActivity(intent)
        }

    }

    private fun getProgressForGoal() {
        val progressList = mutableListOf<ProgressLog>()
        val goalId = intent.getStringExtra("goalId")

        firebaseData.child("Progress").child(firebaseAuth.currentUser?.uid ?: "").child(goalId.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (progressSnapshot in dataSnapshot.children) {
                    val progressId = progressSnapshot.key
                    val progressData = progressSnapshot.getValue(ProgressLog::class.java)
                    progressData?.let {
                        progressData.id = progressId
                        progressList.add(progressData)
                    }
                }
                if (progressList.isEmpty()) {
                    Toast.makeText(this@TrackDetails, "No tracking for this goal", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@TrackDetails, TrackAll::class.java)
                    startActivity(intent)
                } else {
                    displayLineChart(progressList)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("FirebaseError", "Error: ${databaseError.message}")
            }
        })
    }

    private fun displayLineChart(progressList: List<ProgressLog>) {
        val entries = mutableListOf<Entry>()

        // Populate entries
        progressList.forEachIndexed { index, progressData ->
            val entry = Entry(index.toFloat(), progressData.progress.toFloat())
            entries.add(entry)
        }

        val dataSet = LineDataSet(entries, "Progress")
        dataSet.setDrawValues(false)

        val lineData = LineData(dataSet)
        binding.chart.data = lineData

        val xAxis = binding.chart.xAxis
        xAxis.isEnabled = false

        val yAxisLeft = binding.chart.axisLeft
        yAxisLeft.textColor = android.graphics.Color.WHITE
        yAxisLeft.gridColor = android.graphics.Color.GRAY

        val yAxisRight = binding.chart.axisRight
        yAxisRight.textColor = android.graphics.Color.WHITE
        yAxisRight.gridColor = android.graphics.Color.GRAY

        val description = Description()
        description.text = "Progress Over Time"
        binding.chart.description = description

        binding.chart.invalidate()
    }
}