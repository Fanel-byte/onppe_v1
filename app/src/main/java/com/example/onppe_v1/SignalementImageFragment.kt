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
import android.widget.EditText
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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.findNavController
import com.example.onppe_v1.databinding.FragmentSignalementImageBinding
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class SignalementImageFragment : Fragment() {
    private lateinit var signalementModel: SignalementTransfertModel
    private lateinit var binding: FragmentSignalementImageBinding
    private lateinit var image : ImageView
    private var signalementId: Int? = null
    private lateinit var video: VideoView
    private lateinit var btn_upload_camera : ImageView
    private lateinit var btn_upload_gallery : ImageView
    private lateinit var btn_upload_video : ImageView
    lateinit var image_body: MultipartBody.Part
    lateinit var imageInfo: Image

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
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        signalementModel = ViewModelProvider(requireActivity()).get(SignalementTransfertModel::class.java)
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
                //get image path

                val filesDir = requireContext().getFilesDir()
                val file = File(filesDir, "image" + ".png")
                val bos = ByteArrayOutputStream()
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 0, bos)
                val bitmapdata = bos.toByteArray()
                val fos = FileOutputStream(file)
                fos.write(bitmapdata)
                fos.flush()
                fos.close()
                val reqFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                image_body = MultipartBody.Part.createFormData("path", file.getName(), reqFile)

                binding.img.visibility=View.INVISIBLE

            }
        }
        // code to upload the image from the gallery
        activityResultLauncher1 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val intent = result.data
            if (result.resultCode == AppCompatActivity.RESULT_OK && intent != null) {
                val selectedImageUri = intent.getData()
                imageBitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source = ImageDecoder.createSource(requireActivity().contentResolver, selectedImageUri!!)
                    ImageDecoder.decodeBitmap(source)

                } else {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, selectedImageUri)
                }
                image.setImageBitmap(imageBitmap)
                binding.img.visibility=View.INVISIBLE
                image.visibility = View.VISIBLE
                binding.img.visibility=View.INVISIBLE
                //get image path
                val filesDir = requireContext().getFilesDir()
                val file = File(filesDir, "image" + ".png")
                val bos = ByteArrayOutputStream()
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 0, bos)
                val bitmapdata = bos.toByteArray()
                val fos = FileOutputStream(file)
                fos.write(bitmapdata)
                fos.flush()
                fos.close()
                val reqFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                image_body = MultipartBody.Part.createFormData("path", file.getName(), reqFile)


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

        //cas 1 : envoyer un signalement avec Image et Descriptif
        binding.envoie.setOnClickListener {
            addSignalement(Signalement(null,null,null,null,null,null,null,true,"")) { id ->
                if (id != null) {
                    imageInfo = Image(binding.Descriptionimage.text.toString(), id)
                    val imageInfoMB = MultipartBody.Part.createFormData("image", Gson().toJson(imageInfo))
                    addImg(imageInfoMB, image_body)
                }
            }
        }


        //cas 2 : envoyer un signalement avec plus d'information
        binding.add.setOnClickListener{
           signalementModel.DescriptifvideoImageSon = binding.Descriptionimage.text.toString()
            view.findNavController().navigate(R.id.action_signalementImageFragment_to_signalementForm1Fragment)
        }

        binding.back.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementImageFragment_to_signalementFragment)
        }
        binding.home.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signalementImageFragment_to_fonctionnalitiesActivity)
        }
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

    private fun addImg(image :  MultipartBody.Part ,imageBody: MultipartBody.Part) {
        CoroutineScope(Dispatchers.IO).launch {
            val response =  RetrofitService.endpoint.addImg(image,imageBody)
            withContext(Dispatchers.Main) {
                if(response.isSuccessful) {
                    findNavController().navigate(R.id.action_signalementImageFragment_to_finFormulaireFragment)
                }
                else {
                    Toast.makeText(requireActivity(),"Une erreur s'est produite",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}