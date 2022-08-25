package com.example.testapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class Squerys {
    private lateinit var contexto : Context
    private lateinit var db : SQLiteDatabase
    private var user: FirebaseUser? = Firebase.auth.currentUser

    fun SelectDb(context: Context, selectquery:String?,): Cursor {
        this.contexto = context
        var helper = MyDBHelper(this.contexto)
        this.db = helper.readableDatabase
        var cursor : Cursor
        cursor = if (this.user != null){
            var user = "USERMAIL= '${this.user!!.email}'"
            db.query("SUPERUSERS",null,user,null,null,null,null)
        }else{
            var user = "USERMAIL= 'none'"
            db.query("SUPERUSERS",null,user,null,null,null,null)
        }
        return cursor
    }
    fun TruncateTable(context: Context){
        this.contexto = context
        var helper = MyDBHelper(this.contexto)
        this.db = helper.readableDatabase
        db.execSQL("DROP TABLE SUPERUSERS")
        db.execSQL("CREATE TABLE SUPERUSERS(ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME VARCHAR(25), USERMAIL VARCHAR(25), TASK VARCHAR(500))")
    }

    fun UpdateTable(context: Context,wherequery: String,name: String?,descrip: String?, mail: String?){
        this.contexto = context
        var helper = MyDBHelper(this.contexto)
        this.db = helper.readableDatabase
        var cv = ContentValues()
        if (descrip != null ){cv.put("TASK",descrip)}
        if (name != null ){cv.put("NAME",name)}
        if (this.user != null ){cv.put("USERMAIL", this.user!!.email)}
        var where = "ID=$wherequery"
        db.update("SUPERUSERS",cv,where,null)

    }
    fun InsertTable(context: Context,name: String,descrip: String,mail: String?){
        this.contexto = context
        var helper = MyDBHelper(this.contexto)
        this.db = helper.readableDatabase
        var cv = ContentValues()
        cv.put("NAME",name)
        cv.put("TASK",descrip)
        if (this.user != null ){
            cv.put("USERMAIL", this.user!!.email)
        }else{
            cv.put("USERMAIL","none")
        }
        db.insert("SUPERUSERS",null,cv)
    }

    fun DeleteTable(context: Context,ID : String){
        this.contexto = context
        var helper = MyDBHelper(this.contexto)
        this.db = helper.readableDatabase
        var where = "ID = $ID"
        db.delete("SUPERUSERS",where,null)
    }

}