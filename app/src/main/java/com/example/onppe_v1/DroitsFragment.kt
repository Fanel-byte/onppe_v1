package com.example.onppe_v1

import android.app.Dialog
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import com.example.onppe_v1.databinding.ActivityFonctionnalitiesBinding
import com.example.onppe_v1.databinding.FragmentDroitsBinding
import com.example.onppe_v1.databinding.FragmentSignalementFormInfosBinding


class DroitsFragment : Fragment() {
    lateinit var binding: FragmentDroitsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDroitsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val linkText = "<a href=\"http://onppe.dz/index.php/ar/2017-01-08-17-41-39/2017-07-02-08-16-21/26-15-12-28-1436-15-2015#slider\">Cliquez ici</a>"
        binding.textViewLink.text = Html.fromHtml(linkText)
        binding.textViewLink.movementMethod = LinkMovementMethod.getInstance()


    }
}