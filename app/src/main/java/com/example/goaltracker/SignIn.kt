package com.example.goaltracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast;
import com.example.goaltracker.databinding.ActivitySignInBinding
import com.example.goaltracker.databinding.ActivitySignUpBinding


class SignIn : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.LoginButton.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            // Check if email and password are not empty
            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Attempt to sign in with email and password
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this@SignIn, OnCompleteListener<AuthResult> { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this@SignIn, "Authentication succeeded.", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@SignIn, HomePage::class.java)
                            startActivity(intent)
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(this@SignIn, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
            } else {
                Toast.makeText(this@SignIn, "Please enter email and password.", Toast.LENGTH_SHORT).show()
            }
        }

        val startButton = binding.SignUpButton
        startButton.setOnClickListener {
            val intent = Intent(this@SignIn, SignUp::class.java)
            startActivity(intent)
        }
    }
}