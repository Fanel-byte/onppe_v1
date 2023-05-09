package com.example.onppe_v1

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.example.onppe_v1.databinding.FragmentSignalementForm2Binding
import com.example.onppe_v1.databinding.FragmentSignalementForm3Binding
import kotlinx.coroutines.NonDisposableHandle.parent
import kotlin.properties.Delegates


class SignalementForm2Fragment : Fragment() {

    lateinit var binding: FragmentSignalementForm2Binding
    var sexe=""
    var wilayacode by Delegates.notNull<Int>()
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
        var signalementtransfert = arguments?.getSerializable("data") as SignalementTransfert
        val items1 = listOf(
            "أنثى",
            "ذكر ")
        val adapter = ArrayAdapter(requireActivity(), R.layout.list_item, items1)
        binding.sexe.setAdapter(adapter)

        val wilayas = listOf(
            "أدرار",
            "الشلف",
            "الأغواط",
            "أم البواقي",
            "باتنة",
            "بجاية",
            "بسكرة",
            "بشار",
            "البليدة",
            "البويرة",
            "تمنراست",
            "تبسة",
            "تلمسان",
            "تيارت",
            "تيزي وزو",
            "الجزائر",
            "الجلفة",
            "جيجل",
            "سطيف",
            "سعيدة",
            "سكيكدة",
            "سيدي بلعباس",
            "عنابة",
            "قالمة",
            "قسنطينة",
            "المدية",
            "مستغانم",
            "المسيلة",
            "معسكر",
            "ورقلة",
            "وهران",
            "البيض",
            "إليزي",
            "برج بوعريريج",
            "بومرداس",
            "الطارف",
            "تندوف",
            "تيسمسيلت",
            "الوادي",
            "خنشلة",
            "سوق أهراس",
            "تيبازة",
            "ميلة",
            "عين الدفلى",
            "النعامة",
            "عين تموشنت",
            "غرداية",
            "غليزان")
        val adapter2 = ArrayAdapter(requireActivity(), R.layout.list_item, wilayas)
        binding.wilaya.setAdapter(adapter2)
        binding.wilaya.setOnItemClickListener { parent, view, position, id ->
            wilayacode=position+1 }

        binding.sexe.setOnItemClickListener { parent, view, position, id ->
            if (position == 0) {
                sexe = "F"
            } else {
                sexe = "M"
            }
        }

        binding.back.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementForm2Fragment_to_signalementForm1Fragment)
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
            signalementtransfert.situationparentEnfant="mariés"
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
            signalementtransfert.situationparentEnfant="divorcés"
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
            signalementtransfert.situationparentEnfant="veuf(ve)"
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
            signalementtransfert.situationparentEnfant="autre"
        }
        binding.next.setOnClickListener { view: View ->
            signalementtransfert.prenomEnfant=binding.prenom.text.toString()
            signalementtransfert.nomEnfant=binding.nom.text.toString()
            signalementtransfert.sexeEnfant=sexe
            signalementtransfert.ageEnfant=binding.age.text.toString().toInt()
            signalementtransfert.adresseEnfant=binding.adresse.text.toString()
            signalementtransfert.wilayacodeEnfant=wilayacode
            val data = bundleOf("data" to signalementtransfert)
            view.findNavController().navigate(R.id.action_signalementForm2Fragment_to_signalementForm3Fragment,data)
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



    }


}