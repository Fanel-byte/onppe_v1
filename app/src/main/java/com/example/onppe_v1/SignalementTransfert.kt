package com.example.onppe_v1

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName ="signalements" )
data class SignalementTransfert(
    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    var upload: Int,
    val videoImageSon: ByteArray,
    val DescriptifvideoImageSon:String?,
    val typepreuve:String?,
    var motifid:Int?,
    var dateincident:String?,
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
    var prenomCitoyen:String?,
    var sexeCitoyen:String?,
    var ageCitoyen:Int?,
    var adresseCitoyen:String?,
    var telCitoyen:String?,
    var descriptif:String?,
    var statut: String = "في انتظار الإرسال",
    var lieudanger:String?,
    var longitudesignaleur: Double ?,
    var latitudesignaleur: Double ?,
    var signalementId: Int?,
    ): Serializable
