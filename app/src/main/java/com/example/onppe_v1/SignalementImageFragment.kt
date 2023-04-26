package com.example.onppe_v1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.onppe_v1.databinding.FragmentSignalementImageBinding


class SignalementImageFragment : Fragment() {

    lateinit var bindingImageFragment: FragmentSignalementImageBinding
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_GALLERY = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        bindingImageFragment = FragmentSignalementImageBinding.inflate( inflater , container , false )
        return bindingImageFragment.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // prendre une video
        bindingImageFragment.imagecapture.setOnClickListener(){

        }
        // recuperer de la gallerie
        bindingImageFragment.imagegalerie.setOnClickListener(){

        }
    }
}