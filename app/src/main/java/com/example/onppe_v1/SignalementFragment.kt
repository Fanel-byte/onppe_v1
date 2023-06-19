package com.example.onppe_v1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentFactory
import androidx.navigation.findNavController
import com.example.onppe_v1.databinding.FragmentSignalementBinding


class SignalementFragment : Fragment() {
    lateinit var binding: FragmentSignalementBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentSignalementBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.frameLayout3.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementFragment_to_signalementFormSignaleurFragment)

        }
        binding.frameLayout2.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementFragment_to_signalementImageFragment)
        }
        binding.frameLayout4.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementFragment_to_signalementVideoFragment)
        }
        binding.frameLayout5.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementFragment_to_signalementSonFragment)
        }
        binding.suivi.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementFragment_to_suivisignalementfragment)
        }
        binding.home.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementFragment_to_fonctionnalitiesActivity)
        }
        binding.back.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementFragment_to_fonctionnalitiesActivity)
        }

    }

}
