package com.example.onppe_v1

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.onppe_v1.databinding.FragmentAideBinding

class AideFragment : Fragment() {
    lateinit var binding: FragmentAideBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentAideBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.droits.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_aideFragment_to_droitsfragment)
        }
        binding.app.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_aideFragment_to_appfragment)
        }
        binding.onppe.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_aideFragment_to_onppefragment)
        }
    }}