package com.example.onppe_v1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.onppe_v1.databinding.FragmentFinFormulaireBinding

class FinFormulaireFragment : Fragment() {

    lateinit var binding: FragmentFinFormulaireBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFinFormulaireBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.suivi.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_finFormulaireFragment_to_suivisignalementfragment)
        }
        //Pour ne pas avoir le bouton retour
              (requireActivity() as AppCompatActivity?)?.supportActionBar!!.setDisplayHomeAsUpEnabled (false)
        //Pour ne pas avoir de retour
        (requireActivity() as AppCompatActivity).apply {
            onBackPressedDispatcher.addCallback(viewLifecycleOwner) {}
        }
    }


}