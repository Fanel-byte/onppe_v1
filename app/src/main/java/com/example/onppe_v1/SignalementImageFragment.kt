package com.example.onppe_v1

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
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
import androidx.navigation.findNavController
import com.example.onppe_v1.databinding.FragmentSignalementImageBinding


class SignalementImageFragment : Fragment() {

    private lateinit var binding: FragmentSignalementImageBinding
    private lateinit var image : ImageView
    private lateinit var video: VideoView
    private lateinit var btn_upload_camera : ImageView
    private lateinit var btn_upload_gallery : ImageView
    private lateinit var btn_upload_video : ImageView
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var activityResultLauncher1: ActivityResultLauncher<Intent>
    private lateinit var activityResultLauncher2: ActivityResultLauncher<Intent>
    lateinit var imageBitmap: Bitmap
    val requestCode = 400
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignalementImageBinding.inflate(inflater, container, false)
        val view = binding.root
        image = binding.ImageView
        btn_upload_camera = binding.imagecapture
        btn_upload_gallery = binding.imagegalerie


        // code to uplaod the image from the camera
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val intent = result.data
            if (result.resultCode == AppCompatActivity.RESULT_OK && intent != null)
            {
                imageBitmap = intent.extras?.get("data") as Bitmap
                image.setImageBitmap(imageBitmap)
                binding.img.visibility=View.INVISIBLE
            }
        }
        // code to upload the image from the gallery
        activityResultLauncher1 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val intent = result.data
            if (result.resultCode == AppCompatActivity.RESULT_OK && intent != null) {
                val selectedImageUri = intent.getData()
                imageBitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    Toast.makeText(context, "teeest", Toast.LENGTH_SHORT).show();

                    val source = ImageDecoder.createSource(requireActivity().contentResolver, selectedImageUri!!)
                    ImageDecoder.decodeBitmap(source)

                } else {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, selectedImageUri)
                }
                image.setImageBitmap(imageBitmap)
                binding.img.visibility=View.INVISIBLE
                image.visibility = View.VISIBLE


            }
        }


        // by clicking this button we get the image from the camera
        btn_upload_camera.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED)  {
                openCameraIntent()
            }
            else {
                checkPermission()
            }
        }
        // by clicking this button we get the image from the gallery
        btn_upload_gallery.setOnClickListener {
            imageChooser()
        }

        binding.envoie.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementImageFragment_to_finFormulaireFragment)
        }
        binding.add.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementImageFragment_to_signalementForm1Fragment)
        }
        binding.back.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementImageFragment_to_signalementFragment)
        }
        binding.home.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementImageFragment_to_fonctionnalitiesActivity)
        }

        return view
    }

    // Take a picture launching camera 1
    fun openCameraIntent() {
        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        activityResultLauncher.launch(pictureIntent)
    }
    // Request permission
    private fun checkPermission() {
        val perms = arrayOf(Manifest.permission.CAMERA)
        ActivityCompat.requestPermissions(requireActivity(),perms, requestCode)
    }

    override fun onRequestPermissionsResult(permsRequestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(permsRequestCode, permissions, grantResults)
        if (permsRequestCode==requestCode && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCameraIntent()
        }
    }

    // Function to choose a pic from the gallery
    fun imageChooser() {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        activityResultLauncher1.launch(intent)
    }

}