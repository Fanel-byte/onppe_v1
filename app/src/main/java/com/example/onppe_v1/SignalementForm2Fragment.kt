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
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.onppe_v1.databinding.FragmentSignalementForm2Binding
import com.example.onppe_v1.databinding.FragmentSignalementForm3Binding
import kotlinx.coroutines.NonDisposableHandle.parent
import kotlin.properties.Delegates


class SignalementForm2Fragment : Fragment() {

    lateinit var binding: FragmentSignalementForm2Binding
    private lateinit var signalementModel: SignalementTransfertModel
    private var sexe=""
    private var wilayacode = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSignalementForm2Binding.inflate(inflater, container, false)
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

        // Définir la taille de la fenêtre du dialog
        myDialog.window?.setLayout(width, height)
        signalementModel = ViewModelProvider(requireActivity()).get(SignalementTransfertModel::class.java)
        val items1 = resources.getStringArray(R.array.gender)
        val wilayas = resources.getStringArray(R.array.wilayas).toList()
        val adapter = ArrayAdapter(requireActivity(), R.layout.list_item, items1)
        binding.sexe.setAdapter(adapter)
        val adapter2 = ArrayAdapter(requireActivity(), R.layout.list_item, wilayas)
        binding.wilaya.setAdapter(adapter2)
        binding.wilaya.setOnItemClickListener { parent, view, position, id ->
            wilayacode=position+1 }

        binding.wilaya.threshold = Int.MAX_VALUE // Show all items in the dropdown

        binding.wilaya.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.wilaya.showDropDown() // Show dropdown when AutoCompleteTextView gains focus
            }
        }
        binding.sexe.threshold = Int.MAX_VALUE // Show all items in the dropdown

        binding.sexe.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.sexe.showDropDown() // Show dropdown when AutoCompleteTextView gains focus
            }
        }

        binding.sexe.setOnItemClickListener { parent, view, position, id ->
            if (position == 0) {
                sexe = "F"
            } else {
                if (position == 1) {
                    sexe = "M"
                }
            }
        }

        binding.back.setOnClickListener { view: View ->
            view.findNavController().popBackStack()
        }
        binding.maried.setOnClickListener {
            binding.maried.setBackgroundColor( Color.parseColor("#CCF28123") )
            binding.maried.setTextColor(Color.WHITE)
            binding.divorced.setBackgroundColor( Color.WHITE )
            binding.divorced.setTextColor(Color.parseColor("#6B7280"))
            binding.widower.setBackgroundColor( Color.WHITE )
            binding.widower.setTextColor(Color.parseColor("#6B7280"))
            binding.other.setBackgroundColor( Color.WHITE)
            binding.other.setTextColor(Color.parseColor("#6B7280"))
            signalementModel.situationparentEnfant="mariés"
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
            signalementModel.situationparentEnfant="divorcés"
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
            signalementModel.situationparentEnfant="veuf(ve)"
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
            signalementModel.situationparentEnfant="autre"
        }
        binding.next.setOnClickListener { view: View ->
            if ((binding.sexe.text.toString().isEmpty())||(binding.age.text.toString().isEmpty())) {
                //affichage du pop up
                myDialog.show()
            } else {
                signalementModel.prenomEnfant=binding.prenom.text.toString()
                signalementModel.nomEnfant=binding.nom.text.toString()
                signalementModel.sexeEnfant=sexe
                signalementModel.ageEnfant=binding.age.text.toString().toInt()
                signalementModel.adresseEnfant=binding.adresse.text.toString()
                signalementModel.wilayacodeEnfant=wilayacode
                view.findNavController().navigate(R.id.action_signalementForm2Fragment_to_signalementForm3Fragment)
            }
        }
        binding.back.setOnClickListener { view: View ->
            view.findNavController().popBackStack()        }
        binding.back2.setOnClickListener { view: View ->
            view.findNavController().popBackStack()        }

        binding.home.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementForm2Fragment_to_fonctionnalitiesActivity)
        }

    }

    private fun RemplirChamps(signalementModel : SignalementTransfertModel ) {
        if (signalementModel.nomEnfant != null) {
            binding.prenom.setText(signalementModel.nomEnfant)
        }
        if (signalementModel.prenomEnfant != null) {
            binding.nom.setText(signalementModel.prenomEnfant)
        }
        if (signalementModel.ageEnfant != null) {
            binding.age.setText(signalementModel.ageEnfant.toString())
        }
        if (signalementModel.adresseEnfant != null) {
            binding.adresse.setText(signalementModel.adresseEnfant)
        }
        if (signalementModel.wilayacodeEnfant != null) {
            // binding.wilaya.setSelection(signalementModel.wilayacodeEnfant!!)
        }
        when (signalementModel.situationparentEnfant) {
            "mariés" -> {
                binding.maried.setBackgroundColor( Color.parseColor("#CCF28123") )
                binding.maried.setTextColor(Color.WHITE)
            }
            "divorcés" -> {
                binding.divorced.setBackgroundColor( Color.parseColor("#CCF28123") )
                binding.divorced.setTextColor(Color.WHITE)
            }
            "veuf(ve)" -> {
                binding.widower.setBackgroundColor( Color.parseColor("#CCF28123") )
                binding.widower.setTextColor(Color.WHITE)
            }
            "autre"-> {
                binding.other.setBackgroundColor( Color.parseColor("#CCF28123") )
                binding.other.setTextColor(Color.WHITE)
            }
        }
        when (signalementModel.sexeEnfant) {
           // "F" -> binding.sexe.setText(0)
           // "M" -> binding.sexe.setText(1)

        }
    }
}

