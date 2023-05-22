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
import androidx.navigation.findNavController


class SuiviSignalementFragment : Fragment() {

    lateinit var binding: FragmentSuiviSignalementBinding
    lateinit var signalementsModel: SignalementsModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSuiviSignalementBinding.inflate(inflater, container, false)
        val view = binding.root
        
        
        binding.home.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_suivisignalementfragment_to_fonctionnalitiesActivity)
        }
        binding.back.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_suivisignalementfragment_to_signalementFragment)
        }
        
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val instanceDB = AppDatabase.buildDatabase(requireContext())?.getSignalementDao()
        signalementsModel = ViewModelProvider(requireActivity()).get(SignalementsModel::class.java)
        //GetSignalements()

        // Get from SQL LITE and add it in signalementsModel
        signalementsModel.signalements = instanceDB!!.getSignalement()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity() , RecyclerView.VERTICAL,false)
        binding.recyclerView.adapter = SuiviSignalementAdapter(requireActivity(), signalementsModel.signalements)
        val itemDecor = DividerItemDecoration(requireActivity(),1)
        binding.recyclerView.addItemDecoration(itemDecor)

        /*
        if(signalementModel.signalements.isEmpty()) {
            // Get data from the server
        }else {
            binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity() , RecyclerView.VERTICAL,false)
            binding.recyclerView.adapter = SuiviSignalementAdapter(requireActivity(), instanceDB!!.getSignalement())
            val itemDecor = DividerItemDecoration(requireActivity(),1)
            binding.recyclerView.addItemDecoration(itemDecor)}
         */
    }
    /*
    private fun GetSignalements() {    CoroutineScope(Dispatchers.IO).launch {
        val response = RetrofitService.endpoint.getsignalements(1)
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
                else {view?.findNavController()?.navigate(R.id.action_mainFragment_to_nosuiviFragment)}
            }else{
                Toast.makeText(requireActivity(), "erreur lors de l'acces aux serveur : " +response.code().toString() , Toast.LENGTH_SHORT).show()
            }
        }
    }}
     */

    }

