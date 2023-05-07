package com.example.onppe_v1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.findNavController
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
            "أنثى",
            "ذكر ")
        val adapter = ArrayAdapter(requireActivity(), R.layout.list_item, items1)
        binding.sexe.setAdapter(adapter)

        binding.back.setOnClickListener {
            view.findNavController().navigate(R.id.action_signalementForm4Fragment2_to_signalementForm3Fragment)
        }

        binding.back2.setOnClickListener {
            view.findNavController().navigate(R.id.action_signalementForm4Fragment2_to_signalementForm3Fragment)
        }
        binding.back3.setOnClickListener {
            view.findNavController().navigate(R.id.action_signalementForm4Fragment2_to_signalementForm3Fragment)
        }

        binding.next.setOnClickListener {
            view.findNavController().navigate(R.id.action_signalementForm4Fragment2_to_signalementForm5Fragment)
        }
        binding.next2.setOnClickListener {
            view.findNavController().navigate(R.id.action_signalementForm4Fragment2_to_signalementForm5Fragment)
        }

        binding.home.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementForm4Fragment_to_fonctionnalitiesActivity)
        }




    }

}