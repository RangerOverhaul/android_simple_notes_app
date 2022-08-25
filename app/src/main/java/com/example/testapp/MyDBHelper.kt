package com.example.testapp

import android.content.Context
import android.database.sqlite.*

class MyDBHelper(context: Context) : SQLiteOpenHelper(context, "USERDataBase",null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE SUPERUSERS(ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME VARCHAR(25), USERMAIL VARCHAR(25), TASK VARCHAR(500))")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

}