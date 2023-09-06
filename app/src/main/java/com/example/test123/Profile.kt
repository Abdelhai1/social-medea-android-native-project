package com.example.test123

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Profile : AppCompatActivity() {
    private lateinit var pimg : ImageView
    private lateinit var name : TextView
    private lateinit var postB : Button
    private var auth : FirebaseAuth = FirebaseAuth.getInstance()
    private  var db : FirebaseFirestore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        pimg = findViewById(R.id.imageView9)
        name= findViewById(R.id.textView3)
        postB = findViewById(R.id.button3)
        val id = auth.currentUser!!.uid
        db.collection("users").document(id).get().addOnCompleteListener {
            Glide.with(this).load(it.result!!.data?.getValue("image").toString().trim()).into(pimg)
            name.setText(it.result!!.data?.getValue("name").toString().trim())
        }.addOnFailureListener {
            Toast.makeText(this, "error in data base!", Toast.LENGTH_SHORT).show()
        }
        postB.setOnClickListener {
            intent = Intent(this,Createpost::class.java)
            startActivity(intent)
        }

    }
}