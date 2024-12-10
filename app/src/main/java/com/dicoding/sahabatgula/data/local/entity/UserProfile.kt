package com.dicoding.sahabatgula.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserProfile(
    @PrimaryKey
    var id: String = "",
    val name: String? = "",
    val email: String? = "",
    val password: String? = "",
    val umur: Int? = 0,
    val berat: Int? = 0,
    val tinggi: Int? = 0,
    val gender: String? = "",
    val lingkarPinggang: String? = "",
    val riwayatDiabetes: Int = 0,
    val gulaDarahTinggi: Int = 0,
    val tekananDarahTinggi: Int = 0,
    val tingkatAktivitas: String? = "",
    val konsumsiBuah: Int = 0,
    val kaloriHarian: Int = 0,
    val karbohidratHarian: Int = 0,
    val proteinHarian: Int = 0,
    val lemakHarian: Int = 0,
    val gulaHarian: Int = 0
)

