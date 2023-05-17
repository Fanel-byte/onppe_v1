package com.example.onppe_v1

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.onppe_v1.databinding.FragmentSignalementForm1Binding
import com.example.onppe_v1.databinding.FragmentSignalementForm4Binding


class SignalementForm4Fragment : Fragment() {
    lateinit var binding: FragmentSignalementForm4Binding
    private lateinit var signalementModel: SignalementTransfertModel
    private var sexe:String = ""
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
        val dialogBinding = layoutInflater.inflate(R.layout.fragment_popup_window,null)
        val myDialog = Dialog(requireActivity())
        myDialog.setContentView(dialogBinding)
        myDialog.setCancelable(true)
        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // Récupérer la taille de l'écran
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = (displayMetrics.widthPixels * 0.75).toInt()
        val height =  WindowManager.LayoutParams.WRAP_CONTENT

        signalementModel = ViewModelProvider(requireActivity()).get(SignalementTransfertModel::class.java)
        val items1 = listOf(
            "أنثى",
            "ذكر ")
        val adapter = ArrayAdapter(requireActivity(), R.layout.list_item, items1)
        binding.sexe.setAdapter(adapter)
        RemplirChamps(signalementModel)

        binding.sexe.setOnItemClickListener { parent, view, position, id ->
            if (position == 0) {
                sexe = "F"
            } else {
                if (position == 1) {
                    sexe = "M"
                }
            }
        }

        binding.back.setOnClickListener {
            view.findNavController().navigate(R.id.action_signalementForm4Fragment_to_signalementForm3Fragment)
        }

        binding.back2.setOnClickListener {
            view.findNavController().navigate(R.id.action_signalementForm4Fragment_to_signalementForm3Fragment)
        }

        binding.next.setOnClickListener {
            if ((binding.prenom.text.toString().isEmpty())||(binding.nom.text.toString().isEmpty())||(binding.tel.text.toString().isEmpty())){
                myDialog.show()
            }else{
                signalementModel.nomCitoyen=binding.nom.text.toString()
                signalementModel.prenomCitoyen=binding.prenom.text.toString()
                signalementModel.ageCitoyen=binding.age.text.toString().toInt()
                signalementModel.sexeCitoyen=sexe
                signalementModel.adresseCitoyen=binding.adresse.text.toString()
                signalementModel.telCitoyen=binding.tel.text.toString()
                view.findNavController().navigate(R.id.action_signalementForm4Fragment_to_signalementForm5Fragment)
            }
        }
        binding.back3.setOnClickListener {
            view.findNavController().navigate(R.id.action_signalementForm4Fragment_to_signalementForm3Fragment)
        }
        binding.next2.setOnClickListener {
            view.findNavController().navigate(R.id.action_signalementForm4Fragment_to_signalementForm5Fragment)
        }
        binding.home.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementForm4Fragment_to_fonctionnalitiesActivity)
        }
    }

    private fun RemplirChamps(signalementModel : SignalementTransfertModel ) {
        if (signalementModel.nomCitoyen != null){
            binding.nom.setText(signalementModel.nomCitoyen)
        }
        if (signalementModel.prenomCitoyen != null){
            binding.prenom.setText(signalementModel.prenomCitoyen)
        }
        if (signalementModel.ageCitoyen != null){
            binding.age.setText(signalementModel.ageCitoyen.toString())
        }
        if (signalementModel.adresseCitoyen != null){
            binding.adresse.setText(signalementModel.adresseCitoyen)
        }
        if (signalementModel.telCitoyen != null){
            binding.tel.setText(signalementModel.telCitoyen)
        }
        if (signalementModel.sexeCitoyen != null){
            //binding.sexe.setText(signalementModel.sexeCitoyen)
        }
    }
}