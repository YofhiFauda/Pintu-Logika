package com.digitallogic.halaman_kuis

data class GridItem(
    val number: Int,
    val color: ColorType,
    var isSelected: Boolean = false
)

enum class ColorType {
    MERAH, HIJAU, ORANYE
}
