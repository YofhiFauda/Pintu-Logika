package com.digitallogic.halaman_kuis

data class LevelConfig(
    val level: Int,
    val inputs: Int,
    val gates: List<GateConfig>,
    val expectedOutput: Boolean,
    val layoutResId: Int,
    val connections: List<ConnectionConfig>  // Tambahkan ini
)

data class GateConfig(
    val id: String,
    val type: String,
    val inputIds: List<String>,
    val isFinal: Boolean = false
)
