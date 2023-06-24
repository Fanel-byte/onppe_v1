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
        val dialogBinding = layoutInflater.inflate(R.layout.fragment_popup_help_main,null)
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

        binding.question.setOnClickListener {
            myDialog.show()        }

    }

}
