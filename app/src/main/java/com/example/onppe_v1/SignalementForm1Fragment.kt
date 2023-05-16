package com.example.onppe_v1

import android.app.DatePickerDialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

lateinit var binding: FragmentSignalementForm1Binding
var motifid =0


class SignalementForm1Fragment : Fragment() {
    lateinit var binding: FragmentSignalementForm1Binding
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
        val signalementtransfert = arguments?.getSerializable("data") as? SignalementTransfert

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
                binding.motif.error = "Veuillez sélectionner un motif"
            } else {
                binding.motif.error = null

                val date = binding.date.text.toString()
                if (signalementtransfert != null) {
                    signalementtransfert.motifid = motifid
                    signalementtransfert.dateincident = date
                    val data = bundleOf("data" to signalementtransfert)
                    view.findNavController().navigate(
                        R.id.action_signalementForm1Fragment_to_signalementForm2Fragment,
                        data
                    )
                } else {
                    val signalementtransfert2 = SignalementTransfert(
                        null,
                        null,
                        null,
                        null,
                        motifid,
                        date,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        false,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                    )
                    val data = bundleOf("data" to signalementtransfert2)
                    view.findNavController().navigate(
                        R.id.action_signalementForm1Fragment_to_signalementForm2Fragment,
                        data
                    )
                }
            }
        }
        binding.next2.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementForm1Fragment_to_signalementForm2Fragment)
        }

    }
}