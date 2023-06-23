package com.example.onppe_v1

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.onppe_v1.databinding.FragmentSignalementImageBinding
import com.google.gson.Gson
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class SignalementImageFragment : Fragment() {
    private lateinit var signalementModel: SignalementTransfertModel
    private lateinit var binding: FragmentSignalementImageBinding
    var image_body: MultipartBody.Part? = null
    var imageInfo: Image? = null
    private var targetFileSize = 500 * 1024 // Taille souhaitée en octets (500 ko)
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var activityResultLauncher1: ActivityResultLauncher<Intent>
    var imageBitmap: Bitmap? = null
    val requestCode = 400


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignalementImageBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signalementModel = ViewModelProvider(requireActivity()).get(SignalementTransfertModel::class.java)

        RemplirChamps(signalementModel)

        // code to uplaod the image from the camera
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val intent = result.data
            if (result.resultCode == AppCompatActivity.RESULT_OK && intent != null)
            {// Lors de la capture de l'image depuis la caméra
                imageBitmap = intent.extras?.get("data") as Bitmap


                binding.ImageView.setImageBitmap(imageBitmap)
                //get image path

                val filesDir = requireContext().getFilesDir()
                val file = File(filesDir, "image" + ".png")
                val bos = ByteArrayOutputStream()

                var quality = 100
                //compresser la photo a 500 KO
                imageBitmap!!.compress(Bitmap.CompressFormat.PNG, quality, bos)

                while (bos.toByteArray().size > targetFileSize) {
                    bos.reset()
                    quality -= 10
                    imageBitmap!!.compress(Bitmap.CompressFormat.JPEG, quality, bos)
                }

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



                binding.ImageView.setImageBitmap(imageBitmap)
                binding.img.visibility=View.INVISIBLE
                binding.ImageView.visibility = View.VISIBLE
                binding.img.visibility=View.INVISIBLE
                //get image path
                val filesDir = requireContext().getFilesDir()
                val file = File(filesDir, "image" + ".png")
                val bos = ByteArrayOutputStream()
                var quality = 100
                //compresser la photo a 500 KO
                imageBitmap?.compress(Bitmap.CompressFormat.JPEG, quality, bos)
                while (bos.toByteArray().size > targetFileSize) {
                    bos.reset()
                    quality -= 10
                    imageBitmap?.compress(Bitmap.CompressFormat.JPEG, quality, bos)
                }
                val bitmapdata = bos.toByteArray()
                val fos = FileOutputStream(file)
                fos.write(bitmapdata)
                fos.flush()
                fos.close()
                val reqFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                image_body = MultipartBody.Part.createFormData("path", file.getName(), reqFile)


            }
        }
        val dialogBinding2 = layoutInflater.inflate(R.layout.fragment_help_image,null)
        val myDialog2 = Dialog(requireActivity())
        myDialog2.setContentView(dialogBinding2)
        myDialog2.setCancelable(true)
        myDialog2.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // Récupérer la taille de l'écran
        val displayMetrics2 = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics2)

        binding.question.setOnClickListener {
            myDialog2.show()        }
        // by clicking this button we get the image from the camera
        binding.imagecapture.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED)  {
                openCameraIntent()
            }
            else {
                checkPermission()
            }
        }
        // by clicking this button we get the image from the gallery
        binding.imagegalerie.setOnClickListener {
            imageChooser()
        }


        // envoyer un signalement avec plus d'information
        binding.add.setOnClickListener{
            signalementModel.videoImageSon =  image_body
            signalementModel.DescriptifvideoImageSon = binding.Descriptionimage.text.toString()
            signalementModel.typepreuve = "image"
            if (signalementModel.videoImageSon == null){
                Toast.makeText(requireActivity(), "الرجاء إدخال صورة أولا", Toast.LENGTH_SHORT).show()
            }
            else {
                view.findNavController().navigate(R.id.action_signalementImageFragment_to_signalementFormSignaleurFragment)
            }
        }


    }

    // Take a picture launching camera 1
    fun openCameraIntent() {

        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        pictureIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, "1080x1920")
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

    private fun RemplirChamps(signalementModel : SignalementTransfertModel){
        if (signalementModel.DescriptifvideoImageSon != null){
            binding.Descriptionimage.setText(signalementModel.DescriptifvideoImageSon)
        }
        //if (signalementModel.videoImageSon != null){ }
    }




}