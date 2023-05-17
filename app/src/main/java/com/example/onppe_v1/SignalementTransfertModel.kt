package com.example.onppe_v1

import androidx.lifecycle.ViewModel
import okhttp3.MultipartBody


class SignalementTransfertModel:ViewModel() {
    val videoImageSon: MultipartBody.Part? = null
    var DescriptifvideoImageSon:String?= null
    val typepreuve:String?= null
    val id: Int?= null
    var motifid:Int?= null
    var dateincident :String?= null
    var nomEnfant:String?= null
    var prenomEnfant:String?= null
    var ageEnfant:Int?= null
    var sexeEnfant: String?= null
    var situationparentEnfant:String?= null
    var adresseEnfant:String?= null
    var wilayacodeEnfant:Int?= null
    var typesignaleurid:Int?= null
    var identitesecrete:Boolean?= null
    var nomCitoyen:String?= null
    var prenomCitoyen :String?= null
    var sexeCitoyen:String?= null
    var ageCitoyen :Int?= null
    var adresseCitoyen:String?= null
    var telCitoyen:String?= null
    var descriptif:String?= null
}