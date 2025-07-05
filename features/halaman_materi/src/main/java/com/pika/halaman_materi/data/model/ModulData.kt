package com.pika.halaman_materi.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SubModul(
    val nama: String = "",
    val judul: String = "",
    val konten: String = "",
    val tanggalUpload: String = "",
    val isSelesai: Boolean = false
) : Parcelable

data class Modul(
    val nama: String,
    val isSelesai: Boolean,
    val subModul: List<SubModul>,
    var isExpanded: Boolean = false // default collapsed
)

