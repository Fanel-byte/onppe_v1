package com.example.onppe_v1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.findNavController
import com.example.onppe_v1.databinding.FragmentSignalementForm1Binding

lateinit var binding: FragmentSignalementForm1Binding
class SignalementForm1Fragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignalementForm1Binding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val items1 = listOf(
            "1",
            "2",
            "3",
            "4",
            "5")
        val adapter = ArrayAdapter(requireActivity(), R.layout.list_item, items1)
        binding.cause.setAdapter(adapter)
        binding.next.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementForm1Fragment_to_signalementForm3Fragment)
        }

    }
}