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
import java.util.Calendar


class Createpost : AppCompatActivity() {
    private lateinit var img: ImageView
    private lateinit var create: Button
    private lateinit var caption: EditText
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var st: FirebaseStorage
    private lateinit var pb: ProgressBar
    @SuppressLint("SimpleDateFormat")
    private val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
    @SuppressLint("SimpleDateFormat")
    private val sdfid = SimpleDateFormat("yyyyMMddHHmmss")
    var imageUri : Uri?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_createpost)

        img=findViewById(R.id.imageView99)
        create=findViewById(R.id.button2)
        caption=findViewById(R.id.editTextText12)
        pb=findViewById(R.id.progressBar22)

        img.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type ="image/*"
            startActivityForResult(intent,1)
        }

        create.setOnClickListener {
            if(imageUri == null|| caption.text.isEmpty()){
                if(caption.text.isEmpty()){
                    caption.error = "Pealse add caption!"
                }
                if (imageUri == null) {
                    Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
                }
            }else{
                pb.visibility = View.VISIBLE
                auth = FirebaseAuth.getInstance()

                    val uid = auth.currentUser!!.uid

                    st = FirebaseStorage.getInstance()


                    if (imageUri!! != null) {
                        val srt = sdfid.format(Calendar.getInstance().time).toString()
                        val fileName = "$srt.jpg"
                        val refStorage = FirebaseStorage.getInstance().reference.child("images/$fileName")

                        refStorage.putFile(imageUri!!)
                            .addOnSuccessListener(
                                OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                                    taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                                        val imageUrl = it.toString()
                                        db = FirebaseFirestore.getInstance()
                                        db.collection("users").document(uid).get().addOnCompleteListener {
                                            val userMap = hashMapOf(
                                                "name" to it.result!!.data?.getValue("name").toString().trim(),
                                                "pimg" to it.result!!.data?.getValue("image").toString().trim(),
                                                "text" to caption.text.toString(),
                                                "image" to imageUrl,
                                                "time" to sdf.format(Calendar.getInstance().time).toString(),
                                                "sort" to srt,

                                                )

                                            db.collection("posts").document(sdfid.format(Calendar.getInstance().time).toString()).set(userMap).addOnSuccessListener {
                                                Toast.makeText(this, "post added Successfully!", Toast.LENGTH_SHORT).show()
                                                caption.text.clear()

                                                pb.visibility = View.GONE


                                            }.addOnFailureListener {
                                                pb.visibility = View.GONE
                                                Toast.makeText(this, "Failed1!", Toast.LENGTH_SHORT).show()
                                            }
                                        }

                                    }
                                })

                            ?.addOnFailureListener(OnFailureListener { e ->
                                print(e.message)
                            })
                    }
                }


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