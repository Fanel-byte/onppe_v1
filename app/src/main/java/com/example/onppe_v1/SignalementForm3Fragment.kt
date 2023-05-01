package com.example.onppe_v1

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.findNavController
import com.example.onppe_v1.databinding.FragmentSignalementForm3Binding


class SignalementForm3Fragment : Fragment() {
    lateinit var binding: com.example.onppe_v1.databinding.FragmentSignalementForm3Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignalementForm3Binding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.next.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(R.id.action_signalementForm3Fragment_to_signalementForm4Fragment)
        }
        binding.nextanonym.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(R.id.action_signalementForm3Fragment_to_signalementForm5Fragment)

            binding.back2.setOnClickListener { view: View ->
                view.findNavController()
                    .navigate(R.id.action_signalementForm3Fragment_to_signalementForm2Fragment)
            }

        }


    }
}