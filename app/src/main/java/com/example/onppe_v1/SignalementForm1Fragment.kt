package com.example.onppe_v1

import android.app.DatePickerDialog
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
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.example.onppe_v1.databinding.FragmentSignalementForm1Binding
import java.text.SimpleDateFormat
import java.util.*
import java.util.Date
import kotlin.properties.Delegates
import androidx.lifecycle.ViewModelProvider



class SignalementForm1Fragment : Fragment() {
    lateinit var binding: FragmentSignalementForm1Binding
    private var motifid =0
    private lateinit var signalementModel: SignalementTransfertModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignalementForm1Binding.inflate(inflater, container, false)
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
        val items1 = listOf(
            "فقدان الطفل لوالديه وبقائه دون سند عائلي",
            "تعريض الطفل للإهمال أو التشرد",
            "المساس بحقه في التعليم",
            "التسول بالطفل أو تعريضه للتسول",
            "عجز من يقوم برعاية الطفل عن التحكم في تصرفاته",
            "التقصير البين والمتواصل في التربية والرعاية",
            "سوء معاملة الطفل",
            "الطفل ضحية جريمة من ممثله الشرعي",
            "الطفل ضحية جريمة من أي شخص آخر",
            "الاستغلال الجنسي للطفل بمختلف أشكاله",
            "الاستغلال الاقتصادي للطفل",
            "وقوع الطفل ضحية نزاعات مسلحة",)
        val adapter = ArrayAdapter(requireActivity(), R.layout.list_item, items1)
        binding.motif.setAdapter(adapter)
        binding.date.setText(SimpleDateFormat("yyyy-MM-dd").format(Date()))
        // verifier si les champs sont deja enregistrer signalement model :

        binding.motif.threshold = Int.MAX_VALUE // Show all items in the dropdown

        binding.motif.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.motif.showDropDown() // Show dropdown when AutoCompleteTextView gains focus
            }
        }

        RemplirChamps(signalementModel)
        binding.motif.setOnItemClickListener { parent, view, position, id ->
            motifid=position+1
        }


        binding.date.setOnClickListener {
            val cal = Calendar.getInstance()
            val picker =  DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH,month)
                cal.set(Calendar.DAY_OF_MONTH,day)
                binding.date.setText(SimpleDateFormat("yyyy-MM-dd").format(cal.time))

            }

            DatePickerDialog(requireActivity(),R.style.MyDatePickerStyle, picker, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),cal.get(
                Calendar.DAY_OF_MONTH)).show()

        }
        binding.agenda.setOnClickListener {
            val cal = Calendar.getInstance()
            val picker =  DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH,month)
                cal.set(Calendar.DAY_OF_MONTH,day)
                binding.date.setText(SimpleDateFormat("yyyy-MM-dd").format(cal.time))

            }

            DatePickerDialog(requireActivity(),R.style.MyDatePickerStyle, picker, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),cal.get(
                Calendar.DAY_OF_MONTH)).show()


        }


        binding.back.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementForm1Fragment_to_signalementFragment)
        }
        binding.home.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementForm1Fragment_to_fonctionnalitiesActivity)
        }


        binding.next.setOnClickListener { view: View ->
            if (binding.motif.text.toString().isEmpty()) {
                //affichage du pop up
                myDialog.show()
            }
            else{
            val date = binding.date.text.toString()
            signalementModel.dateincident = date
            signalementModel.motifid= motifid
            view.findNavController().navigate(
                R.id.action_signalementForm1Fragment_to_signalementForm2Fragment,
            )
        }
        }

    }
    private fun RemplirChamps(signalementModel : SignalementTransfertModel ){
        if (signalementModel.dateincident != null){
            binding.date.text = signalementModel.dateincident
        }
        if (signalementModel.motifid != null){
            //binding.motif.setText(signalementModel.motifid!! - 1)

        }
    }
}