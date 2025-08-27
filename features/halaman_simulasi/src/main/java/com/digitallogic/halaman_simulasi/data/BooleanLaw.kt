package com.digitallogic.halaman_simulasi.data

data class BooleanLaw(
    val id: Int,
    val title: String,
    val description: String,
    val formula1: String,
    val formula2: String? = null // default null
)