package com.dicoding.sahabatgula.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserProfileResponse(

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("data")
	val data: ListUserProfileItem? = null
)

data class ListUserProfileItem(

	@field:SerializedName("kaloriHarian")
	val kaloriHarian: Int? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("tekananDarahTinggi")
	val tekananDarahTinggi: Boolean? = null,

	@field:SerializedName("riwayatDiabetes")
	val riwayatDiabetes: Boolean? = null,

	@field:SerializedName("umur")
	val umur: Int? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("karbohidratHarian")
	val karbohidratHarian: Int? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("proteinHarian")
	val proteinHarian: Int? = null,

	@field:SerializedName("lemakHarian")
	val lemakHarian: Int? = null,

	@field:SerializedName("berat")
	val berat: Int? = null,

	@field:SerializedName("gulaHarian")
	val gulaHarian: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("konsumsiBuah")
	val konsumsiBuah: Boolean? = null,

	@field:SerializedName("lingkarPinggang")
	val lingkarPinggang: String? = null,

	@field:SerializedName("isPremium")
	val isPremium: Boolean? = null,

	@field:SerializedName("tingkatAktivitas")
	val tingkatAktivitas: String? = null,

	@field:SerializedName("tinggi")
	val tinggi: Int? = null,

	@field:SerializedName("gulaDarahTinggi")
	val gulaDarahTinggi: Boolean? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
