package com.example.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.authentication.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth



        val user = Firebase.auth.currentUser
        user?.let {
            val name = it.displayName
            val email = it.email
            if (name != null){
                binding.tvName.text=name
            }
            if (email!= null){
                binding.tvEmail.text=email
            }
        }



    }

}
