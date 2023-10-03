package com.asthabansal.firebasefirestore

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Note
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.asthabansal.firebasefirestore.databinding.ActivityMainBinding
import com.asthabansal.firebasefirestore.databinding.CustomDialogBinding
import com.asthabansal.firebasefirestore.databinding.CustomUpdateDialogBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.lang.reflect.Array.get

class MainActivity : AppCompatActivity(),RecyclerInterface {
    var notesList = arrayListOf<NotesDataClass>()
    lateinit var adapter: RecyclerAdapter
    lateinit var layoutManager: LinearLayoutManager
    lateinit var binding: ActivityMainBinding
    var firestore= Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //throw RuntimeException("Test Crash")

        layoutManager = LinearLayoutManager(this)
        binding.recycler.layoutManager = layoutManager

        adapter = RecyclerAdapter(notesList, this )
        binding.recycler.adapter = adapter



        binding.fab.setOnClickListener {
            var dialog = Dialog(this)
            var dialogbinding = CustomDialogBinding.inflate(layoutInflater)
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)

            dialog.show()
            dialog.setContentView(dialogbinding.root)

            dialogbinding.btnAdd.setOnClickListener {
                if (dialogbinding.etTitle.text.toString().isEmpty()) {
                    dialogbinding.etTitle.error = "Enter your name"
                } else if (dialogbinding.etDescription.text.toString().isEmpty()) {
                    dialogbinding.etDescription.error = "Enter your roll no"
                } else {
                    firestore.collection("User")
                        .add(NotesDataClass(title = dialogbinding.etTitle.text.toString(),
                    description = dialogbinding.etDescription.text.toString()))
                        .addOnSuccessListener {
                            Toast.makeText(this@MainActivity,"data added",Toast.LENGTH_LONG).show()
                            getData()
                        }
                        .addOnCanceledListener {
                            Toast.makeText(this@MainActivity,"Cancelled",Toast.LENGTH_LONG).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this@MainActivity,"data not added", Toast.LENGTH_LONG).show()
                        }
                }

                dialog.dismiss()
                adapter.notifyDataSetChanged()

            }
        }
    }

    override fun btndelete(position: Int) {
        firestore.collection("User")
            .document(notesList[position].id?:"")
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "data deleted", Toast.LENGTH_LONG).show()
                notesList.removeAt(position)
                adapter.notifyDataSetChanged()
            }
    }

    override fun btnupdate(position: Int) {
        var dialog = Dialog(this)
        var dialogbinding = CustomUpdateDialogBinding.inflate(layoutInflater)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog.setContentView(dialogbinding.root)
        dialog.show()
        dialogbinding.btnAdd.setOnClickListener {
            if (dialogbinding.etTitle.text.toString().isEmpty()) {
                dialogbinding.etTitle.error = "Enter your name"
            } else if (dialogbinding.etDescription.text.toString().isEmpty()) {
                dialogbinding.etDescription.error = "Enter your roll no"
            } else {
                var updateItem = NotesDataClass(title = dialogbinding.etTitle.text.toString(),
                description = dialogbinding.etDescription.text.toString(),
                id = notesList[position].id?:"")
                firestore.collection("User")
                    .document(notesList[position].id?:"")
                    .set(updateItem)
                    .addOnSuccessListener {
                        Toast.makeText(this,"item updated",Toast.LENGTH_LONG).show()
                    }
                dialog.dismiss()
                adapter.notifyDataSetChanged()
               notesList.set(position,
                    NotesDataClass(
                        title = dialogbinding.etTitle.text.toString(),
                        description = dialogbinding.etDescription.text.toString()))
            }
        }
    }
    fun getData(){
    firestore.collection("User")
    .get()
    .addOnSuccessListener {
        System.out.println("in snapshot ${it.documents}")
        for( items in it.documents){
            System.out.println("items ${items.data}")
            var firestoreClass = items.toObject<NotesDataClass>()?: NotesDataClass()
            //items.id = firestoreClass.id
            firestoreClass.id = items.id
            notesList.add(firestoreClass)
            adapter.notifyDataSetChanged()
        }
    }
    }
}
