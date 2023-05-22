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
import com.example.onppe_v1.databinding.FragmentSignalementForm3Binding


class SignalementForm3Fragment : Fragment() {
    lateinit var binding: FragmentSignalementForm3Binding
    private lateinit var signalementModel: SignalementTransfertModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSignalementForm3Binding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signalementModel = ViewModelProvider(requireActivity()).get(SignalementTransfertModel::class.java)


        binding.next.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementForm3Fragment_to_signalementForm4Fragment)
        }
        binding.nextanonym.setOnClickListener { view: View ->
            signalementModel.identitesecrete = true
            view.findNavController().navigate(R.id.action_signalementForm3Fragment_to_signalementForm5Fragment)
        }
        binding.back2.setOnClickListener { view: View ->
            view.findNavController().popBackStack()        }

        binding.back.setOnClickListener { view: View ->
            view.findNavController().popBackStack()        }


        binding.home.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementForm3Fragment_to_fonctionnalitiesActivity)
        }



        binding.enfant.setOnClickListener {
            signalementModel.typesignaleurid = 1
            binding.enfant.setBackgroundColor( Color.parseColor("#CCF28123") )
            binding.moral.setBackgroundColor( Color.parseColor("#F2FAF2") )
            binding.personne.setBackgroundColor( Color.parseColor("#F2FAF2")  )
            binding.representant.setBackgroundColor(Color.parseColor("#F2FAF2"))
        }
        binding.representant.setOnClickListener {
            signalementModel.typesignaleurid = 2
            binding.representant.setBackgroundColor( Color.parseColor("#CCF28123") )
            binding.moral.setBackgroundColor( Color.parseColor("#F2FAF2") )
            binding.personne.setBackgroundColor( Color.parseColor("#F2FAF2")  )
            binding.enfant.setBackgroundColor(Color.parseColor("#F2FAF2"))
        }
        binding.personne.setOnClickListener {
            signalementModel.typesignaleurid = 3
            binding.personne.setBackgroundColor( Color.parseColor("#CCF28123") )
            binding.moral.setBackgroundColor( Color.parseColor("#F2FAF2") )
            binding.enfant.setBackgroundColor( Color.parseColor("#F2FAF2")  )
            binding.representant.setBackgroundColor(Color.parseColor("#F2FAF2"))
        }
        binding.moral.setOnClickListener {
            signalementModel.typesignaleurid = 4
            binding.moral.setBackgroundColor( Color.parseColor("#CCF28123") )
            binding.enfant.setBackgroundColor( Color.parseColor("#F2FAF2") )
            binding.personne.setBackgroundColor( Color.parseColor("#F2FAF2")  )
            binding.representant.setBackgroundColor(Color.parseColor("#F2FAF2"))
        }
    }


    private fun RemplirChamps(signalementModel : SignalementTransfertModel ) {
        when (signalementModel.typesignaleurid) {
            1 -> binding.enfant.setBackgroundColor( Color.parseColor("#CCF28123") )
            2 -> binding.representant.setBackgroundColor( Color.parseColor("#CCF28123") )
            3 -> binding.personne.setBackgroundColor( Color.parseColor("#CCF28123") )
            4 -> binding.moral.setBackgroundColor( Color.parseColor("#CCF28123") )
        }
    }

}
