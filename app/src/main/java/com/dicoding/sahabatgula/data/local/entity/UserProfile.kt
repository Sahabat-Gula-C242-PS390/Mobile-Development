package com.dicoding.sahabatgula.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserProfile(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val gender: String = "",
    val umur: Int = 0,
    val berat: Int = 0,
    val tinggi: Int = 0,
    val lingkarPinggang: String = "",
    val tekananDarahTinggi: Boolean = false,
    val gulaDarahTinggi: Boolean = false,
    val tingkatAktivitas: String = "",
    val konsumsiBuah: Boolean = false,

    // optional di API
    val kaloriHarian: Int = 0,
    val karbohidratHarian: Int = 0,
    val proteinHarian: Int = 0,
    val lemakHarian: Int = 0,
    val gulaHarian: Int = 0
)
