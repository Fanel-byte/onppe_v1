package com.example.onppe_v1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.onppe_v1.databinding.FragmentSuiviSignalementDetailBinding

class SuiviSignalementDetailFragment : Fragment() {
    lateinit var binding: FragmentSuiviSignalementDetailBinding
    lateinit var signalementModel: SignalementModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSuiviSignalementDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signalementModel = ViewModelProvider(requireActivity()).get(SignalementModel::class.java)
        //declaration


        val position = arguments?.getInt("position")
        if (position != null) {
            // read only
            val signalement = signalementModel.signalements.get(position)
            binding.reason.text = signalement.designationar
            binding.adresse1.text=signalement.adresse
            binding.wilaya1.text = signalement.namear
            binding.age1.text = signalement.age.toString()
            binding.nomcomplet.text = signalement.nom + " " +signalement.prenom
            binding.sexe1.text = signalement.sexe
            binding.situation.text = signalement.situationparent
            binding.reason.text = signalement.descriptif
            binding.number.text = (position+1).toString()


        }
    }
}