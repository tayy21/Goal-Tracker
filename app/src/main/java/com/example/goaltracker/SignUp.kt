package com.example.goaltracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.goaltracker.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUp : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.SignUpButton.setOnClickListener {
            val email = binding.email.text.toString()
            val pass = binding.password.text.toString()
            val pass2 = binding.password2.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && pass2.isNotEmpty()) {
                if (pass == pass2) {
                    firebaseAuth.createUserWithEmailAndPassword(email , pass).addOnCompleteListener{
                        if (it.isSuccessful) {
                                val intent = Intent(
                                    this@SignUp,
                                    SignIn::class.java
                                )
                                startActivity(intent)
                            Toast.makeText(this , "successful" , Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this , "unsuccessful" , Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(this , "password is not matching" , Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this , "fill in all fields" , Toast.LENGTH_SHORT).show()
            }
        }

        val startButton = findViewById<Button>(R.id.LoginButton)

        // Set up click listener for the start button
        startButton.setOnClickListener { // When the button is clicked, start the SignInActivity
            val intent = Intent(
                this@SignUp,
                SignIn::class.java
            )
            startActivity(intent)
        }
    }
}