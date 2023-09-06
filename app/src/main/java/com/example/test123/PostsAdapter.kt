package com.example.test123

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class PostsAdapter(var c:Context) : RecyclerView.Adapter<PostsAdapter.MyViewHolder>() {
    private  var db : FirebaseFirestore = FirebaseFirestore.getInstance()
    companion object {
        var postsList=ArrayList<PostData>()
    }
    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val tname : TextView = itemView.findViewById(R.id.textView4)
        val ttext : TextView = itemView.findViewById(R.id.textView5)
        val pimg : ImageView = itemView.findViewById(R.id.imageView2)
        val postImg : ImageView = itemView.findViewById(R.id.imageView3)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.posts,parent,false)
        return  MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return postsList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = postsList[position]
        holder.tname.text = currentItem.name
        holder.ttext.text = currentItem.text
        Glide.with(c).load(currentItem.pimg).into(holder.pimg)
        Glide.with(c).load(currentItem.image).into(holder.postImg)
    }

    init {
        postsList.clear()
        db = FirebaseFirestore.getInstance()
        db.collection("posts")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if(error != null){

                        Log.e("Data base error!",error.message.toString())
                        return
                    }

                    for (dc: DocumentChange in value?.documentChanges!!){
                        if(dc.getType() == DocumentChange.Type.ADDED){
                            val user = dc.getDocument().toObject(PostData::class.java)
                            postsList.add(user)
                            notifyDataSetChanged()
                        }
                    }

                }
            })
    }
}