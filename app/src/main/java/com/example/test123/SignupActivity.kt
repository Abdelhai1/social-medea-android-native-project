package com.example.test123

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.text.SimpleDateFormat


class SignupActivity : AppCompatActivity() {
    private lateinit var st : FirebaseStorage
    private lateinit var img : ImageView
    private lateinit var auth: FirebaseAuth
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var signup: Button
    private lateinit var etUsername: EditText
    private lateinit var etConfirmPass: EditText
    private lateinit var login: TextView
    private lateinit var db : FirebaseFirestore
    private lateinit var pb : ProgressBar

    private var imageUri : Uri?=null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        pb = findViewById(R.id.progressBar)
        etEmail=findViewById(R.id.email1)
        etPassword=findViewById(R.id.pass1)
        img = findViewById(R.id.imageView8)
        etUsername=findViewById(R.id.name1)
        etConfirmPass=findViewById(R.id.passc1)
        signup=findViewById(R.id.signup1)
        login=findViewById(R.id.login1)

        img.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type ="image/*"
            startActivityForResult(intent,1)
        }

        signup.setOnClickListener {

            if (etUsername.text.isEmpty() || etEmail.text.isEmpty() || etPassword.text.isEmpty() || etConfirmPass.text.isEmpty() || etPassword.text.toString() != etConfirmPass.text.toString() || etPassword.text.length < 6 || imageUri == null){
                if (etUsername.text.isEmpty()){
                    etUsername.error = "Please enter a username"
                }
                if (etEmail.text.isEmpty()){
                    etEmail.error = "Please enter an email"
                }
                if (etPassword.text.isEmpty()){
                    etPassword.error = "Please enter a password"
                }
                if (etConfirmPass.text.isEmpty()){
                    etConfirmPass.error = "Please enter a confirm password"
                }
                if (etPassword.text.toString() != etConfirmPass.text.toString()) {
                    etPassword.error
                    etConfirmPass.error
                    Toast.makeText(
                        this,
                        "please make sure you wrote the same password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if(etPassword.text.length<6){
                    etPassword.error = "Please enter more than 5"
                }
                if (imageUri == null) {
                    Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                pb.visibility = View.VISIBLE

                    auth = FirebaseAuth.getInstance()
                    auth.createUserWithEmailAndPassword(etEmail.text.toString(),etPassword.text.toString()).addOnCompleteListener {

                         val uid = auth.currentUser!!.uid

                         st = FirebaseStorage.getInstance()


                        if (imageUri!! != null) {
                            val fileName = "$uid.jpg"
                            val refStorage = FirebaseStorage.getInstance().reference.child("images/$fileName")

                            refStorage.putFile(imageUri!!)
                                .addOnSuccessListener(
                                    OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                                        taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                                            val imageUrl = it.toString()
                                            val userMap = hashMapOf(
                                                "id" to uid,
                                                "name" to etUsername.text.toString(),
                                                "email" to etEmail.text.toString(),
                                                "image" to imageUrl,

                                                )
                                            db = FirebaseFirestore.getInstance()
                                            db.collection("users").document(uid).set(userMap).addOnSuccessListener {
                                                Toast.makeText(this, "User created Successfully!", Toast.LENGTH_SHORT).show()
                                                pb.visibility = View.GONE
                                                auth.signInWithEmailAndPassword(etEmail.text.toString(),etPassword.text.toString()).addOnSuccessListener {
                                                    intent = Intent(this,HomeActivity::class.java)
                                                    startActivity(intent)
                                                }.addOnFailureListener {
                                                    pb.visibility = View.GONE
                                                    Toast.makeText(this, "Failed1!", Toast.LENGTH_SHORT).show()
                                                }


                                            }.addOnFailureListener {
                                                pb.visibility = View.GONE
                                                Toast.makeText(this, "Failed1!", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    })

                                ?.addOnFailureListener(OnFailureListener { e ->
                                    print(e.message)
                                })
                        }
                    }.addOnFailureListener{
                        Toast.makeText(this, "Failed! in create", Toast.LENGTH_SHORT).show()
                        pb.visibility = View.GONE
                    }

            }

        }

        login.setOnClickListener {
            intent=  Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data!=null){
            if (data.data!=null){
                imageUri=data.data!!
                img.setImageURI(imageUri)
            }
        }
    }


}