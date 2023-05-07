package com.example.onppe_v1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.onppe_v1.databinding.FragmentSignalementForm5Binding
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody


class SignalementForm5Fragment : Fragment() {


    lateinit var binding: FragmentSignalementForm5Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignalementForm5Binding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var signalementtransfert = arguments?.getSerializable("data") as SignalementTransfert
        binding.next.setOnClickListener { view: View ->

            signalementtransfert.descriptif=binding.description.text.toString()

            var signalement = Signalement(
                null,
                null,
                signalementtransfert.motifid,
                null,
                signalementtransfert.descriptif,
                null,
                signalementtransfert.typesignaleurid,
                signalementtransfert.identitesecrete,
                signalementtransfert.dateincident
            )

            var enfant = Enfant(
                null,
                signalementtransfert.nomEnfant,
                signalementtransfert.prenomEnfant,
                signalementtransfert.ageEnfant,
                signalementtransfert.sexeEnfant,
                signalementtransfert.situationparentEnfant,
                signalementtransfert.adresseEnfant,
                signalementtransfert.wilayacodeEnfant
            )

            var citoyen = Citoyen(
                null,
                signalementtransfert.nomCitoyen,
                signalementtransfert.prenomCitoyen,
                signalementtransfert.sexeCitoyen,
                signalementtransfert.ageCitoyen,
                signalementtransfert.adresseCitoyen,
                signalementtransfert.telCitoyen
            )

            addEnfant(enfant){ id ->
                if (id != null) {
                    signalement.enfantid=id
                    addCitoyen(citoyen){ id ->
                        if (id != null) {
                            signalement.citoyenid=id
                            if(signalementtransfert.videoImageSon==null)
                            {
                            addSignalement(signalement)
                            }
                            else{
                                if(signalementtransfert.typepreuve=="image")
                                {
                                    addSignalement_avecpreuve(signalement) { id ->
                                        if (id != null) {
                                            var imageInfo = Image(
                                                signalementtransfert.DescriptifvideoImageSon,
                                                id
                                            )
                                            val imageInfoMB = MultipartBody.Part.createFormData("image", Gson().toJson(imageInfo))
                                            addImg(imageInfoMB,
                                                signalementtransfert.videoImageSon!!
                                            )
                                        }
                                    }
                                }
                                if (signalementtransfert.typepreuve=="son"){
                                    addSignalement_avecpreuve(signalement) { id ->
                                        if (id != null) {
                                            var sonInfo = Son(
                                                signalementtransfert.DescriptifvideoImageSon,
                                                id
                                            )
                                            val sonInfoMB = MultipartBody.Part.createFormData("vocal", Gson().toJson(sonInfo))
                                            addSon(sonInfoMB,signalementtransfert.videoImageSon!!)
                                        }
                                    }
                                }
                                if (signalementtransfert.typepreuve=="video"){
                                    addSignalement_avecpreuve(signalement) { id ->
                                        if (id != null) {
                                            var videoInfo = Video(
                                                signalementtransfert.DescriptifvideoImageSon,
                                                id
                                            )
                                            val imageInfoMB = MultipartBody.Part.createFormData("video", Gson().toJson(videoInfo))
                                            addVideo(imageInfoMB,signalementtransfert.videoImageSon!!)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            view.findNavController().navigate(R.id.action_signalementForm5Fragment_to_finFormulaireFragment)
        }
        binding.back.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementForm5Fragment_to_signalementForm4Fragment)
        }
    }

    private fun addEnfant(enfant: Enfant,callback: (Int?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitService.endpoint.createEnfant(enfant)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val id = response.body()
                    callback(id)
                } else {
                    Toast.makeText(requireActivity(), "erreur " + response.code().toString(), Toast.LENGTH_SHORT).show()
                    callback(null)
                }
            }
        }
    }
    private fun addCitoyen(citoyen: Citoyen,callback: (Int?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {

            val response =RetrofitService.endpoint.createCitoyen(citoyen)
            withContext(Dispatchers.Main) {
                // binding.progressBar.visibility= View.INVISIBLE
                if (response.isSuccessful) {
                    val id = response.body()
                    callback(id)
                }
                else {
                    Toast.makeText(requireActivity(),"une erreur", Toast.LENGTH_SHORT).show()
                    callback(null)
                }
            }

        }
    }
    private fun addSignalement(signalement: Signalement) {
        CoroutineScope(Dispatchers.IO).launch {

            val response =RetrofitService.endpoint.addSignalement(signalement)
            withContext(Dispatchers.Main) {
                // binding.progressBar.visibility= View.INVISIBLE
                if (response.isSuccessful) {
                    Toast.makeText(requireActivity(),"Votre signalement est effectué avec succès", Toast.LENGTH_SHORT).show()}

                else {
                    Toast.makeText(requireActivity(),"une erreur", Toast.LENGTH_SHORT).show()}
            }

        }
    }

    private fun addSignalement_avecpreuve(new: Signalement, callback: (Int?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitService.endpoint.addSignalement(new)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val id = response.body()
                    Toast.makeText(requireActivity(),"Votre signalement est effectué avec succès", Toast.LENGTH_SHORT).show()
                    callback(id)
                } else {
                    Toast.makeText(requireActivity(), "erreur " + response.code().toString(), Toast.LENGTH_SHORT).show()
                    callback(null)
                }
            }
        }
    }
    private fun addImg(image :  MultipartBody.Part ,imageBody: MultipartBody.Part) {
        CoroutineScope(Dispatchers.IO).launch {
            val response =  RetrofitService.endpoint.addImg(image,imageBody)
            withContext(Dispatchers.Main) {
                if(response.isSuccessful) {
                }
                else {
                    Toast.makeText(requireActivity(),"Une erreur s'est produite",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun addSon(son :  MultipartBody.Part ,sonBody: MultipartBody.Part) {
        CoroutineScope(Dispatchers.IO).launch {
            val response =  RetrofitService.endpoint.addSon(son,sonBody)
            withContext(Dispatchers.Main) {
                if(response.isSuccessful) {
                }
                else {
                    Toast.makeText(requireActivity(),"Une erreur s'est produite",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun addVideo(video :  MultipartBody.Part ,videoBody: MultipartBody.Part) {
        CoroutineScope(Dispatchers.IO).launch {
            val response =  RetrofitService.endpoint.addVideo(video,videoBody)
            withContext(Dispatchers.Main) {
                if(response.isSuccessful) {
                }
                else {
                    Toast.makeText(requireActivity(),"Une erreur s'est produite",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}