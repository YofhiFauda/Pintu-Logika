package com.digitallogic.core_data.model.materi

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// Updated SubModul with dynamic content support
data class SubModulDynamic(
    val nama: String = "",
    val judul: String = "",
    val tanggalUpload: String = "",
    val dynamicContent: List<ContentItem> = listOf(),
    val isSelesai: Boolean = false
)

data class Modul(
    val nama: String,
    val isSelesai: Boolean,
    val subModul: List<SubModulDynamic>,
    var isExpanded: Boolean = false // default collapsed
)

enum class ContentType {
    TEXT, IMAGE, LINK
}

data class ContentItem(
    val type: ContentType,
    val content: String,
    val style: String = "",
    val alignment: String = "left",
    val imageUrl: String = "",
    val caption: String = "",
    val linkText: String = "",
    val linkUrl: String = ""
)