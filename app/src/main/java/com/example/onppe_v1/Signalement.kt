package com.example.onppe_v1

data class Signalement(
    val id: Int?,
    val citoyenid:Int?,
    val motifid:Int?,
    val enfantid:Int?,
    val descriptif:String?,
    val preuveid:Int?,
    val typesignaleurid:Int?,
    val identitesecrete:Boolean?,
    val dateincident :String?
)
