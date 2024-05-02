package com.example.goaltracker

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.goaltracker.data.Goal
import com.example.goaltracker.databinding.ActivityAddTaskBinding
import com.example.goaltracker.databinding.ActivityTrackBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import android.widget.SeekBar
import com.example.goaltracker.data.ProgressLog

class Track : AppCompatActivity() {
    private lateinit var binding: ActivityTrackBinding
    private lateinit var firebaseData: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseData = FirebaseDatabase.getInstance().reference
        firebaseAuth = FirebaseAuth.getInstance()

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val todayDate = dateFormat.format(calendar.time)
        binding.editTextDate.hint = todayDate

        val goalId = intent.getStringExtra("goalId")

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Calculate percentage
                val percentage = progress * 100 / seekBar!!.max
                // Update TextView with percentage
                binding.tvUpdate.text = "$percentage%"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Not needed, but must implement
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Not needed, but must implement
            }
        })

        if (goalId != null) {
            firebaseData.child("Goal").child(firebaseAuth.currentUser?.uid ?: "").child(goalId)
                .get().addOnSuccessListener { dataSnapshot ->
                    if (dataSnapshot.exists()) {
                        val goal = dataSnapshot.getValue(Goal::class.java)
                        if (goal != null) {
                            // Populate the views with goal details
                            binding.apply {
                                textgoal.setText(goal.title)
                                goalDesc.setText(goal.description)
                            }
                        }
                    }
                }.addOnFailureListener {
                    Toast.makeText(this@Track, "Failed to load goal details", Toast.LENGTH_SHORT).show()
                }
        }

        binding.SaveButton.setOnClickListener {
            val userId = firebaseAuth.currentUser?.uid.toString()
            val progress = binding.seekBar.progress
            val date = if (binding.editTextDate.text.isEmpty()) {
                binding.editTextDate.hint.toString()
            } else {
                binding.editTextDate.text.toString()
            }



            if (progress != null && date.isNotEmpty()) {
                val progress = ProgressLog(progress, date)
                val newGoalRef = firebaseData.child("Progress").child(userId).child(goalId.toString()).push()
                newGoalRef.setValue(progress)


                Toast.makeText(this, "Goal successfully saved", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Unsuccessful, please try again", Toast.LENGTH_SHORT).show()
            }
        }
    }
}