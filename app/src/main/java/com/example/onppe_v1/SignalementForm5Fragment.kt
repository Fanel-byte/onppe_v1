package com.example.onppe_v1

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.example.onppe_v1.databinding.FragmentSignalementForm5Binding
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import java.io.IOException


class SignalementForm5Fragment : Fragment() {
    lateinit var binding: FragmentSignalementForm5Binding
    private lateinit var signalementModel: SignalementTransfertModel
    private var sexe=""
    private var wilayacode=0
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
        val instanceDB = AppDatabase.buildDatabase(requireContext())?.getSignalementDao()

        signalementModel = ViewModelProvider(requireActivity()).get(SignalementTransfertModel::class.java)
        binding.next.setOnClickListener { view: View ->
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    if (isNetworkAvailable()) {
                        var signalement = Signalement(
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
                                            addSignalement(signalement)
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
                                                            signalementModel.videoImageSon!!
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
                                                            signalementModel.videoImageSon!!
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
                                                            signalementModel.videoImageSon!!
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
                    else {
                        throw IOException("Erreur : Aucune connexion Internet.")
                    }
                }
                catch (e: IOException) {
                    if (instanceDB != null) {
                        instanceDB.addSignalement(createSignalementTransfert(signalementModel,false))
                     }
                    Toast.makeText(requireActivity(), "Aucune connexion Internet", Toast.LENGTH_SHORT).show()}
                catch (e: Exception) {
                    Toast.makeText(requireActivity(), "Erreur Serveur", Toast.LENGTH_SHORT).show()}
            }
        }

        binding.back.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementForm5Fragment_to_signalementForm4Fragment)
        }
        binding.back2.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementForm5Fragment_to_signalementForm4Fragment)
        }
        binding.back3.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementForm5Fragment_to_signalementForm4Fragment)
        }
        binding.home.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementForm5Fragment_to_fonctionnalitiesActivity)
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
                    findNavController().navigate(R.id.action_signalementForm5Fragment_to_finFormulaireFragment)
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
                    callback(id)
                    Toast.makeText(requireActivity(),"Votre signalement est effectué avec succès", Toast.LENGTH_SHORT).show()
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
                    findNavController().navigate(R.id.action_signalementForm5Fragment_to_finFormulaireFragment)
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
                    findNavController().navigate(R.id.action_signalementForm5Fragment_to_finFormulaireFragment)
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
    private fun createSignalementTransfert(signalementTransfertModel: SignalementTransfertModel, Upload : Boolean): SignalementTransfert {
        return SignalementTransfert(
            upload = Upload,
            //videoImageSon = signalementTransfertModel.videoImageSon,
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
            descriptif = signalementTransfertModel.descriptif
        )
    }

}