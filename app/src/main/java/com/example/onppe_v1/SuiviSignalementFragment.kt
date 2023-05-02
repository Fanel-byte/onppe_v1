package com.example.onppe_v1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.onppe_v1.databinding.FragmentSuiviSignalementBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.widget.Toast


class SuiviSignalementFragment : Fragment() {

    lateinit var binding: FragmentSuiviSignalementBinding
    lateinit var signalementModel: SignalementModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSuiviSignalementBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signalementModel = ViewModelProvider(requireActivity()).get(SignalementModel::class.java)
        GetSignalements()

        if(signalementModel.signalements.isEmpty()) {
            // Get data from the server
        }else {
            val adapter = SuiviSignalementAdapter(requireActivity(),signalementModel.signalements)
            binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity() ,
                RecyclerView.VERTICAL,false)
            binding.recyclerView.adapter = SuiviSignalementAdapter(requireActivity(),signalementModel.signalements)
            val itemDecor = DividerItemDecoration(requireActivity(),1)
            binding.recyclerView.addItemDecoration(itemDecor)}
    }
    private fun GetSignalements() {    CoroutineScope(Dispatchers.IO).launch {
        val response = RetrofitService.endpoint.getsignalements()
        withContext(Dispatchers.Main){

            if(response.isSuccessful){
                var signalements = response.body()
                if (signalements != null) {
                    signalementModel.signalements = signalements
                    binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity() ,
                        RecyclerView.VERTICAL,false)
                    binding.recyclerView.adapter = SuiviSignalementAdapter(requireActivity(),signalements )
                    val itemDecor = DividerItemDecoration(requireActivity(),1)
                    binding.recyclerView.addItemDecoration(itemDecor)
                }
            }else{
                Toast.makeText(requireActivity(), "ERREUR " +response.code().toString() , Toast.LENGTH_SHORT).show()
            }
        }
    }

    } }
