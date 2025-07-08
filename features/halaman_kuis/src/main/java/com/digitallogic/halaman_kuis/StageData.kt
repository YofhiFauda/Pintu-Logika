package com.digitallogic.halaman_kuis

data class StageData(
    val stage: Int,
    val description: String,
    val logicFunction: (GridItem) -> Boolean
)

