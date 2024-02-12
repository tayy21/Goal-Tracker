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


class SignIn : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        mAuth = FirebaseAuth.getInstance()

        emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.password)
        loginButton = findViewById(R.id.LoginButton)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Check if email and password are not empty
            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Attempt to sign in with email and password
                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this@SignIn, OnCompleteListener<AuthResult> { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(this@SignIn, "Authentication succeeded.", Toast.LENGTH_SHORT).show()
                            // Here you can navigate to the next activity or perform any action you want.
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(this@SignIn, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
            } else {
                Toast.makeText(this@SignIn, "Please enter email and password.", Toast.LENGTH_SHORT).show()
            }
        }

        val startButton = findViewById<Button>(R.id.SignUpButton)
        startButton.setOnClickListener {
            val intent = Intent(this@SignIn, SignUp::class.java)
            startActivity(intent)
        }
    }
}