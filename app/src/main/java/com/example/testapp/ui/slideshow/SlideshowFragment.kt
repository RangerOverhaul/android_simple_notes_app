package com.example.testapp.ui.slideshow

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.testapp.MainActivity
import com.example.testapp.R
import com.example.testapp.Squerys
import com.example.testapp.databinding.FragmentSlideshowBinding
import com.example.testapp.ui.gallery.GalleryFragment
import com.example.testapp.ui.home.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SlideshowFragment : Fragment() {

    private lateinit var slideshowViewModel: SlideshowViewModel
    private var _binding: FragmentSlideshowBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        slideshowViewModel =
            ViewModelProvider(this).get(SlideshowViewModel::class.java)

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val user = Firebase.auth.currentUser
        if (user != null){
            binding.logtitle.text = user.email
        }
        val textView: TextView = binding.logtitle
        slideshowViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        binding.bottomLog.setOnClickListener{
            if (binding.logMail.text.isNotEmpty() && binding.logPass.text.isNotEmpty()){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(binding.logMail.text.toString(),
                    binding.logPass.text.toString()).addOnCompleteListener {
                        if (it.isSuccessful){
                            Toast.makeText(super.getContext(),"${binding.logMail.text} Created", Toast.LENGTH_LONG).show()
                            binding.logPass.text = null
                            binding.logPass.focusable
                        }else{
                            Toast.makeText(super.getContext(),"Eror en el registro", Toast.LENGTH_LONG).show()
                        }
                }
            }
        }
        binding.bottomSig.setOnClickListener{
            if (binding.logMail.text.isNotEmpty() && binding.logPass.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(binding.logMail.text.toString(),
                    binding.logPass.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(super.getContext()," ¡WELCOME! ", Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(super.getContext(),"Usuario no registrado", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        binding.bottomOUT.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(super.getContext(),"Log Out", Toast.LENGTH_LONG).show()
            showHome()
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun showHome(){
        val intent = Intent(super.requireContext(), MainActivity::class.java)
        startActivity(intent)
    }
}