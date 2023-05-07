package com.example.onppe_v1

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.onppe_v1.databinding.FragmentSignalementForm2Binding
import com.example.onppe_v1.databinding.FragmentSignalementForm5Binding


class SignalementForm5Fragment : Fragment() {


    lateinit var binding: FragmentSignalementForm5Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignalementForm5Binding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.next.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementForm5Fragment_to_finFormulaireFragment)
        }
        binding.next2.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementForm5Fragment_to_finFormulaireFragment)
        }
        binding.back.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementForm5Fragment_to_signalementForm4Fragment)
        }
        binding.back2.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementForm5Fragment_to_signalementForm4Fragment)
        }
        binding.back3.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementForm5Fragment_to_signalementForm4Fragment)
        }
        binding.home.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementForm5Fragment_to_fonctionnalitiesActivity)
        }
    }
}