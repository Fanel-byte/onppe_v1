package com.example.onppe_v1

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.onppe_v1.databinding.FragmentSignalementForm5Binding
import com.google.gson.Gson
import kotlinx.coroutines.*
import okhttp3.MultipartBody
import okio.Buffer
import java.io.IOException


class SignalementForm5Fragment : Fragment() {
    lateinit var binding: FragmentSignalementForm5Binding
    private lateinit var signalementModel: SignalementTransfertModel

    // Exception Handler for Coroutines
    val exceptionHandler = CoroutineExceptionHandler {    coroutineContext, throwable ->
        CoroutineScope(Dispatchers.Main).launch {
            val instanceDB = AppDatabase.buildDatabase(requireContext())?.getSignalementDao()
            instanceDB?.addSignalement(createSignalementTransfert(signalementModel,false))
            Toast.makeText(requireActivity(), "Le signalement sera envoyé une fois la connexion établi", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSignalementForm5Binding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val instanceDB = AppDatabase.buildDatabase(requireContext())?.getSignalementDao()
        signalementModel = ViewModelProvider(requireActivity()).get(SignalementTransfertModel::class.java)
        binding.next.setOnClickListener { view: View ->
            signalementModel.descriptif = binding.description.text.toString()
            CoroutineScope(Dispatchers.Main).launch {
                val signalement = Signalement(
                    null,
                    null,
                    signalementModel.motifid,
                    null,
                    signalementModel.descriptif,
                    null,
                    signalementModel.typesignaleurid,
                    signalementModel.identitesecrete,
                    signalementModel.dateincident
                )

                var enfant = Enfant(
                    null,
                    signalementModel.nomEnfant,
                    signalementModel.prenomEnfant,
                    signalementModel.ageEnfant,
                    signalementModel.sexeEnfant,
                    signalementModel.situationparentEnfant,
                    signalementModel.adresseEnfant,
                    signalementModel.wilayacodeEnfant
                )

                var citoyen = Citoyen(
                    null,
                    signalementModel.nomCitoyen,
                    signalementModel.prenomCitoyen,
                    signalementModel.sexeCitoyen,
                    signalementModel.ageCitoyen,
                    signalementModel.adresseCitoyen,
                    signalementModel.telCitoyen
                )

                addEnfant(enfant) { id ->
                    if (id != null) {
                        signalement.enfantid = id
                        addCitoyen(citoyen) { id ->
                            if (id != null) {
                                signalement.citoyenid = id
                                if (signalementModel.videoImageSon == null) {
                                    addSignalement(signalement,instanceDB)
                                } else {
                                    if (signalementModel.typepreuve == "image") {
                                        addSignalement_avecpreuve(signalement) { id ->
                                            if (id != null) {
                                                var imageInfo = Image(
                                                    signalementModel.DescriptifvideoImageSon,
                                                    id
                                                )
                                                val imageInfoMB =
                                                    MultipartBody.Part.createFormData(
                                                        "image",
                                                        Gson().toJson(imageInfo)
                                                    )
                                                addImg(
                                                    imageInfoMB,
                                                    signalementModel.videoImageSon!!,
                                                    instanceDB
                                                )
                                            }
                                        }
                                    }
                                    if (signalementModel.typepreuve == "son") {
                                        addSignalement_avecpreuve(signalement) { id ->
                                            if (id != null) {
                                                var sonInfo = Son(
                                                    signalementModel.DescriptifvideoImageSon,
                                                    id
                                                )
                                                val sonInfoMB =
                                                    MultipartBody.Part.createFormData(
                                                        "vocal",
                                                        Gson().toJson(sonInfo)
                                                    )
                                                addSon(
                                                    sonInfoMB,
                                                    signalementModel.videoImageSon!!,
                                                    instanceDB
                                                )
                                            }
                                        }
                                    }
                                    if (signalementModel.typepreuve == "video") {
                                        addSignalement_avecpreuve(signalement) { id ->
                                            if (id != null) {
                                                var videoInfo = Video(
                                                    signalementModel.DescriptifvideoImageSon,
                                                    id
                                                )
                                                val imageInfoMB =
                                                    MultipartBody.Part.createFormData(
                                                        "video",
                                                        Gson().toJson(videoInfo)
                                                    )
                                                addVideo(
                                                    imageInfoMB,
                                                    signalementModel.videoImageSon!!,
                                                    instanceDB
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        binding.back.setOnClickListener { view: View ->
            view.findNavController().popBackStack()        }
        binding.back2.setOnClickListener { view: View ->
            view.findNavController().popBackStack()        }
        binding.home.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementForm5Fragment_to_fonctionnalitiesActivity)
        }
    }

    private fun addEnfant(enfant: Enfant,callback: (Int?) -> Unit) {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
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
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
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
    private fun addSignalement(signalement: Signalement, instanceDB: SignalementDao?) {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response =RetrofitService.endpoint.addSignalement(signalement)
            withContext(Dispatchers.Main) {
                // binding.progressBar.visibility= View.INVISIBLE
                if (response.isSuccessful) {
                    instanceDB?.addSignalement(createSignalementTransfert(signalementModel,true))
                    Toast.makeText(requireActivity(),"Votre signalement est effectué avec succès", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_signalementForm5Fragment_to_finFormulaireFragment)}

                else {
                    Toast.makeText(requireActivity(),"une erreur", Toast.LENGTH_SHORT).show()}
            }

        }
    }
    private fun addSignalement_avecpreuve(new: Signalement, callback: (Int?) -> Unit) {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = RetrofitService.endpoint.addSignalement(new)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val id = response.body()
                    callback(id)
                    Toast.makeText(requireActivity(),"Votre signalement est effectué avec succès", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireActivity(), "erreur " + response.code().toString(), Toast.LENGTH_SHORT).show()
                    callback(null)
                }
            }
        }
    }
    private fun addImg(
        image:  MultipartBody.Part,
        imageBody: MultipartBody.Part,
        instanceDB: SignalementDao?) {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response =  RetrofitService.endpoint.addImg(image,imageBody)
            withContext(Dispatchers.Main) {
                if(response.isSuccessful) {
                    instanceDB?.addSignalement(createSignalementTransfert(signalementModel,true))
                    findNavController().navigate(R.id.action_signalementForm5Fragment_to_finFormulaireFragment)
                }
                else {
                    Toast.makeText(requireActivity(),"Une erreur s'est produite",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun addSon(
        son:  MultipartBody.Part,
        sonBody: MultipartBody.Part, instanceDB: SignalementDao?
    ) {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response =  RetrofitService.endpoint.addSon(son,sonBody)
            withContext(Dispatchers.Main) {
                if(response.isSuccessful) {
                    instanceDB?.addSignalement(createSignalementTransfert(signalementModel,true))
                    findNavController().navigate(R.id.action_signalementForm5Fragment_to_finFormulaireFragment)
                }
                else {
                    Toast.makeText(requireActivity(),"Une erreur s'est produite",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun addVideo(
        video:  MultipartBody.Part,
        videoBody: MultipartBody.Part, instanceDB: SignalementDao?
    ) {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response =  RetrofitService.endpoint.addVideo(video,videoBody)
            withContext(Dispatchers.Main) {
                if(response.isSuccessful) {

                    instanceDB?.addSignalement(createSignalementTransfert(signalementModel,true))
                    findNavController().navigate(R.id.action_signalementForm5Fragment_to_finFormulaireFragment)
                }
                else {
                    Toast.makeText(requireActivity(),"Une erreur s'est produite",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCapabilities = connectivityManager.activeNetwork ?: return false

        val activeNetwork = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

        return activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    }
    fun partToByteArray(part: MultipartBody.Part?): ByteArray {
        val sink = Buffer()
        part?.body?.writeTo(sink)
        return sink.readByteArray()
    }

    // Convertir signalementModel to signalement data class:
    private fun createSignalementTransfert(signalementTransfertModel: SignalementTransfertModel, Upload : Boolean): SignalementTransfert {
        return SignalementTransfert(
            upload = Upload,
            videoImageSon = partToByteArray(signalementTransfertModel.videoImageSon),
            DescriptifvideoImageSon = signalementTransfertModel.DescriptifvideoImageSon,
            typepreuve = signalementTransfertModel.typepreuve,
            id = signalementTransfertModel.id,
            motifid = signalementTransfertModel.motifid,
            dateincident = signalementTransfertModel.dateincident,
            nomEnfant = signalementTransfertModel.nomEnfant,
            prenomEnfant = signalementTransfertModel.prenomEnfant,
            ageEnfant = signalementTransfertModel.ageEnfant,
            sexeEnfant = signalementTransfertModel.sexeEnfant,
            situationparentEnfant = signalementTransfertModel.situationparentEnfant,
            adresseEnfant = signalementTransfertModel.adresseEnfant,
            wilayacodeEnfant = signalementTransfertModel.wilayacodeEnfant,
            typesignaleurid = signalementTransfertModel.typesignaleurid,
            identitesecrete = signalementTransfertModel.identitesecrete,
            nomCitoyen = signalementTransfertModel.nomCitoyen,
            prenomCitoyen = signalementTransfertModel.prenomCitoyen,
            sexeCitoyen = signalementTransfertModel.sexeCitoyen,
            ageCitoyen = signalementTransfertModel.ageCitoyen,
            adresseCitoyen = signalementTransfertModel.adresseCitoyen,
            telCitoyen = signalementTransfertModel.telCitoyen,
            descriptif = signalementTransfertModel.descriptif,
            statut = if (Upload) "envoyé en attente de réponse" else "en attente d envoi"

        )
    }

}