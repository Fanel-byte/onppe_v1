package com.example.onppe_v1

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.onppe_v1.databinding.FragmentSuiviSignalementDetailBinding

class SuiviSignalementDetailFragment : Fragment() {
    lateinit var binding: FragmentSuiviSignalementDetailBinding
    lateinit var signalementsModel: SignalementsModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSuiviSignalementDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signalementsModel = ViewModelProvider(requireActivity()).get(SignalementsModel::class.java)



        val position = arguments?.getInt("position")
        if (position != null) {
            val signalement = signalementsModel.signalements.get(position)
            if (signalement.motifid != null)
                binding.reason.text = resources.getStringArray(R.array.motifs).toList()[signalement.motifid!!]
            binding.adresse1.text=signalement.adresseEnfant
            if (signalement.wilayacodeEnfant != null)
                binding.wilaya1.text = resources.getStringArray(R.array.wilayas).toList()[signalement.wilayacodeEnfant!!]
            binding.age1.text = signalement.ageEnfant.toString()
            binding.nomcomplet.text = signalement.nomEnfant + " " +signalement.prenomEnfant
            binding.sexe1.text = signalement.sexeEnfant
            binding.situation.text = signalement.situationparentEnfant
            binding.contenu.text = signalement.descriptif
            binding.datesignalement2.text = signalement.dateincident

        }
        binding.home.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_suivisignalementdetailfragment_to_fonctionnalitiesActivity)
        }
        binding.back.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_suivisignalementdetailfragment_to_suivisignalementfragment)
        }
    }
}