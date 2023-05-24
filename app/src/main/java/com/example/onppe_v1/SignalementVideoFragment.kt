package com.example.onppe_v1

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.graphics.drawable.ColorDrawable
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController

import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.onppe_v1.databinding.FragmentSignalementImageBinding
import com.example.onppe_v1.databinding.FragmentSignalementVideoBinding
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.google.gson.Gson
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import com.google.android.exoplayer2.source.ProgressiveMediaSource

import com.google.android.exoplayer2.source.MediaSource

import com.google.android.exoplayer2.util.Util
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory as DefaultDataSourceFactory1


class SignalementVideoFragment : Fragment() {
    private lateinit var signalementModel: SignalementTransfertModel

    private lateinit var binding: FragmentSignalementVideoBinding
    private lateinit var video: PlayerView
    private lateinit var btn_upload_camera : ImageView
    private lateinit var btn_Capture_video : ImageView
    lateinit var videoInfo: Video
    //lateinit var video_body: MultipartBody.Part
    var video_body: MultipartBody.Part? = null
    lateinit var player:SimpleExoPlayer

    private lateinit var activityResultLauncher2: ActivityResultLauncher<Intent>
    val requestCode = 400
    var intent_video: Uri? = null

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


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSignalementVideoBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val dialogBinding = layoutInflater.inflate(R.layout.fragment_popup_window_video,null)
        val myDialog = Dialog(requireActivity())
        myDialog.setContentView(dialogBinding)
        myDialog.setCancelable(true)
        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // Récupérer la taille de l'écran
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        signalementModel = ViewModelProvider(requireActivity()).get(SignalementTransfertModel::class.java)
        video=binding.videoView
        btn_Capture_video = binding.videocapture
        btn_upload_camera = binding.videogalerie

        //init Video
        player=  SimpleExoPlayer.Builder(requireActivity()).build()
        video.setPlayer(player)
        // End VideoView


// Code to upload the video from the gallery
        activityResultLauncher2 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val intent = result.data
            if (result.resultCode == AppCompatActivity.RESULT_OK && intent != null) {
                val selectedVideoUri = intent.data
                if (selectedVideoUri != null && checkVideoDuration(selectedVideoUri)) {
                    // La vidéo sélectionnée respecte la durée maximale de 30 secondes
                    // Continuer le traitement comme avant
                    // player.uri.setVideoURI(selectedVideoUri)
                    val mediaItem = MediaItem.fromUri(selectedVideoUri!!)
                    player.setMediaItem(mediaItem)
                    intent_video = selectedVideoUri
                    video.requestFocus()
                    // Prepare the player
                    player.prepare();
                    // Start playback
                    player.setPlayWhenReady(true);

                    video.visibility = View.VISIBLE
                    //add video in path
                    val path = getPathFromUri(selectedVideoUri!!)
                    val input = context?.contentResolver?.openInputStream(selectedVideoUri!!)
                    val fileName = "video.mp4"
                    val outputStream = requireContext().openFileOutput(fileName, Context.MODE_PRIVATE)
                    input?.copyTo(outputStream)
                    input?.close()
                    outputStream.close()
                    val file = File(requireContext().filesDir, fileName)
                    val reqFile = RequestBody.create("videos/*".toMediaTypeOrNull(), file)
                    video_body = MultipartBody.Part.createFormData("path", file.name, reqFile)

                } else {
                    // La vidéo sélectionnée dépasse la durée maximale de 30 secondes
                    myDialog.show()
                    //   Toast.makeText(requireActivity(), "يجب أن يكون الفيديو المحدد أقل من أو يساوي 30 ثانية", Toast.LENGTH_SHORT).show()
                }
            }

        }
        btn_upload_camera.setOnClickListener {
            // if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED)  {
            VideoChooser()
            // }
            //else {
            //      checkPermission()
            // }
        }
        // Code to upload the video from the camera
        btn_Capture_video.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED)  {
                openVideoCameraIntent()
            }
            else {
                checkPermission()
            }
        }
        //cas 1 : envoyer un signalement avec video et Descriptif
        binding.envoie.setOnClickListener {
            if (video_body==null){
                Toast.makeText(requireActivity(), "veillez faire entrer la video d'abord", Toast.LENGTH_SHORT).show()
            }
            else {
            addSignalement(Signalement(null,null,null,null,null,null,null,true,"")) { id ->
                if (id != null) {
                    signalementModel.DescriptifvideoImageSon = binding.Descriptionvideo.text.toString()
                    signalementModel.id = id
                    videoInfo = Video(binding.Descriptionvideo.text.toString(), id)
                    val videoInfoMB = MultipartBody.Part.createFormData("video", Gson().toJson(videoInfo))
                    signalementModel.videoImageSon = video_body
                    addVideo(videoInfoMB, video_body!!)
                }
            }
        }
        }
        //cas 2 : envoyer un signalement avec plus d'information
        binding.add.setOnClickListener{
            signalementModel.DescriptifvideoImageSon = binding.Descriptionvideo.text.toString()
            signalementModel.videoImageSon = video_body
            signalementModel.typepreuve = "video"
            if (video_body==null){
                Toast.makeText(requireActivity(), "veillez faire entrer la video d'abord", Toast.LENGTH_SHORT).show()
            }else {
                view.findNavController().navigate(R.id.action_signalementVideoFragment_to_signalementForm1Fragment)
            }
        }
        binding.back.setOnClickListener { view: View ->
            view.findNavController().popBackStack()
        }
        binding.home.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementVideoFragment_to_fonctionnalitiesActivity)
        }
    }

    // Request permission
    private fun checkPermission() {
        val perms = arrayOf(Manifest.permission.CAMERA)
        requestPermissions(perms, requestCode)
    }

    override fun onRequestPermissionsResult(permsRequestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(permsRequestCode, permissions, grantResults)

        if (permsRequestCode==requestCode && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openVideoCameraIntent()

        }
    }

    private fun getPathFromUri(uri: Uri): String? {
        var path: String? = null
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        val cursor = context?.contentResolver?.query(uri, projection, null, null, null)
        if (cursor != null && cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            path = cursor.getString(columnIndex)
            cursor.close()
        }
        return path
    }



    // function to start recording a video from the camera
    fun openVideoCameraIntent() {
        val videoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        videoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30)

        activityResultLauncher2.launch(videoIntent)
    }

    private fun addSignalement(new: Signalement, callback: (Int?) -> Unit) {
        binding.progressBar.visibility = View.VISIBLE

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = RetrofitService.endpoint.addSignalement(new)
            withContext(Dispatchers.Main) {
                binding.progressBar.visibility = View.INVISIBLE
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

    private fun addVideo(video :  MultipartBody.Part ,videoBody: MultipartBody.Part) {
        binding.progressBar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response =  RetrofitService.endpoint.addVideo(video,videoBody)
            withContext(Dispatchers.Main) {
                binding.progressBar.visibility = View.INVISIBLE
                if(response.isSuccessful) {
                    Toast.makeText(requireActivity(),"Signalement envoyé",Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_signalementVideoFragment_to_finFormulaireFragment)
                }
                else {
                    Toast.makeText(requireActivity(),"Une erreur s'est produite",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun getVideoDuration(uri: Uri): Long {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(requireContext(), uri)
        val durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        retriever.release()

        return durationStr?.toLongOrNull() ?: 0L
    }
    fun VideoChooser() {
        val intent = Intent()
        intent.setType("video/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        activityResultLauncher2.launch(intent)
    }



    private fun checkVideoDuration(uri: Uri): Boolean {
        val duration = getVideoDuration(uri)
        val maxDuration = 30 * 1000 // 30 secondes en millisecondes

        return duration <= maxDuration
    }
}