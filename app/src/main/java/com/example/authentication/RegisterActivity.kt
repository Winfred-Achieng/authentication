package com.example.authentication

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.authentication.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth




        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPasswd.text.toString()
            val confirmPassword = binding.etConfirmPasswd.text.toString()

            if (name.isEmpty()) { binding.etName.error = "Name is required"
            } else if (email.isEmpty() || !isValidEmail(email)) { binding.etEmail.error = "Valid email is required"
            } else if (password.isEmpty() || !isPasswordValid(password)) { binding.etPasswd.error = "Password is required (min 6 digits)"
            } else if (confirmPassword.isEmpty()) { binding.etConfirmPasswd.error = "Confirm Password is required"
            } else if (password != confirmPassword) {
                binding.etConfirmPasswd.error = "Passwords do not match"
            }else if (password != confirmPassword){binding.etConfirmPasswd.error ="Passwords do not match"
            } else {


                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                            Toast.makeText(baseContext, "Registration successful.", Toast.LENGTH_SHORT,).show()

                            // set  the user's display name here
                            val user = auth.currentUser
                            user?.sendEmailVerification()
                                ?.addOnCompleteListener(this) { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(this, "Verification email sent. Please check your inbox.", Toast.LENGTH_LONG).show()
                                    } else {
                                        Toast.makeText(this, "Failed to send verification email. Please try again.", Toast.LENGTH_LONG).show()
                                    }
                                }

                            val profileUpdates = UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build()
                            user?.updateProfile(profileUpdates)
                                ?.addOnCompleteListener{ profileUpdateTask ->
                                    if (profileUpdateTask.isSuccessful) {
                                        Log.d(TAG, "User profile updated with display name.")
                                    } else {
                                        Log.w(TAG, "Error updating user profile with display name.", profileUpdateTask.exception)
                                    }
                                }
                            val intent =Intent(this,LoginActivity::class.java)
                            startActivity(intent)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(baseContext, "Registration failed.", Toast.LENGTH_SHORT,).show()
                            val intent =Intent(this,RegisterActivity::class.java)
                            startActivity(intent)
                        }
                    }
            }

        }

    }
    fun onClickText2(view: View){
        val intent = Intent(this, LoginActivity::class.java)
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