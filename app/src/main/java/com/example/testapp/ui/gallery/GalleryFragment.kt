package com.example.testapp.ui.gallery

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.testapp.MainActivity
import com.example.testapp.Squerys
import com.example.testapp.R
import com.example.testapp.databinding.FragmentGalleryBinding
import kotlin.properties.Delegates


class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel
    private var _binding: FragmentGalleryBinding? = null
    private var inputText: String? = ""
    private var querys = Squerys()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var copylist : Array<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root


        inputText = arguments?.getCharSequence("input_txt") as String?

        val textView: TextView = binding.Listittle
        galleryViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        // listado de items

        var rs =  querys.SelectDb(super.requireContext(),null,)
        var listItems = arrayOfNulls<String>(rs.count)
        var lisItemsshow = arrayOfNulls<String>(rs.count)
        if (rs.moveToNext()) {
            var next = true
            var textable = ""
            var textableshow = ""
            var cnt = 0
            while (next){
                textable = rs.getString(0) +") "+rs.getString(1)+": " + rs.getString(3)
                listItems[cnt] = textable
                textableshow = rs.getString(1)+": " + rs.getString(3)
                lisItemsshow[cnt] = textableshow
                cnt+=1
                next = rs.moveToNext()
            }
        }
        var adapter = ArrayAdapter(super.requireContext(), android.R.layout.simple_list_item_1,lisItemsshow)
        binding.ListTask.adapter = adapter

        binding.update.setOnClickListener{
            this.copylist = listItems.requireNoNulls()
            // FIREBASE
            galleryViewModel.upFireBase(super.requireContext())
        }

        binding.ListTask.setOnItemClickListener { parent, view, position, id ->
            val main = (activity as MainActivity)
            val intent = Intent(requireContext(),DetailTask::class.java)
            var items = listItems[position]?.split("\n ")
            var fields = items?.toTypedArray()
            intent.putExtra("Task",fields)
            startActivity(intent)
            Reload()
        }
        binding.delete.setOnClickListener{
            querys.TruncateTable(super.requireContext())
            Reload()
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun Reload(){

        val transaction = childFragmentManager.beginTransaction()
        val frag2 = GalleryFragment()
        transaction.replace(R.id.onlygal,frag2)
        binding.onlygal.removeAllViews()
        transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()

    }
}