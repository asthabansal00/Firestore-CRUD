package com.asthabansal.firebasefirestore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler

class RecyclerAdapter(var notesList: ArrayList<NotesDataClass>, var recyclerInterface: RecyclerInterface ) :RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    class ViewHolder(view: View) :RecyclerView.ViewHolder(view) {
        var tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        var tvDescription = view.findViewById<TextView>(R.id.tvDescription)
        var update = view.findViewById<Button>(R.id.btnupdate)
        var delete = view.findViewById<Button>(R.id.btndelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler,parent ,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        holder.tvTitle.setText(notesList[position].title.toString())
        holder.tvDescription.setText(notesList[position].description.toString())
        holder.delete?.setOnClickListener {
            recyclerInterface.btndelete(position)
        }
        holder.update?.setOnClickListener {
            recyclerInterface.btnupdate(position)
        }
    }

    override fun getItemCount() = notesList.size
}