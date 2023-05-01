package com.example.onppe_v1

import android.Manifest
import android.app.Activity
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
import com.example.onppe_v1.databinding.FragmentSignalementImageBinding
import com.example.onppe_v1.databinding.FragmentSignalementVideoBinding

class SignalementVideoFragment : Fragment() {

    private lateinit var binding: FragmentSignalementVideoBinding
    private lateinit var video: VideoView
    private lateinit var btn_upload_camera : ImageView
    private lateinit var btn_upload_gallery : ImageView
    private lateinit var btn_Capture_video : ImageView
    private lateinit var activityResultLauncher2: ActivityResultLauncher<Intent>
    val requestCode = 400
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
                video.requestFocus()
                video.start()
                video.visibility = View.VISIBLE
            }
        }

        btn_upload_camera.setOnClickListener {
            VideoChooser()
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
        return view
    }

    // Request permission
    private fun checkPermission() {
        val perms = arrayOf(Manifest.permission.CAMERA)
        ActivityCompat.requestPermissions(requireActivity(),perms, requestCode)
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
}