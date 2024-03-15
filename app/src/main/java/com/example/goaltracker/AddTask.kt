package com.example.goaltracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.goaltracker.data.Goal
import com.example.goaltracker.databinding.ActivityAddTaskBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*
import java.util.Locale


class AddTask : AppCompatActivity() {
    private lateinit var binding: ActivityAddTaskBinding
    private lateinit var firebaseData: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseImg: FirebaseStorage


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
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

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 2 && data != null && data.data != null) {
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
                            val cata = binding.editCategory.text.toString()
                            val userId = firebaseAuth.currentUser?.uid.toString()
                            val deadline = if (binding.editTextDate.text.isEmpty()) {
                                binding.editTextDate.hint.toString()
                            } else {
                                binding.editTextDate.text.toString()
                            }

                            if (title.isNotEmpty() && desc.isNotEmpty() && cata.isNotEmpty()) {
                                val goal = Goal(title, desc, deadline, cata, userId, imageUrl)
                                val newGoalRef = firebaseData.child("Goal").child(userId).push()
                                val goalId = newGoalRef.key
                                newGoalRef.setValue(goal)

                                val intent = Intent(this@AddTask, HomePage::class.java)
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