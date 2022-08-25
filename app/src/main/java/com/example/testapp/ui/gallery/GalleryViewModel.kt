package com.example.testapp.ui.gallery

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testapp.Communicator
import com.example.testapp.MainActivity
import com.example.testapp.MyDBHelper
import com.example.testapp.Squerys
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class GalleryViewModel : ViewModel() {
    private val fb = FirebaseFirestore.getInstance()

    private val _text = MutableLiveData<String>().apply {
        value = "List of Tasks"
    }
    val text: LiveData<String> = _text

    fun upFireBase(context: Context){
        var fireDataBase = FirebaseDatabase.getInstance().getReference("Users")
        var localdb = Squerys()
        var rs = localdb.SelectDb(context,null)
        var move = rs.moveToNext()
        while (move) {
                if (fireDataBase != null) {
                    var user = User(rs.getString(1), rs.getString(3))
                    fireDataBase.child(rs.getString(1)).setValue(user).addOnSuccessListener {
                        rs.getString(1)
                        rs.getString(3)
                    }.addOnFailureListener {
                        Toast.makeText(context, "ERROR FATAL", Toast.LENGTH_LONG).show()
                    }
                }
            move = rs.moveToNext()
        }

    }
}