package com.digitallogic.halaman_simulasi.data


// Data class yang diperbaiki
data class TruthTableRow(
    val inputA: String,
    val inputB: String = "",
    val inputC: String = "",
    val inputAComplement: String = "",
    val inputBComplement: String = "",
    val inputCComplement: String = "",
    val outputLeft: String,
    val outputRight: String = "",
    val isEqual: Boolean = true,
)
