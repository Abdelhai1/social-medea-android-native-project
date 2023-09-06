package com.example.test123

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private var auth : FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var loginButton: Button
    private lateinit var signupButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginButton = findViewById(R.id.button)
        signupButton = findViewById(R.id.button2)

        loginButton.setOnClickListener {
            intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
        signupButton.setOnClickListener {
            intent = Intent(this,SignupActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        if(auth.currentUser != null) {
            intent = Intent(this,HomeActivity::class.java)
            startActivity(intent)
            finish()
        }}
}