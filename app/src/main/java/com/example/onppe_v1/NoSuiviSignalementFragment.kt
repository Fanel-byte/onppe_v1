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
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.onppe_v1.databinding.FragmentNoSuiviSignalementBinding
import com.example.onppe_v1.databinding.FragmentSuiviSignalementDetailBinding


class NoSuiviSignalementFragment : Fragment() {

    lateinit var binding: FragmentNoSuiviSignalementBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentNoSuiviSignalementBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dialogBinding2 = layoutInflater.inflate(R.layout.fragment_help_suivi,null)
        val myDialog2 = Dialog(requireActivity())
        myDialog2.setContentView(dialogBinding2)
        myDialog2.setCancelable(true)
        myDialog2.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // Récupérer la taille de l'écran
        val displayMetrics2 = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics2)
        val width = (displayMetrics2.widthPixels * 0.75).toInt()
        val height =  WindowManager.LayoutParams.WRAP_CONTENT
        // Définir la taille de la fenêtre du dialog
        myDialog2.window?.setLayout(width, height)
        binding.question.setOnClickListener {
            myDialog2.show()        }

        //Pour ne pas avoir le bouton retour
        (requireActivity() as AppCompatActivity?)?.supportActionBar!!.setDisplayHomeAsUpEnabled (false)

    }


}
