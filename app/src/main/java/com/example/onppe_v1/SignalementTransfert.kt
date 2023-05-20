package com.example.onppe_v1

import androidx.room.Entity
import androidx.room.PrimaryKey
import okhttp3.MultipartBody
import java.io.Serializable

@Entity(tableName ="signalements" )
data class SignalementTransfert(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val upload:Boolean,
    val videoImageSon: ByteArray,
    val DescriptifvideoImageSon:String?,
    val typepreuve:String?,

    var motifid:Int?,
    var dateincident :String?,
    var nomEnfant:String?,
    var prenomEnfant:String?,
    var ageEnfant:Int?,
    var sexeEnfant: String?,
    var situationparentEnfant:String?,
    var adresseEnfant:String?,
    var wilayacodeEnfant:Int?,
    var typesignaleurid:Int?,
    var identitesecrete:Boolean?,
    var nomCitoyen:String?,
    var prenomCitoyen :String?,
    var sexeCitoyen:String?,
    var ageCitoyen :Int?,
    var adresseCitoyen:String?,
    var telCitoyen:String?,
    var descriptif:String?,
): Serializable
