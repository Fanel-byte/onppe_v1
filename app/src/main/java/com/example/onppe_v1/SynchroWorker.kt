package com.example.onppe_v1

import android.content.Context
import android.provider.Settings
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.edit
import androidx.navigation.findNavController
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import java.util.HashMap

class SynchroWorker (val context: Context, val params: WorkerParameters): CoroutineWorker(context,params) {
    val sharedPreferences = context.getSharedPreferences("signaleur_infos", Context.MODE_PRIVATE)
    val deviceId =  sharedPreferences.getString("deviceId", "")
    var token:String = ""
    // Define the exception handler
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        // Handle the exception here, for example, log it or return Result.failure()
        //Result.failure()
        Result.retry()
    }
    override suspend fun doWork(): Result {
        var res =  Result.retry()
        val signalements = AppDatabase.buildDatabase(context)?.getSignalementDao()
            ?.getSignalementToSynchronize()

        if (signalements.isNullOrEmpty()) {
            // liste vide donc pas de signalements a inserer dans le serveur
            res = Result.success()
        }
        else {
            // inserer chaque signalement :
            for (sign in signalements) {
                CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                    val signalement = Signalement(
                        null,
                        null,
                        sign.motifid,
                        null,
                        sign.descriptif,
                        null,
                        3,
                        sign.identitesecrete,
                        sign.dateincident,
                        sign.lieudanger,
                        sign.longitudesignaleur,
                        sign.latitudesignaleur
                    )

                    var enfant = Enfant(
                        null,
                        sign.nomEnfant,
                        sign.prenomEnfant,
                        sign.ageEnfant,
                        sign.sexeEnfant,
                        sign.situationparentEnfant,
                        sign.adresseEnfant,
                        sign.wilayacodeEnfant
                    )

                    addEnfant(enfant) { id ->
                        if (id != null) {
                            signalement.enfantid = id
                            // generate token if it does't exist
                            val pref_token = context.getSharedPreferences("token_db", Context.MODE_PRIVATE)
                            if (pref_token.getString("token",null)==null) {
                                generateToken(deviceId!!)
                            }
                            verifieridcitoyen(deviceId!!, sign)
                            signalement.citoyenid = deviceId
                            addSignalement(signalement) { id ->
                                if (id != null) {
                                    // changer l etat du signalement a envoyer
                                    sign.upload = 1
                                    sign.signalementId = id
                                    AppDatabase.buildDatabase(context)?.getSignalementDao()?.updateSynchronize(sign)
                                }
                            }
                        }
                    }
                }
            }
        }
        return res
    }

    fun addEnfant(enfant: Enfant,callback: (Int?) -> Unit) {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = RetrofitService.endpoint.createEnfant(enfant)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val id = response.body()
                    callback(id)
                } else {
                    callback(null)
                }
            }
        }
    }
    private fun verifieridcitoyen(deviceId: String , citoyen: SignalementTransfert ) {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = RetrofitService.endpoint.verifieridCitoyen(deviceId)
            withContext(Dispatchers.Main) {
                // binding.progressBar.visibility= View.INVISIBLE
                if (response.isSuccessful) {
                    if (response.body() == "0") {
                        var citoyen = Citoyen(
                            deviceId,
                            citoyen.nomCitoyen,
                            citoyen.prenomCitoyen,
                            citoyen.sexeCitoyen,
                            citoyen.ageCitoyen,
                            citoyen.adresseCitoyen,
                            citoyen.telCitoyen
                        )
                        addCitoyen(citoyen)
                    }
                }
                else {

                }
            }

        }
    }
    private fun addCitoyen(citoyen: Citoyen) {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = RetrofitService.endpoint.createCitoyen(citoyen)
            withContext(Dispatchers.Main) {
                // binding.progressBar.visibility= View.INVISIBLE
                if (response.isSuccessful) {
                }
                else {
                }
            }

        }
    }
    private fun addSignalement(new: Signalement, callback: (Int?) -> Unit) {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = RetrofitService.endpoint.addSignalement(new)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val id = response.body()
                    callback(id)
                } else {
                    callback(null)
                }
            }
        }

    }

    // Dans le cas ou le token n'est pas enconre genere :
    private fun generateToken(deviceId: String) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            token =  task.result
            val data = HashMap<String,String>()
            // Change this to device ID
            data.put("deviceId",deviceId)
            data.put("token",token)
            addToken(data)

        })
    }
    private fun addToken(data: HashMap<String, String>) {
        val  exceptionHandler =   CoroutineExceptionHandler { coroutineContext, throwable ->
            CoroutineScope(Dispatchers.Main).launch {
            }
        }
        CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
            val result = RetrofitService.endpoint.addToken(data)
            withContext(Dispatchers.Main) {
                if(result.isSuccessful) {
                    val pref = context.getSharedPreferences("token_db", Context.MODE_PRIVATE)
                    pref.edit {
                        putString("token",token)
                    }
                    Toast.makeText(context,"Token ajouté",Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(context,"Une erreur s'est produite",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}