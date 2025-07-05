package com.pika.halaman_materi.data

data class SubModul(
    val nama: String,
    val isSelesai: Boolean
)

data class Modul(
    val nama: String,
    val isSelesai: Boolean,
    val subModul: List<SubModul>,
    var isExpanded: Boolean = false // default collapsed
)
