package com.example.onppe_v1

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.onppe_v1.databinding.FragmentSignalementSonBinding
import com.google.gson.Gson
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.lang.Runnable


class SignalementSonFragment : Fragment() {
    private lateinit var signalementModel: SignalementTransfertModel
    private var output: String? = null
    private var mediaRecorder: MediaRecorder? = null
    private var state: Boolean = false
    private lateinit var binding : FragmentSignalementSonBinding
    private lateinit var counter : TextView
    private lateinit var progressBar: SeekBar
    lateinit var sonInfo: Son
    var son_body: MultipartBody.Part? = null
    private var timeElapsed = 0L
    private var handler: Handler? = null
    private var timerRunnable: Runnable? = null
    private val MAX_RECORDING_DURATION = 30 * 1000 // 30 secondes en millisecondes


    // Exception Handler for Coroutines
    val exceptionHandler = CoroutineExceptionHandler {    coroutineContext, throwable ->
        CoroutineScope(Dispatchers.Main).launch {
            if (isAdded) {
            binding.progressBar.visibility = View.INVISIBLE
            // Ne pas affichre le toast peut poser probleme : not attached to an activity (mettre pop up)
            Toast.makeText(requireActivity(),"Une erreur lors de la connexion au serveur",Toast.LENGTH_SHORT).show()
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
        binding.voicerecorder.setOnClickListener {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.FOREGROUND_SERVICE), 0)
            if (ContextCompat.checkSelfPermission(requireActivity(),
                    Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(requireActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                mediaRecorder = MediaRecorder()
                output = requireContext().getExternalFilesDir(null)?.absolutePath + "/recording.mp3"
                mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
                mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                mediaRecorder?.setOutputFile(output)
                startRecording()
                val audioFile = File(output)
                val audioRequestBody = RequestBody.create("vocaux/*".toMediaTypeOrNull(), audioFile)
                son_body = MultipartBody.Part.createFormData("path", audioFile.name, audioRequestBody)


            }
        }
        binding.stoprecord.setOnClickListener {
            stopRecording()
        }
        signalementModel = ViewModelProvider(requireActivity()).get(SignalementTransfertModel::class.java)
        binding.playrecord.setOnClickListener {
            val mediaPlayer = MediaPlayer()
            try {
                mediaPlayer.setDataSource(output)
                mediaPlayer.prepare()
                mediaPlayer.start()

                updateProgressBar(mediaPlayer)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        counter = binding.counter
        progressBar = binding.progressBar
        progressBar.max = 100
        progressBar.progress = 0

        handler = Handler()


        //cas 1 : envoyer un signalement avec Son et Descriptif
        binding.envoie.setOnClickListener {
            if ( son_body  == null){
                Toast.makeText(requireActivity(), "veillez faire entrer le son d'abord", Toast.LENGTH_SHORT).show()
            }else
            {
                addSignalement(Signalement(null,null,null,null,null,null,null,true,"")) { id ->
                    Toast.makeText(requireActivity(), "id value test $id", Toast.LENGTH_SHORT).show()
                    if (id != null) {
                        sonInfo = Son(binding.Descriptionson.text.toString(), id)
                        val sonInfoMB = MultipartBody.Part.createFormData("vocal", Gson().toJson(sonInfo))
                        signalementModel.id = id
                        signalementModel.videoImageSon = son_body
                        signalementModel.DescriptifvideoImageSon = binding.Descriptionson.text.toString()
                        addSon(sonInfoMB, son_body!!)
                    }
                }
            }
        }
        //cas 2 : envoyer un signalement avec plus d'information
        binding.add.setOnClickListener{
            signalementModel.DescriptifvideoImageSon = binding.Descriptionson.text.toString()
            signalementModel.videoImageSon = son_body
            signalementModel.typepreuve = "son"
            if (son_body == null){
                Toast.makeText(requireActivity(), "veillez faire entrer le son d'abord", Toast.LENGTH_SHORT).show()
            }
            else {
                view.findNavController().navigate(R.id.action_signalementSonFragment_to_signalementFormSignaleurFragment)
            }
        }



        binding.back.setOnClickListener { view: View ->
            view.findNavController().popBackStack()         }
        binding.home.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementSonFragment_to_fonctionnalitiesActivity)
        }

    }

    // function to start recording
    private fun startRecording() {
        try {
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            state = true
            binding.voicerecorder.setImageResource(R.drawable.ic_pause)
            binding.stoprecord.setImageResource(R.drawable.ic_stop)

            timeElapsed = 0L
            timerRunnable = Runnable {
                timeElapsed += 1000
                val seconds = (timeElapsed / 1000) % 60
                val minutes = (timeElapsed / 1000) / 60
                counter.text = String.format("%02d:%02d", minutes, seconds)
                if (timeElapsed >= MAX_RECORDING_DURATION) {
                    stopRecording()
                } else {
                    handler?.postDelayed(timerRunnable!!, 1000)
                }
            }
            handler?.postDelayed(timerRunnable!!, 1000)
            Toast.makeText(context, "Recording started!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // function to stop recording
    private fun stopRecording() {
        if (state) {
            mediaRecorder?.stop()
            mediaRecorder?.release()
            state = false
            binding.playrecord.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            binding.voicerecorder.setImageResource(R.drawable.upload_sound)
            binding.stoprecord.setImageResource(R.drawable.upload)

            handler?.removeCallbacks(timerRunnable!!)
            counter.text = ""
            Toast.makeText(context, "Recording stopped!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "You are not recording right now!", Toast.LENGTH_SHORT).show()
        }
    }
    private fun updateProgressBar(mediaPlayer: MediaPlayer) {
        progressBar.max = mediaPlayer.duration
        progressBar.progress = mediaPlayer.currentPosition

        // Cancel previous callbacks before setting a new one
        handler?.removeCallbacksAndMessages(null)

        // Update progress bar every 100 milliseconds
        handler?.postDelayed({
            updateProgressBar(mediaPlayer)
        }, 100)
    }
    private fun addSignalement(new: Signalement, callback: (Int?) -> Unit) {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = RetrofitService.endpoint.addSignalement(new)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val id = response.body()
                    Toast.makeText(requireActivity(), "Signalement ajouté in bdd $id", Toast.LENGTH_SHORT).show()
                    callback(id)
                } else {
                    Toast.makeText(requireActivity(), "erreur " + response.code().toString(), Toast.LENGTH_SHORT).show()
                    callback(null)
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

}