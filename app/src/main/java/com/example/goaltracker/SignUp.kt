package com.example.goaltracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.goaltracker.data.Goal
import com.example.goaltracker.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.example.goaltracker.data.User
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseData: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseData = FirebaseDatabase.getInstance().reference

        binding.SignUpButton.setOnClickListener {
            val email = binding.email.text.toString()
            val pass = binding.password.text.toString()
            val pass2 = binding.password2.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && pass2.isNotEmpty()) {
                if (pass == pass2) {
                    firebaseAuth.createUserWithEmailAndPassword(email , pass).addOnCompleteListener{
                        if (it.isSuccessful) {
                            val userId = firebaseAuth.currentUser?.uid
                            val email = firebaseAuth.currentUser?.email

                            if (userId != null && email != null) {
                                val user = User(userId, email)
                                val userRef = firebaseData.child("users").child(userId)
                                userRef.child("email").setValue(user)
                            }

                            val intent = Intent(this@SignUp, SignIn::class.java)
                            startActivity(intent)

                            Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Unsuccessful", Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(this , "password is not matching" , Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this , "fill in all fields" , Toast.LENGTH_SHORT).show()
            }
        }

        val startButton = binding.LoginButton

        startButton.setOnClickListener {
            val intent = Intent(
                this@SignUp,
                SignIn::class.java
            )
            startActivity(intent)
        }
    }
}