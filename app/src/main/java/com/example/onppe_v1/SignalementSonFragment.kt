package com.example.onppe_v1

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.onppe_v1.databinding.FragmentSignalementSonBinding
import com.github.squti.androidwaverecorder.RecorderState
import com.github.squti.androidwaverecorder.WaveRecorder
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class SignalementSonFragment : Fragment() {
    private lateinit var signalementModel: SignalementTransfertModel

    private lateinit var binding : FragmentSignalementSonBinding
    lateinit var sonInfo: Son
    var son_body: MultipartBody.Part? = null
    var player:SimpleExoPlayer? =null
    private val MAX_RECORDING_DURATION = 30  // 30 secondes
    // Add Hakim
    private val PERMISSIONS_REQUEST_RECORD_AUDIO = 77
    private lateinit var waveRecorder: WaveRecorder
    private lateinit var filePath: String
    private var isRecording = false
    private var Record = false
    private var isPaused = false

    // Exception Handler for Coroutines
    val exceptionHandler = CoroutineExceptionHandler {    coroutineContext, throwable ->
        CoroutineScope(Dispatchers.Main).launch {
            if (isAdded) {
                //binding.progressBar.visibility = View.INVISIBLE
                // Ne pas affiche le toast peut poser probleme : not attached to an activity (mettre pop up)
                Toast.makeText(requireActivity(),throwable.message.toString(),Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignalementSonBinding.inflate(inflater, container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Init recorder
        signalementModel = ViewModelProvider(requireActivity()).get(SignalementTransfertModel::class.java)
        filePath = requireActivity().externalCacheDir?.absolutePath + "/recording.mp3"
        waveRecorder = WaveRecorder(filePath)

        waveRecorder.onStateChangeListener = {
            when (it) {
                RecorderState.RECORDING -> startRecording()
                RecorderState.STOP -> stopRecording()
                RecorderState.PAUSE ->  pauseRecording()
            }
        }

        waveRecorder.onTimeElapsed = {
            val value = formatTimeUnit(it * 1000)
            if(value=="00:$MAX_RECORDING_DURATION") {
                waveRecorder.stopRecording()
            }
            binding.counter.text = value
        }

        // end Init


        binding.voicerecorder.setOnClickListener {
            if (!isRecording && !isPaused) {
                if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), PERMISSIONS_REQUEST_RECORD_AUDIO)
                }
                else {
                    waveRecorder.startRecording()
                }
            } else {
                if(!isPaused) {
                    waveRecorder.pauseRecording()
                }
                else {
                    waveRecorder.resumeRecording()
                }
            }
        }

        binding.stoprecord.setOnClickListener {
            if (isRecording==false && Record==false) {
                Toast.makeText(requireActivity(), "Veuillez d'abord effectuer un enregistrement", Toast.LENGTH_SHORT).show()

            }
            if  (Record==true) {
                Toast.makeText(requireActivity(), "votre enregistrement a été effectuer ", Toast.LENGTH_SHORT).show()

            }

                waveRecorder.stopRecording()

        }





        //cas 1 : envoyer un signalement avec Son et Descriptif
        binding.envoie.setOnClickListener {
            if ( son_body  == null){
                Toast.makeText(requireActivity(), "veuillez faire entrer le son d'abord", Toast.LENGTH_SHORT).show()
            }else
            {
                addSignalement(Signalement(null,null,null,null,null,null,null,true,""))
            }
        }
        //cas 2 : envoyer un signalement avec plus d'information
        binding.add.setOnClickListener{
            signalementModel.DescriptifvideoImageSon = binding.Descriptionson.text.toString()
            signalementModel.videoImageSon = son_body
            signalementModel.typepreuve = "son"
            if (son_body == null){
                Toast.makeText(requireActivity(), "veuillez faire entrer le son d'abord", Toast.LENGTH_SHORT).show()
            }
            else {
                view.findNavController().navigate(R.id.action_signalementSonFragment_to_signalementFormSignaleurFragment)
            }
        }





    }



    // recondings methods

    private fun startRecording() {
        isRecording = true
        isPaused = false
        binding.counter.visibility = View.VISIBLE
        binding.voicerecorder.setImageResource(R.drawable.ic_pause)
        binding.stoprecord.setImageResource(R.drawable.ic_stop)
        player?.stop()
        binding.audio.setPlayer(null)
    }

    private fun stopRecording() {

        Record=true
        isRecording = false
        isPaused = false
        binding.counter.visibility = View.INVISIBLE
        binding.counter.text = "00:00"
        binding.voicerecorder.setImageResource(R.drawable.upload_sound)
        binding.stoprecord.setImageResource(R.drawable.ic_stop)
        player?.stop()
        binding.audio.setPlayer(null)

        player = SimpleExoPlayer.Builder(requireActivity()).build()
        binding.audio.setPlayer(player)
        val mediaItem = MediaItem.fromUri(filePath)
        player?.setMediaItem(mediaItem)
        binding.audio.requestFocus()
        player?.prepare()
        player?.play()
        // create body
        val audioFile = File(filePath)
        val audioRequestBody = RequestBody.create("vocaux/*".toMediaTypeOrNull(), audioFile)
        son_body = MultipartBody.Part.createFormData("path", audioFile.name, audioRequestBody)
    }



    private fun pauseRecording() {
        binding.voicerecorder.setImageResource(R.drawable.upload_sound)
        isPaused = true
    }

    // end recordings method


    private fun addSignalement(new: Signalement) {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = RetrofitService.endpoint.addSignalement(new)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val id = response.body()
                    Toast.makeText(requireActivity(), "Signalement ajouté in bdd $id", Toast.LENGTH_SHORT).show()
                    if (id != null) {
                        sonInfo = Son(binding.Descriptionson.text.toString(), id)
                        val sonInfoMB = MultipartBody.Part.createFormData("vocal", Gson().toJson(sonInfo))
                        signalementModel.id = id
                        signalementModel.videoImageSon = son_body
                        signalementModel.DescriptifvideoImageSon = binding.Descriptionson.text.toString()
                        addSon(sonInfoMB, son_body!!)
                    }
                } else {
                    Toast.makeText(requireActivity(), "erreur " + response.code().toString(), Toast.LENGTH_SHORT).show()
                    //callback(null)
                }
            }
        }
    }

    private fun addSon(son :  MultipartBody.Part ,sonBody: MultipartBody.Part) {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response =  RetrofitService.endpoint.addSon(son,sonBody)
            withContext(Dispatchers.Main) {
                if(response.isSuccessful) {
                    view?.findNavController()?.navigate(R.id.action_signalementSonFragment_to_finFormulaireFragment)
                }
                else {
                    Toast.makeText(requireActivity(),"Une erreur s'est produite",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun RemplirChamps(signalementModel : SignalementTransfertModel){
        if (signalementModel.DescriptifvideoImageSon != null){
            binding.Descriptionson.setText(signalementModel.DescriptifvideoImageSon)
        }
        //if (signalementModel.videoImageSon != null){ }
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST_RECORD_AUDIO -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    waveRecorder.startRecording()
                }

            }

        }
    }

}
