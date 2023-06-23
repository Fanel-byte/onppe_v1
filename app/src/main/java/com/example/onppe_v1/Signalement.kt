package com.example.onppe_v1

data class Signalement(
    val id: Int?,
    var citoyenid:String?,
    val motifid:Int?,
    var enfantid:Int?,
    val descriptif:String?,
    val preuveid:Int?,
    val typesignaleurid: Int?,
    val identitesecrete:Boolean?,
    val dateincident:String?,
    val lieudanger:String?,
    var longitudesignaleur:Double?,
    var latitudesignaleur:Double?,
    val source :String = "تطبيق الهاتف"
)
