package com.umangmathur.umangdemoapp.adapters

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.umangmathur.umangdemoapp.R
import com.umangmathur.umangdemoapp.model.UserPost.UserPost

class PostsRecyclerViewAdapter(private val postsList: ArrayList<UserPost>) : Adapter<PostsRecyclerViewAdapter.UserPostViewHolder>() {

    override fun onBindViewHolder(holder: UserPostViewHolder, position: Int) {
        val userPost: UserPost = postsList[position]
        holder.txtTitle.text = userPost.title
        holder.txtBody.text = userPost.body
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserPostViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.post_item_layout, parent, false)
        return UserPostViewHolder(view)
    }

    override fun getItemCount(): Int {
        return postsList.size
    }

    class UserPostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        val txtBody: TextView = itemView.findViewById(R.id.txtBody)
    }

}