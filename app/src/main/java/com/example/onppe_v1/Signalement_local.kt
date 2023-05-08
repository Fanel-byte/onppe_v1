package com.example.onppe_v1

data class Signalement_local(
    var signalementid: Int,
    var descriptif: String,
    var statut: String,
    var motif: String,
    var datesignalement: String,
    var nomenfant: String,
    var prenomenfant: String,
    var adresseenfant: String,
    var age: Int,
    var sexe: String,
    var situationparent: String,
    var wilaya: String,
)