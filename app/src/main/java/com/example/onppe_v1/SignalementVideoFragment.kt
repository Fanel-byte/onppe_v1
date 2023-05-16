package com.example.onppe_v1

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import androidx.core.os.bundleOf
import androidx.navigation.Navigation.findNavController

import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.onppe_v1.databinding.FragmentSignalementImageBinding
import com.example.onppe_v1.databinding.FragmentSignalementVideoBinding
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class SignalementVideoFragment : Fragment() {

    private lateinit var binding: FragmentSignalementVideoBinding
    private lateinit var video: VideoView
    private lateinit var btn_upload_camera : ImageView
    private lateinit var btn_Capture_video : ImageView
    lateinit var videoInfo: Video
    lateinit var video_body: MultipartBody.Part

    private lateinit var activityResultLauncher2: ActivityResultLauncher<Intent>
    val requestCode = 400
    var intent_video: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignalementVideoBinding.inflate(inflater, container, false)
        val view = binding.root
        video=binding.videoView
        btn_Capture_video = binding.videocapture
        btn_upload_camera = binding.videogalerie


// Code to upload the video from the gallery
        activityResultLauncher2 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val intent = result.data
            if (result.resultCode == AppCompatActivity.RESULT_OK && intent != null) {
                val selectedVideoUri = intent.data
                video.setVideoURI(selectedVideoUri)
                intent_video = selectedVideoUri
                video.requestFocus()
                video.start()
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
            }
        }





        btn_upload_camera.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED)  {
                VideoChooser()
            }
            else {
                checkPermission()
            }
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


        //cas 1 : envoyer un signalement avec Image et Descriptif
        binding.envoie.setOnClickListener {
            addSignalement(Signalement(null,null,null,null,null,null,null,true,"")) { id ->
                if (id != null) {
                    videoInfo = Video(binding.Descriptionvideo.text.toString(), id)
                    val imageInfoMB = MultipartBody.Part.createFormData("video", Gson().toJson(videoInfo))
                    addVideo(imageInfoMB, video_body)

                }
            }
        }
        //cas 2 : envoyer un signalement avec plus d'information
        binding.add.setOnClickListener{

            val signalement = SignalementTransfert(video_body ,
                binding.Descriptionvideo.text.toString(),
                "video",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null ,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null)
            val data = bundleOf("data" to signalement)
            view.findNavController().navigate(R.id.action_signalementVideoFragment_to_signalementForm1Fragment,data)
        }



        binding.back.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementVideoFragment_to_signalementFragment)
        }
        binding.home.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementVideoFragment_to_fonctionnalitiesActivity)
        }

        return view
    }

    // Request permission
    private fun checkPermission() {
        val perms = arrayOf(Manifest.permission.CAMERA)
        ActivityCompat.requestPermissions(requireActivity(),perms, requestCode)
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
        activityResultLauncher2.launch(videoIntent)
    }
    fun VideoChooser() {
        val intent = Intent()
        intent.setType("video/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        activityResultLauncher2.launch(intent)
    }

    private fun addSignalement(new: Signalement, callback: (Int?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitService.endpoint.addSignalement(new)
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

    private fun addVideo(video :  MultipartBody.Part ,videoBody: MultipartBody.Part) {
        CoroutineScope(Dispatchers.IO).launch {
            val response =  RetrofitService.endpoint.addVideo(video,videoBody)
            withContext(Dispatchers.Main) {
                if(response.isSuccessful) {
                    findNavController().navigate(R.id.action_signalementVideoFragment_to_finFormulaireFragment)
                }
                else {
                    Toast.makeText(requireActivity(),"Une erreur s'est produite",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}