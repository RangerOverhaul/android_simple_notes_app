package com.example.testapp.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.service.autofill.OnClickAction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.testapp.Communicator
import com.example.testapp.MainActivity
import com.example.testapp.R
import com.example.testapp.databinding.FragmentHomeBinding
import java.time.Year


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private lateinit var comm: Communicator
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textitle
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        binding.fechaInicio.setOnClickListener{ showDatePickerDialog()}

        comm = requireActivity() as Communicator
        binding.bottomAdd.setOnClickListener {
            var autores = binding.Autores.text.toString()
            var materia =  binding.Materia.text.toString()
            if (!autores.isEmpty() and !materia.isEmpty()){

                val text = autores +":\n "+ materia +"\n " + binding.fechaInicio.text.toString() +"\n "+binding.Descripcion.text.toString()
                comm.passDataCom(text)
                binding.Descripcion.setText("")
                binding.Materia.setText("")
                binding.fechaInicio.setText("")
                binding.Autores.setText("")
                binding.Descripcion.requestFocus()
            }else{
                if (autores.isEmpty()){
                    binding.Autores.requestFocus()
                }else{
                    binding.Materia.requestFocus()
                }
                Toast.makeText(super.getContext(),"Rellene los campos faltantes",Toast.LENGTH_LONG).show()
            }
        }
        binding.bottomCancel.setOnClickListener{
            Toast.makeText(super.getContext(),"Tarea cancelada",Toast.LENGTH_LONG).show()
            binding.Descripcion.setText("")
            binding.Materia.setText("")
            binding.fechaInicio.setText("")
            binding.Autores.setText("")
            binding.Descripcion.requestFocus()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun showalert(message: String){
        val  builder = AlertDialog.Builder(context, 1)
        builder.setTitle(" ALERT ")
        builder.setMessage(message)
        val dialog = builder.create()
        dialog.show()
    }
    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day, moth, year -> onDateSelected(day, moth, year) }
        val main = ( activity as MainActivity)
        datePicker.show(main.supportFragmentManager,"datePicker")
    }
    fun  onDateSelected(day : Int, month: Int, year: Int){
        binding.fechaInicio.setText("$day/$month/$year")
    }
}