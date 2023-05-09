package com.example.onppe_v1

data class Signalement_local(
    var id: Int,
    var descriptif: String,
    var statut: String,
    var designationar: String,
    var nom: String,
    var prenom: String,
    var adresse: String,
    var age: Int,
    var sexe: String,
    var situationparent: String,
    var namear: String,
)