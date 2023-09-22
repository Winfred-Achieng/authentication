package com.example.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.authentication.databinding.ActivityResetPasswdBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ResetPasswdActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResetPasswdBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth =Firebase.auth


        binding.resetBtn.setOnClickListener {
            val email = binding.etResetPasswd.text.toString()

            if (email.isNotEmpty()) {
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Password reset email sent successfully
                            Toast.makeText(applicationContext, "Password reset email sent to $email", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this,LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            // Password reset email failed to send
                            Toast.makeText(applicationContext, "Password reset failed. Please check your email address.", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                // Email field is empty
                Toast.makeText(applicationContext,
                    "Please enter your email address", Toast.LENGTH_SHORT).show()
            }
        }
    }
}