package com.example.test123

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class HomeActivity : AppCompatActivity() {
    private lateinit var rv : RecyclerView
    private lateinit var newArray : ArrayList<PostData>
    private lateinit var adapter: PostsAdapter
    private  var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var pImage :ImageView
    private var db : FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        pImage = findViewById(R.id.imageView7)

        val id = auth.currentUser!!.uid
        db.collection("users").document(id).get().addOnCompleteListener {
            Glide.with(this).load(it.result!!.data?.getValue("image").toString().trim()).into(pImage)
        }
        pImage.setOnClickListener {
            intent = Intent(this,Profile::class.java)
            startActivity(intent)
        }
        val rec=findViewById<RecyclerView>(R.id.rv)
        rec.layoutManager=LinearLayoutManager(baseContext,RecyclerView.VERTICAL,true)


        adapter=PostsAdapter(baseContext)
        rec.adapter=adapter


    }

}