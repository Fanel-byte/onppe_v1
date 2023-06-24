package com.example.onppe_v1

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.findNavController
import java.text.SimpleDateFormat
import java.util.*
import java.util.Date
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.onppe_v1.databinding.FragmentSignalementFormInfosBinding
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer
import java.io.InputStream
import android.provider.OpenableColumns
import androidx.core.content.edit
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging


class SignalementFormInfosFragment : Fragment() {
    lateinit var binding: FragmentSignalementFormInfosBinding
    private var motifid =0
    private lateinit var signalementModel: SignalementTransfertModel
    private val PICK_FILE_REQUEST_CODE = 2
    private var preuve = false
    private val RESULT_OK = 1
    var token:String = ""

    // Exception Handler for Coroutines
    val exceptionHandler = CoroutineExceptionHandler {    coroutineContext, throwable ->
        CoroutineScope(Dispatchers.Main).launch {
            // verify that the fragment is contained in an activity
            if (isAdded) {
                val instanceDB = AppDatabase.buildDatabase(requireContext())?.getSignalementDao()
                instanceDB?.addSignalement(createSignalementTransfert(signalementModel,0))
                view?.findNavController()?.navigate(R.id.action_signalementFormInfosFragment_to_finFormulaireSansCnxFragment)

            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignalementFormInfosBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val instanceDB = AppDatabase.buildDatabase(requireContext())?.getSignalementDao()
        val deviceId: String = Settings.Secure.getString(requireContext().contentResolver, Settings.Secure.ANDROID_ID)
        val dialogBinding = layoutInflater.inflate(R.layout.fragment_popup_window,null)
        val dialogBinding2 = layoutInflater.inflate(R.layout.fragment_help_form,null)
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
        val items1 = resources.getStringArray(R.array.motifs).toList()
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

        binding.question3.setOnClickListener {
            myDialog.setContentView(dialogBinding2)
            myDialog.show()        }
        binding.back3.setOnClickListener { view: View ->
            view.findNavController().popBackStack()
        }


        binding.next2.setOnClickListener { view: View ->
            if ((binding.motif.text.toString().isEmpty())|| (binding.lieudanger.text.toString().isEmpty())) {
                //affichage du pop up
                myDialog.show()
            }
            else{
                val date = binding.date.text.toString()
                signalementModel.dateincident = date
                signalementModel.motifid= motifid
                signalementModel.descriptif = binding.description.text.toString()
                CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                    val signalement = Signalement(
                        null,
                        null,
                        signalementModel.motifid,
                        null,
                        signalementModel.descriptif,
                        null,
                        3,
                        signalementModel.identitesecrete,
                        signalementModel.dateincident,
                        signalementModel.lieudanger,
                        signalementModel.longitudesignaleur,
                        signalementModel.latitudesignaleur,
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
                    addEnfant(enfant) { id ->
                        if (id != null) {
                            signalement.enfantid = id
                            // generate token if it does't exist
                            val pref = requireActivity().getSharedPreferences("token_db", Context.MODE_PRIVATE)
                            if (pref.getString("token",null)==null) {
                                generateToken(deviceId)
                            }
                            verifieridcitoyen(deviceId)
                            signalement.citoyenid = deviceId

                            if (signalementModel.videoImageSon == null) {
                                addSignalement(signalement,instanceDB) { id ->
                                    if (id != null) {
                                        if (preuve){
                                            val signalementIdMB =
                                                MultipartBody.Part.createFormData(
                                                    "signalementid",
                                                    Gson().toJson(id)
                                                )
                                            if (signalementModel.multipartBodyPreuve != null){
                                                addPreuve(signalementModel.multipartBodyPreuve!!, signalementIdMB , instanceDB)
                                            }
                                        }
                                        else {
                                            view.findNavController().navigate(R.id.action_signalementFormInfosFragment_to_finFormulaireFragment)
                                        }
                                    }
                                }
                            } else {
                                addSignalement(signalement,instanceDB) { id ->
                                    if (id != null) {
                                        if (signalementModel.typepreuve == "image") {
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
                                                signalementModel.videoImageSon!!,instanceDB
                                            )
                                        }

                                        if (signalementModel.typepreuve == "son") {
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
                                        if (signalementModel.typepreuve == "video") {
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
        binding.addpreuve.setOnClickListener{
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "application/pdf,application/msword" // Filtrer les types de fichiers
            startActivityForResult(intent, PICK_FILE_REQUEST_CODE)
        }

    }

    private fun generateToken(deviceId: String) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            token =  task.result
            val data = HashMap<String,String>()
            // Change this to device ID
            data.put("token",token)
            data.put("citoyenid",deviceId)
            addToken(data)

        })
    }

    private fun addToken(data: HashMap<String, String>) {
        val  exceptionHandler =   CoroutineExceptionHandler { coroutineContext, throwable ->
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(requireContext(), "Exception: ${throwable.message}", Toast.LENGTH_SHORT).show()
            }
        }
        CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
            val result = RetrofitService.endpoint.addToken(data)
            withContext(Dispatchers.Main) {
                if(result.isSuccessful) {
                    val pref = requireActivity().getSharedPreferences("token_db", Context.MODE_PRIVATE)
                    pref.edit {
                        putString("token",token)
                    }
                    Toast.makeText(requireContext(),"Token ajouté",Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(requireContext(),"22222",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val fileUri = data.data
            if (fileUri != null) {
                val pdfBytes = readBytesFromUri(fileUri)
                if (pdfBytes != null) {
                    binding.fileName.setText(getFileName(fileUri))
                    val requestBody = pdfBytes.toRequestBody("application/pdf".toMediaTypeOrNull())
                    signalementModel.multipartBodyPreuve = MultipartBody.Part.createFormData("path", "file.pdf", requestBody)
                    preuve = true
                }
            }
        }
    }

    fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = requireActivity().contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    val displayNameColumnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (displayNameColumnIndex >= 0) {
                        result = cursor.getString(displayNameColumnIndex)
                    }
                }
            } finally {
                cursor?.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != -1) {
                result = result?.substring(cut!! + 1)
            }
        }
        return result
    }

    private fun readBytesFromUri(uri: Uri): ByteArray? {
        var inputStream: InputStream? = null
        try {
            inputStream = requireActivity().contentResolver.openInputStream(uri)
            return inputStream?.readBytes()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            inputStream?.close()
        }
        return null
    }
    private fun addEnfant(enfant: Enfant,callback: (Int?) -> Unit) {
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
    private fun verifieridcitoyen(deviceId: String) {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = RetrofitService.endpoint.verifieridCitoyen(deviceId)
            withContext(Dispatchers.Main) {
                // binding.progressBar.visibility= View.INVISIBLE
                if (response.isSuccessful) {
                    if (response.body() == "0") {
                        var citoyen = Citoyen(
                            deviceId,
                            signalementModel.nomCitoyen,
                            signalementModel.prenomCitoyen,
                            signalementModel.sexeCitoyen,
                            signalementModel.ageCitoyen,
                            signalementModel.adresseCitoyen,
                            signalementModel.telCitoyen
                        )
                        addCitoyen(citoyen)
                    }
                }
                else {
                    Toast.makeText(requireActivity(), "une erreur", Toast.LENGTH_SHORT)
                        .show()
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
                    Toast.makeText(
                        requireActivity(),
                        "erreur " + response.code().toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

        }
    }
    private fun addSignalement(new: Signalement,instanceDB: SignalementDao?, callback: (Int?) -> Unit) {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = RetrofitService.endpoint.addSignalement(new)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val id = response.body()
                    signalementModel.signalementId = id
                    instanceDB?.addSignalement(createSignalementTransfert(signalementModel,1))
                    callback(id)
                } else {
                    Toast.makeText(requireActivity(), "erreur " + response.code().toString(), Toast.LENGTH_SHORT).show()
                    callback(null)
                }
            }
        }
    }
    private fun addImg(image:  MultipartBody.Part, imageBody: MultipartBody.Part, instanceDB: SignalementDao?) {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response =  RetrofitService.endpoint.addImg(image,imageBody)
            withContext(Dispatchers.Main) {
                if(response.isSuccessful) {
                    instanceDB?.addSignalement(createSignalementTransfert(signalementModel,1))
                    findNavController().navigate(R.id.action_signalementFormInfosFragment_to_finFormulaireFragment)
                }
                else {
                    Toast.makeText(requireActivity(),"Une erreur s'est produite",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun addSon(son:  MultipartBody.Part, sonBody: MultipartBody.Part, instanceDB: SignalementDao?) {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response =  RetrofitService.endpoint.addSon(son,sonBody)
            withContext(Dispatchers.Main) {
                if(response.isSuccessful) {
                    instanceDB?.addSignalement(createSignalementTransfert(signalementModel,1))
                    findNavController().navigate(R.id.action_signalementFormInfosFragment_to_finFormulaireFragment)
                }
                else {
                    Toast.makeText(requireActivity(),"Une erreur s'est produite",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun addPreuve(preuve:  MultipartBody.Part, signalementid: MultipartBody.Part , instanceDB: SignalementDao?) {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response =  RetrofitService.endpoint.addPreuve(preuve , signalementid)
            withContext(Dispatchers.Main) {
                if(response.isSuccessful) {
                    instanceDB?.addSignalement(createSignalementTransfert(signalementModel,1))
                    findNavController().navigate(R.id.action_signalementFormInfosFragment_to_finFormulaireFragment)
                }
                else {
                    Toast.makeText(requireActivity(),"Une erreur s'est produite",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun addVideo(video:  MultipartBody.Part, videoBody: MultipartBody.Part, instanceDB: SignalementDao?) {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response =  RetrofitService.endpoint.addVideo(video,videoBody)
            withContext(Dispatchers.Main) {
                if(response.isSuccessful) {
                    instanceDB?.addSignalement(createSignalementTransfert(signalementModel,1))
                    findNavController().navigate(R.id.action_signalementFormInfosFragment_to_finFormulaireFragment)
                }
                else {
                    Toast.makeText(requireActivity(),"Une erreur s'est produite",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    fun partToByteArray(part: MultipartBody.Part?): ByteArray {
        val sink = Buffer()
        part?.body?.writeTo(sink)
        return sink.readByteArray()
    }
    // Convertir signalementModel to signalement data class:
    private fun createSignalementTransfert(signalementTransfertModel: SignalementTransfertModel, Upload : Int): SignalementTransfert {
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
            lieudanger = signalementTransfertModel.lieudanger,
            longitudesignaleur = signalementTransfertModel.longitudesignaleur,
            latitudesignaleur = signalementTransfertModel.latitudesignaleur,
            statut = if (Upload ==1) "أرسلت في انتظار الرد" else "في انتظار الإرسال",
            signalementId = signalementTransfertModel.signalementId,
        )
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