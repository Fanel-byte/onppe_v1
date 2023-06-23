package com.example.onppe_v1

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationManager

import android.os.Bundle
import android.provider.Settings
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
import com.example.onppe_v1.databinding.FragmentSignalementFormSignaleurBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices


class SignalementFormSignaleurFragment : Fragment() {
    lateinit var binding: FragmentSignalementFormSignaleurBinding
    private lateinit var signalementModel: SignalementTransfertModel
    private var sexe:String = ""
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignalementFormSignaleurBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dialogBinding4 = layoutInflater.inflate(R.layout.fragment_help_form,null)
        val myDialog4 = Dialog(requireActivity())
        myDialog4.setContentView(dialogBinding4)
        myDialog4.setCancelable(true)
        myDialog4.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // Récupérer la taille de l'écran
        val displayMetrics2 = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics2)







        val dialogBinding1 = layoutInflater.inflate(R.layout.fragment_popup_window,null)
        val dialogBinding2 = layoutInflater.inflate(R.layout.popup_window_localisation,null)
        val myDialog = Dialog(requireActivity())
        myDialog.setContentView(dialogBinding1)
        myDialog.setCancelable(true)
        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // Récupérer la taille de l'écran
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)

        val width = (displayMetrics.widthPixels * 0.75).toInt()
        val height =  WindowManager.LayoutParams.WRAP_CONTENT

        val sharedPreferences = requireActivity().getSharedPreferences("signaleur_infos", Context.MODE_PRIVATE)
        if (! RemplirChamps(sharedPreferences)) {
            // La premiere fois :
            val editor = sharedPreferences.edit()
            val deviceId = Settings.Secure.getString(requireContext().contentResolver, Settings.Secure.ANDROID_ID)
            editor.putString("deviceId", deviceId)
            editor.apply()
        }
        // Définir la taille de la fenêtre du dialog
        myDialog.window?.setLayout(width, height)
        signalementModel = ViewModelProvider(requireActivity()).get(SignalementTransfertModel::class.java)
        val items1 = resources.getStringArray(R.array.gender)
        val adapter = ArrayAdapter(requireActivity(), R.layout.list_item, items1)
        binding.sexe.setAdapter(adapter)
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


        binding.back.setOnClickListener {
            view.findNavController().popBackStack()        }

        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        binding.next.setOnClickListener {
            if ((binding.prenom.text.toString().isEmpty())||(binding.nom.text.toString().isEmpty())||(binding.tel.text.toString().isEmpty())||(binding.sexe.text.toString().isEmpty())||(binding.age.text.toString().isEmpty())||(binding.adresse.text.toString().isEmpty())){
                myDialog.setContentView(dialogBinding1)
                myDialog.show()
            }else {
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    myDialog.setContentView(dialogBinding2)
                    myDialog.show()
                } else {
                    fusedLocationClient =
                        LocationServices.getFusedLocationProviderClient(requireActivity())
                    fusedLocationClient.getCurrentLocation(
                        LocationRequest.PRIORITY_HIGH_ACCURACY,
                        null
                    )
                        .addOnSuccessListener { location: Location? ->
                            location?.let {
                                val latitude = location.latitude
                                val longitude = location.longitude
                                signalementModel.latitudesignaleur = latitude
                                signalementModel.longitudesignaleur = longitude
                            }
                        }

                    signalementModel.nomCitoyen = binding.nom.text.toString()
                    signalementModel.prenomCitoyen = binding.prenom.text.toString()
                    signalementModel.ageCitoyen = if (binding.age.text.toString().isNotEmpty()) {
                        binding.age.text.toString().toInt()
                    } else {
                        null
                    }
                    signalementModel.sexeCitoyen = sexe
                    signalementModel.adresseCitoyen = binding.adresse.text.toString()
                    signalementModel.telCitoyen = binding.tel.text.toString()

                    val editor = sharedPreferences.edit()
                    editor.putString("nomCitoyen", signalementModel.nomCitoyen)
                    editor.putString("prenomCitoyen", signalementModel.prenomCitoyen)
                    editor.putString("ageCitoyen", signalementModel.ageCitoyen.toString())
                    editor.putString("adresseCitoyen", signalementModel.adresseCitoyen)
                    editor.putString("telCitoyen", signalementModel.telCitoyen)
                    editor.apply()
                    view.findNavController()
                        .navigate(R.id.action_signalementFormSignaleurFragment_to_signalementFormEnfantFragment)
                }
            }
        }
        binding.question.setOnClickListener {
         myDialog4.show()
        }
    }
    private fun  RemplirChamps(sharedPreferences : SharedPreferences) : Boolean{
        val nomCitoyen = sharedPreferences.getString("nomCitoyen", "")
        if (nomCitoyen == ""){
            return false
        }else {
            val prenomCitoyen = sharedPreferences.getString("prenomCitoyen", "")
            val ageCitoyen = sharedPreferences.getString("ageCitoyen", "")
            val adresseCitoyen = sharedPreferences.getString("adresseCitoyen", "")
            val telCitoyen = sharedPreferences.getString("telCitoyen", "")
            binding.nom.setText(nomCitoyen)
            binding.prenom.setText(prenomCitoyen)
            binding.age.setText(ageCitoyen)
            binding.adresse.setText(adresseCitoyen)
            binding.tel.setText(telCitoyen)
            return true
        }
    }

}