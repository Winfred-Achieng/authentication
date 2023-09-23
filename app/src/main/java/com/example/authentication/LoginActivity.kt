 package com.example.authentication

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.authentication.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

 class LoginActivity : AppCompatActivity() {
     private lateinit var binding: ActivityLoginBinding
     private lateinit var auth: FirebaseAuth

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         binding = ActivityLoginBinding.inflate(layoutInflater)
         setContentView(binding.root)

         auth = Firebase.auth

         binding.btnLogin.setOnClickListener {
             val email = binding.etEmail.text.toString()
             val password = binding.etPasswd.text.toString()

             if (email.isEmpty() || !isValidEmail(email)) { binding.etEmail.error = "Valid email is required"
             } else if (password.isEmpty() || !isPasswordValid(password)) { binding.etPasswd.error = "Password is required (min 6 digits)"
             } else{

                 auth.signInWithEmailAndPassword(email, password)
                     .addOnCompleteListener(this) { task ->
                         if (task.isSuccessful) {
                             val user = auth.currentUser
                             if (user != null && user.isEmailVerified) {
                                 // User is logged in and their email is verified
                                 val intent = Intent(this, MainActivity::class.java)
                                 startActivity(intent)
                             } else {
                                 // User's email is not verified, prevent login
                                 Toast.makeText(applicationContext, "Please verify your email before logging in.", Toast.LENGTH_SHORT).show()
                             }
                         } else {
                             // If sign in fails, display a message to the user.
                             Log.w(TAG, "signInWithEmail:failure", task.exception)
                             Toast.makeText(baseContext, "Login failed.", Toast.LENGTH_SHORT,).show()
                             val intent =Intent(this,LoginActivity::class.java)
                             startActivity(intent)
                         }
                     }
             }
         }
     }

     fun onClickText (view:View){
         val intent =Intent(this,RegisterActivity::class.java)
         startActivity(intent)
     }
     fun onClickForgotPassword (view: View){
         val intent =Intent(this,ResetPasswdActivity::class.java)
         startActivity(intent)
     }

     private fun isValidEmail(email: String): Boolean {
         // Use a regular expression pattern to check for a valid email format
         val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}"
         return email.matches(emailPattern.toRegex())
     }

     private fun isPasswordValid(password: String): Boolean {
         // Implement your password validation logic here
         val minLength = 6
         //val containsUppercase = "[A-Z]".toRegex().containsMatchIn(password)
         //val containsLowercase = "[a-z]".toRegex().containsMatchIn(password)
         val containsDigit = "\\d".toRegex().containsMatchIn(password)

         // Check if password meets all requirements
         return password.length >= minLength && containsDigit
     }
 }
