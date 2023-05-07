package com.example.onppe_v1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.example.onppe_v1.databinding.FragmentSignalementForm1Binding
import com.example.onppe_v1.databinding.FragmentSignalementForm4Binding


class SignalementForm4Fragment : Fragment() {
    lateinit var binding: FragmentSignalementForm4Binding
    lateinit var sexe:String
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

        var signalementtransfert = arguments?.getSerializable("data") as SignalementTransfert

        val items1 = listOf(
            "أنثى",
            "ذكر ")
        val adapter = ArrayAdapter(requireActivity(), R.layout.list_item, items1)
        binding.sexe.setAdapter(adapter)
        binding.sexe.setOnItemClickListener { parent, view, position, id ->
            if (position==0){
                sexe="F"
            }
            else {sexe="M"}
        }
        binding.back.setOnClickListener {
            view.findNavController().navigate(R.id.action_signalementForm4Fragment2_to_signalementForm3Fragment)
        }

        binding.back2.setOnClickListener {
            view.findNavController().navigate(R.id.action_signalementForm4Fragment2_to_signalementForm3Fragment)
        }

        binding.suivant.setOnClickListener {
            signalementtransfert.nomCitoyen=binding.nom.text.toString()
            signalementtransfert.prenomCitoyen=binding.prenom.text.toString()
            signalementtransfert.ageCitoyen=binding.age.text.toString().toInt()
            signalementtransfert.sexeCitoyen=sexe
            signalementtransfert.adresseCitoyen=binding.adresse.text.toString()
            signalementtransfert.telCitoyen=binding.tel.text.toString()
            val data = bundleOf("data" to signalementtransfert)
            view.findNavController().navigate(R.id.action_signalementForm4Fragment2_to_signalementForm5Fragment,data)

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