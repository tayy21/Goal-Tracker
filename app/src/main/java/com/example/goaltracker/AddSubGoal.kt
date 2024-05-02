package com.example.goaltracker

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.goaltracker.data.Goal
import com.example.goaltracker.data.SubGoal
import com.example.goaltracker.databinding.ActivityAddSubGoalBinding
import com.example.goaltracker.databinding.ActivityAddTaskBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

class AddSubGoal : AppCompatActivity() {
    private lateinit var binding: ActivityAddSubGoalBinding
    private lateinit var firebaseData: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseImg: FirebaseStorage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSubGoalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseData = FirebaseDatabase.getInstance().reference
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseImg = FirebaseStorage.getInstance()

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val todayDate = dateFormat.format(calendar.time)
        binding.editTextDate.hint = todayDate

        binding.AddImage.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2)
        }

        binding.BackButton.setOnClickListener {
            finish()
        }

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
                    Toast.makeText(this@AddSubGoal, "Failed to load goal details", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 2 && data != null && data.data != null) {
            val goalId = intent.getStringExtra("goalId")
            val selectedImageUri = data.data
            val userId = firebaseAuth.currentUser?.uid ?: ""
            val filename = UUID.randomUUID().toString()
            val imagesRef = firebaseImg.reference.child("images").child(userId).child(filename)
            binding.AddButton.setOnClickListener {
                if (selectedImageUri != null) {
                    val uploadTask = imagesRef.putFile(selectedImageUri)
                    uploadTask.continueWithTask { task ->
                        if (!task.isSuccessful) {
                            task.exception?.let {
                                throw it
                            }
                        }
                        imagesRef.downloadUrl
                    }.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val imageUrl = task.result.toString()

                            val title = binding.goal.text.toString()
                            val desc = binding.goalDesc.text.toString()
                            val userId = firebaseAuth.currentUser?.uid.toString()
                            val deadline = if (binding.editTextDate.text.isEmpty()) {
                                binding.editTextDate.hint.toString()
                            } else {
                                binding.editTextDate.text.toString()
                            }

                            if (title.isNotEmpty() && desc.isNotEmpty()) {
                                val subgoal = SubGoal(title, desc, deadline, userId, imageUrl)
                                val newGoalRef = firebaseData.child("SubGoal").child(userId).child(goalId.toString()).push()
                                newGoalRef.setValue(subgoal)

                                val intent = Intent(this@AddSubGoal, ViewSub::class.java)
                                intent.putExtra("goalId", goalId)
                                startActivity(intent)

                                Toast.makeText(this, "Goal successfully saved", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, "Unsuccessful, please try again", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            // Handle failures
                            Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    // Handle case when no image is selected
                    Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}