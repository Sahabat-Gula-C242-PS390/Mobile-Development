package com.dicoding.sahabatgula.data.remote.response

data class PredictionResponse(
    val status: String,
    val data: List<PredictionItem>
)

data class PredictionItem(
    val label: String,
    val prediction: String,
    val gula: Int,
    val karbohidrat: Int,
    val lemak: Double,
    val protein: Int,
    val name: String
)
