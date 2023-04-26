package com.example.onppe_v1

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.onppe_v1.databinding.FragmentSignalementVideoBinding

class SignalementVideoFragment : Fragment() {

    lateinit var bindingVideoFragment: FragmentSignalementVideoBinding
    private val REQUEST_VIDEO_CAPTURE = 1
    private val REQUEST_VIDEO_GALLERY = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        bindingVideoFragment = FragmentSignalementVideoBinding.inflate( inflater , container , false )
        return bindingVideoFragment.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // prendre une video
        bindingVideoFragment.videocapture.setOnClickListener(){
            val takeVideoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            if (takeVideoIntent.resolveActivity(requireActivity().packageManager) != null) {
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE)
            }
        }
        // recuperer de la gallerie
        bindingVideoFragment.videogalerie.setOnClickListener(){
            // Create an Intent to open the gallery
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_VIDEO_GALLERY)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == Activity.RESULT_OK && data != null) {
            // The user has successfully recorded a video
            val videoUri: Uri? = data.data
            if (videoUri != null) {
                // Affichage de la vidéo capturée dans le composant VideoView
                bindingVideoFragment.videoView.setVideoURI(videoUri)
                bindingVideoFragment.videoView.start()
            }
        }
        if (requestCode == REQUEST_VIDEO_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
            val videoUri: Uri? = data.data
            if (videoUri != null) {
                // Affichage de la vidéo capturée dans le composant VideoView
                bindingVideoFragment.videoView.setVideoURI(videoUri)
                bindingVideoFragment.videoView.start()
            }
        }

    }


}