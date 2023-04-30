package com.example.onppe_v1

import android.app.DatePickerDialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.example.onppe_v1.databinding.FragmentSignalementForm1Binding
import java.text.SimpleDateFormat
import java.util.*

lateinit var binding: FragmentSignalementForm1Binding
class SignalementForm1Fragment : Fragment() {
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
        binding.cause.setAdapter(adapter)
        binding.next.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementForm1Fragment_to_signalementForm3Fragment)
        }

        binding.date.setOnClickListener {
            val cal = Calendar.getInstance()
            val picker =  DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH,month)
                cal.set(Calendar.DAY_OF_MONTH,day)
                binding.date.setText(SimpleDateFormat("yyyy-MM-dd").format(cal.time))
            }

            DatePickerDialog(requireActivity(),R.style.DatePickerStyle, picker, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),cal.get(
                Calendar.DAY_OF_MONTH)).show()

        }

        binding.next.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementForm1Fragment_to_signalementForm3Fragment)
        }

    }
}