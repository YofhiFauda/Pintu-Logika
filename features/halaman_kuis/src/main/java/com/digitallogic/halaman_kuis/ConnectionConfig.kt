package com.digitallogic.halaman_kuis

data class ConnectionConfig(
    val fromId: String,         // ID sumber (INPUT1, AND1, dll)
    val fromPortId: String,     // Titik output sumber (output_0, output_1, dll)
    val toId: String,           // ID tujuan (AND1, OUTPUT, dll)
    val toPortId: String        // Titik input tujuan (input_0, input_1, dll)
)
