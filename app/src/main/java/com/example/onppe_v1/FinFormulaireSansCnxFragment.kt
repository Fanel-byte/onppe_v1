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
import com.example.onppe_v1.databinding.FragmentFinFormulaireSansCnxBinding

class FinFormulaireSansCnxFragment : Fragment() {

    lateinit var binding: FragmentFinFormulaireSansCnxBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFinFormulaireSansCnxBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.suivi.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_finFormulaireSansCnxFragment_to_suivisignalementfragment)
        }
    }


}