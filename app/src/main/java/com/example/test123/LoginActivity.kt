package com.example.test123

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    private lateinit var etEmail : EditText
    private lateinit var etPass : EditText
    private lateinit var loginButton: Button
    private lateinit var signUp : TextView
    private var auth : FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var pb : ProgressBar
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etEmail = findViewById(R.id.email11)
        etPass = findViewById(R.id.pass11)
        loginButton = findViewById(R.id.login1)
        signUp = findViewById(R.id.textView2)
        pb = findViewById(R.id.progressBar1)

        loginButton.setOnClickListener {
            if ( etEmail.text.isEmpty() || etPass.text.isEmpty()  ||etPass.text.length < 6 ){
                if (etPass.text.length < 6){
                    etPass.error = "password must be more than 5"
                }
                if (etEmail.text.isEmpty()){
                    etEmail.error = "Please enter an email"
                }
                if (etPass.text.isEmpty()) {
                    etPass.error = "Please enter a password"
                }}else{
                    pb.visibility = View.VISIBLE
                    auth = FirebaseAuth.getInstance()
                    auth.signInWithEmailAndPassword(etEmail.text.toString(),etPass.text.toString()).addOnSuccessListener {
                        Toast.makeText(this, "Logged in successfully", Toast.LENGTH_SHORT).show()
                        pb.visibility = View.GONE
                         intent = Intent(this,HomeActivity::class.java)
                        startActivity(intent)
                    }.addOnFailureListener { Toast.makeText(this, "Failed check your informations!", Toast.LENGTH_SHORT).show()
                    pb.visibility = View.GONE}

            }
        }
    }


}