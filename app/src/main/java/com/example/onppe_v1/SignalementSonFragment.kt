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
import androidx.navigation.findNavController
import com.example.onppe_v1.databinding.FragmentSignalementSonBinding


class SignalementSonFragment : Fragment() {

    private var output: String? = null
    private var mediaRecorder: MediaRecorder? = null
    private var state: Boolean = false
    private lateinit var binding : FragmentSignalementSonBinding
    private lateinit var voice_recorder : ImageView
    private lateinit var check_record :ImageView
    private lateinit var stop_record :ImageView
    private lateinit var counter : TextView
    private lateinit var progressBar: SeekBar

    private var timeElapsed = 0L
    private var handler: Handler? = null
    private var timerRunnable: Runnable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {



        binding = FragmentSignalementSonBinding.inflate(inflater, container,false)
        val view = binding.root
        check_record=binding.playrecord
        voice_recorder = binding.voicerecorder
        stop_record=binding.stoprecord
        voice_recorder.setOnClickListener {
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


            }
        }
        stop_record.setOnClickListener {
            stopRecording()
        }

        check_record.setOnClickListener {
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
        binding.envoie.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementSonFragment_to_finFormulaireFragment)
        }
        binding.add.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementSonFragment_to_signalementForm1Fragment)
        }
        binding.back.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementSonFragment_to_signalementFragment)
        }
        binding.home.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementSonFragment_to_fonctionnalitiesActivity)
        }
        return view
    }

    // function to start recording
    private fun startRecording() {
        try {
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            state = true
            voice_recorder.setImageResource(R.drawable.ic_pause)
            stop_record.setImageResource(R.drawable.ic_stop)

            timeElapsed = 0L
            timerRunnable = Runnable {
                timeElapsed += 1000
                val seconds = (timeElapsed / 1000) % 60
                val minutes = (timeElapsed / 1000) / 60
                counter.text = String.format("%02d:%02d", minutes, seconds)
                handler?.postDelayed(timerRunnable!!, 1000)
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
            check_record.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            voice_recorder.setImageResource(R.drawable.upload_sound)
            stop_record.setImageResource(R.drawable.upload)

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

}
