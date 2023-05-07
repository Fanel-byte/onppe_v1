package com.example.onppe_v1

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.findNavController
import com.example.onppe_v1.databinding.FragmentSignalementForm2Binding
import com.example.onppe_v1.databinding.FragmentSignalementForm3Binding


class SignalementForm2Fragment : Fragment() {

    lateinit var binding: FragmentSignalementForm2Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignalementForm2Binding.inflate(inflater, container, false)
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


        binding.maried.setOnClickListener {
            binding.maried.setBackgroundColor( Color.parseColor("#CCF28123") )
            binding.maried.setTextColor(Color.WHITE)
            binding.divorced.setBackgroundColor( Color.WHITE )
            binding.divorced.setTextColor(Color.parseColor("#6B7280"))
            binding.widower.setBackgroundColor( Color.WHITE )
            binding.widower.setTextColor(Color.parseColor("#6B7280"))
            binding.other.setBackgroundColor( Color.WHITE)
            binding.other.setTextColor(Color.parseColor("#6B7280"))

        }
        binding.divorced.setOnClickListener {
            binding.divorced.setBackgroundColor( Color.parseColor("#CCF28123") )
            binding.divorced.setTextColor(Color.WHITE)
            binding.maried.setBackgroundColor( Color.WHITE )
            binding.maried.setTextColor(Color.parseColor("#6B7280"))
            binding.widower.setBackgroundColor( Color.WHITE )
            binding.widower.setTextColor(Color.parseColor("#6B7280"))
            binding.other.setBackgroundColor( Color.WHITE)
            binding.other.setTextColor(Color.parseColor("#6B7280"))
        }

        binding.widower.setOnClickListener {
            binding.widower.setBackgroundColor( Color.parseColor("#CCF28123") )
            binding.widower.setTextColor(Color.WHITE)
            binding.divorced.setBackgroundColor( Color.WHITE )
            binding.divorced.setTextColor(Color.parseColor("#6B7280"))
            binding.maried.setBackgroundColor( Color.WHITE )
            binding.maried.setTextColor(Color.parseColor("#6B7280"))
            binding.other.setBackgroundColor( Color.WHITE)
            binding.other.setTextColor(Color.parseColor("#6B7280"))
        }

        binding.other.setOnClickListener {
            binding.other.setBackgroundColor( Color.parseColor("#CCF28123") )
            binding.other.setTextColor(Color.WHITE)
            binding.divorced.setBackgroundColor( Color.WHITE )
            binding.divorced.setTextColor(Color.parseColor("#6B7280"))
            binding.widower.setBackgroundColor( Color.WHITE )
            binding.widower.setTextColor(Color.parseColor("#6B7280"))
            binding.maried.setBackgroundColor( Color.WHITE)
            binding.maried.setTextColor(Color.parseColor("#6B7280"))
        }
        binding.back.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementForm2Fragment_to_signalementForm1Fragment)
        }
        binding.back2.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementForm2Fragment_to_signalementForm1Fragment)
        }
        binding.back3.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementForm2Fragment_to_signalementForm1Fragment)
        }
        binding.home.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementForm2Fragment_to_fonctionnalitiesActivity)
        }
        binding.next.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementForm2Fragment_to_signalementForm3Fragment)
        }

        binding.next2.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementForm2Fragment_to_signalementForm3Fragment)
        }
    }


}