package com.example.onppe_v1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.onppe_v1.databinding.FragmentSignalementForm1Binding
import com.example.onppe_v1.databinding.FragmentSignalementForm4Binding


class SignalementForm4Fragment : Fragment() {
    lateinit var binding: FragmentSignalementForm4Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignalementForm4Binding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val items1 = listOf(
            "F",
            "M")
        val adapter = ArrayAdapter(requireActivity(), R.layout.list_item, items1)
        binding.sexe.setAdapter(adapter)


        binding.button2.setOnClickListener {

        }
        binding.button3.setOnClickListener {

        }
    }

}