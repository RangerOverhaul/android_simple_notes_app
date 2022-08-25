package com.example.testapp.ui.gallery

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import com.example.testapp.MainActivity
import com.example.testapp.R
import com.example.testapp.MyDBHelper
import com.example.testapp.Squerys
import com.example.testapp.databinding.ActivityDetailTaskBinding
import com.example.testapp.ui.home.DatePickerFragment
import com.example.testapp.databinding.ActivityMainBinding


class DetailTask : AppCompatActivity() {
    private lateinit var binding: ActivityDetailTaskBinding
    private lateinit var name : String
    private lateinit var id : String
    private lateinit var date : String
    private lateinit var descrip : String
    private lateinit var mate : String
    private var querys = Squerys()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val task = intent.getSerializableExtra("Task") as Array<String>
        var nameid = task[0]
        this.id = nameid.split(")")[0]
        this.name = nameid.split(")")[1]
        this.name = this.name.replace(":","")
        this.date = task[2]
        this.mate = task[1]
        this.descrip = task[3]
        binding.numberTask.text = "TASK #$id"
        binding.editDescripcion.setText(descrip)
        binding.editFechaInicio.setText(date)
        binding.editAutores.setText(name)
        binding.editMateria.setText(mate)

        binding.editFechaInicio.setOnClickListener{ showDatePickerDialog()}

        binding.bottomAdd.setOnClickListener{
            var des = binding.editDescripcion.text
            var fech = binding.editFechaInicio.text
            var aut = binding.editAutores.text
            var kind = binding.editMateria.text
            var Task = "\n $kind\n $fech\n $des"
            var sentence = Squerys()
            sentence.UpdateTable(applicationContext,this.id,aut.toString(),Task,null)
            onBackPressed()
        }
        binding.bottomDelete.setOnClickListener{
            var sentence = Squerys()
            sentence.DeleteTable(applicationContext,this.id)
            comprobate()
            //REGRESAR AL MAIN
            onBackPressed()
        }

        binding.bottomCancel.setOnClickListener(){
            val intent = Intent(applicationContext,MainActivity::class.java)
            startActivity(intent)
        }

    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day, moth, year -> onDateSelected(day, moth, year) }
        datePicker.show(supportFragmentManager,"datePicker")
    }

    fun  onDateSelected(day : Int, month: Int, year: Int){
        binding.editFechaInicio.setText("$day/$month/$year")
    }

    private fun comprobate(){
        var rs = querys.SelectDb(applicationContext,null)
        if ( !rs.moveToNext()){
            querys.TruncateTable(applicationContext)
        }
    }
}