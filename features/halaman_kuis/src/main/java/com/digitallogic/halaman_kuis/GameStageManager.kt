package com.digitallogic.halaman_kuis



object GameStageManager {

    val totalStages = 30

    fun getStage(stage: Int): StageData {
        return when (stage) {
            1 -> StageData(1, "not 1",
                { it.number != 1 })
            2 -> StageData(2, "not merah",
                { it.color != ColorType.MERAH })
            3 -> StageData(3, "1 and hijau",
                { it.number == 1 && it.color == ColorType.HIJAU })
            4 -> StageData(4, "2 or hijau",
                { it.number == 2 || it.color == ColorType.HIJAU })
            5 -> StageData(5, "not 1 or merah",
                { it.number != 1 && it.color != ColorType.MERAH })
            6 -> StageData(6, "not 2 and not merah",
                { (it.number != 2 && it.color != ColorType.MERAH) })
            7 -> StageData(7, "hijau and not 3",
                { it.color == ColorType.HIJAU && it.number != 3 })
            8 -> StageData(8, "1 or not hijau",
                { it.number == 1 || it.color != ColorType.HIJAU })
            9 -> StageData(9, "not hijau or merah",
                { it.color != ColorType.HIJAU && it.color != ColorType.MERAH })
            10 -> StageData(10, "1 or 3 and not merah",
                { (it.number == 1 || it.number == 3) && it.color != ColorType.MERAH })
            11 -> StageData(11, "not 1 and not hijau", {
                (it.number != 1) && (it.color != ColorType.HIJAU)
            })
            12 -> StageData(12, "not 2 and oranye", {
                it.number != 2 && it.color == ColorType.ORANYE
            })
            13 -> StageData(13, "2 or 3 and hijau", {
                (it.number == 2 || it.number == 3) && it.color == ColorType.HIJAU
            })
            14 -> StageData(14, "not 1 or 3 and not merah", {
                (it.number != 1 || it.number == 3) && it.color != ColorType.MERAH
            })
            15 -> StageData(15, "not (3 or hijau)", {
                it.number != 3 && it.color != ColorType.HIJAU
            })
            16 -> StageData(16, "(1 or merah) and (not hijau)", {
                (it.number == 1 || it.color == ColorType.MERAH) && it.color != ColorType.HIJAU
            })
            17 -> StageData(17, "not ((1 and merah) or hijau)", {
                !((it.number == 1 && it.color == ColorType.MERAH) || it.color == ColorType.HIJAU)
            })
            18 -> StageData(18, "not (1 or 2 or merah)", {
                it.number != 1 && it.number != 2 && it.color != ColorType.MERAH
            })
            19 -> StageData(19, "not ((hijau and 1) or 3)", {
                !((it.color == ColorType.HIJAU && it.number == 1) || it.number == 3)
            })
            20 -> StageData(20, "(not merah) and (not 2 or hijau)", {
                it.color != ColorType.MERAH && (it.number != 2 || it.color == ColorType.HIJAU)
            })
            21 -> StageData(21, "not ((1 and merah) or (2 and hijau))", {
                !((it.number == 1 && it.color == ColorType.MERAH) || (it.number == 2 && it.color == ColorType.HIJAU))
            })
            22 -> StageData(22, "(1 and hijau) or (2 and oranye)", {
                (it.number == 1 && it.color == ColorType.HIJAU) || (it.number == 2 && it.color == ColorType.ORANYE)
            })
            23 -> StageData(23, "not (merah or oranye) and not 3", {
                it.color != ColorType.MERAH && it.color != ColorType.ORANYE && it.number != 3
            })
            24 -> StageData(24, "((1 or 3) and (not merah or hijau))", {
                (it.number == 1 || it.number == 3) && (it.color != ColorType.MERAH || it.color == ColorType.HIJAU)
            })
            25 -> StageData(25, "not ((2 or hijau) and merah)", {
                !((it.number == 2 || it.color == ColorType.HIJAU) && it.color == ColorType.MERAH)
            })
            26 -> StageData(26, "not ((1 or 2) and (merah or hijau))", {
                !((it.number == 1 || it.number == 2) && (it.color == ColorType.MERAH || it.color == ColorType.HIJAU))
            })
            27 -> StageData(27, "(not 1 and not 2) or not merah", {
                (it.number != 1 && it.number != 2) || it.color != ColorType.MERAH
            })
            28 -> StageData(28, "((not merah) or hijau) and not 3", {
                (it.color != ColorType.MERAH || it.color == ColorType.HIJAU) && it.number != 3
            })
            29 -> StageData(29, "not ((1 and merah) or (3 and hijau))", {
                !((it.number == 1 && it.color == ColorType.MERAH) || (it.number == 3 && it.color == ColorType.HIJAU))
            })
            30 -> StageData(30, "(not 1 and not hijau) or (2 and not merah)", {
                (it.number != 1 && it.color != ColorType.HIJAU) || (it.number == 2 && it.color != ColorType.MERAH)
            })

            else -> StageData(stage, "Selesai!", { false })
        }
    }
}


