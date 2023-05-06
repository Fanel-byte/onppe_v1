package com.example.onppe_v1

import okhttp3.MultipartBody
import java.io.Serializable

data class SignalementTransfert(
    val videoImageSon: MultipartBody.Part?,
    val DescriptifvideoImageSon:String?,
    val id: Int?,
    val motifid:Int?,
    val dateincident :String?,
    val nomEnfant:String?,
    val prenomEnfant:String?,
    val ageEnfant:Int?,
    val sexeEnfant: String?,
    val situationparentEnfant:String?,
    val adresseEnfant:String?,
    val wilayacodeEnfant:Int?,
    val typesignaleurid:Int?,
    val identitesecrete:Boolean?,
    val nomCitoyen:String?,
    val prenomCitoyen :String?,
    val sexeCitoyen:String?,
    val ageCitoyen :Int?,
    val adresseCitoyen:String?,
    val telCitoyen:String?,
    val descriptif:String?,
): Serializable
