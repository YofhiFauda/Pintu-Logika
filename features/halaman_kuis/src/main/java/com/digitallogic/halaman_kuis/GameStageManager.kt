package com.digitallogic.halaman_kuis

object GameStageManager {

    val totalStages = 30

    fun getStage(stage: Int): StageData {
        return when (stage) {
            1 -> StageData(1, "Idempoten: A + A = A", { it.number == 1 }, "A = angka 1\nA + A tetap A\nPilih angka 1")

            2 -> StageData(2, "Komutatif: A · B = B · A", { it.number == 2 && it.color == ColorType.HIJAU }, "A = angka 2\nB = hijau\nA · B = B · A\nPilih angka 2 dan hijau")

            3 -> StageData(3, "Identitas: A · 1 = A", { it.number == 1 && it.color == ColorType.MERAH }, "A = angka 1\n1 = warna merah\nA · 1 berarti pilih angka 1 dan warna merah")

            4 -> StageData(4, "Involusi: (A')' = A", { it.number == 2 && it.color != ColorType.HIJAU }, "A = angka 2 bukan hijau\nNOT dari NOT A = A\nPilih angka 2 yang bukan hijau")

            5 -> StageData(5, "Penyerapan: A + AB = A", { it.number == 1 }, "A = angka 1\nB = warna merah\nA + AB = A (penyerapan)\nPilih angka 1")

            6 -> StageData(6, "Identitas: A + 0 = A", { it.number == 2 }, "A = angka 2\n0 adalah kondisi kosong (tidak dipilih)\nPilih hanya angka 2")

            7 -> StageData(7, "Dominasi: A + 1 = 1", { it.color == ColorType.MERAH }, "A = angka berapa pun\n1 = warna merah\nKarena A + 1 = 1,  pilih semua yang berwarna merah")

            8 -> StageData(8, "Idempoten: B + B = B", { it.number == 2 }, "B = angka 2\nB + B tetap B\nPilih angka 2")

            9 -> StageData(9, "Asosiatif: A · (B · C) = (A · B) · C", { it.number == 1 && (it.color == ColorType.MERAH || it.color == ColorType.ORANYE) }, "A = angka 1\nB = merah\nC = oranye\nPilih angka 1 yang berwarna merah atau oranye")

            10 -> StageData(10, "(A ∨ B) ∧ ¬C", { (it.number == 1 || it.number == 3) && it.color != ColorType.MERAH }, "A = angka 1\nB = angka 3\nC = merah\nPilih angka 1 atau 3 yang tidak berwarna merah")

            11 -> StageData(11, "¬A ∧ ¬B (A = 1, B = hijau)", { it.number != 1 && it.color != ColorType.HIJAU }, "A = angka 1\nB = hijau\nPilih semua yang bukan angka 1 dan bukan hijau")

            12 -> StageData(12, "¬A ∧ B (A = 2, B = oranye)", { it.number != 2 && it.color == ColorType.ORANYE }, "A = angka 2\nB = oranye\nPilih warna oranye yang bukan angka 2")

            13 -> StageData(13, "(A ∨ B) ∧ C", { (it.number == 2 || it.number == 3) && it.color == ColorType.HIJAU }, "A = angka 2\nB = angka 3\nC = hijau\nPilih angka 2 atau 3 yang berwarna hijau")

            14 -> StageData(14, "(¬A ∨ B) ∧ ¬C", { (it.number != 1 || it.number == 3) && it.color != ColorType.MERAH }, "A = angka 1\nB = angka 3\nC = merah\nPilih angka selain 1 atau angka 3 yang bukan merah")

            15 -> StageData(15, "DeMorgan: ¬(A ∨ B)", { it.number != 3 && it.color != ColorType.HIJAU }, "A = angka 3\nB = hijau\nPilih semua yang bukan angka 3 dan bukan hijau")

            16 -> StageData(16, "(A ∨ B) ∧ ¬C", { (it.number == 1 || it.color == ColorType.MERAH) && it.color != ColorType.HIJAU }, "A = angka 1\nB = merah\nC = hijau\nPilih angka 1 atau warna merah yang bukan hijau")

            17 -> StageData(17, "DeMorgan: ¬((A ∧ B) ∨ C)", { !((it.number == 1 && it.color == ColorType.MERAH) || it.color == ColorType.HIJAU) }, "A = angka 1\nB = merah\nC = hijau\nPilih selain (angka 1 dan merah) atau hijau")

            18 -> StageData(18, "¬(A ∨ B ∨ C)", { it.number != 1 && it.number != 2 && it.color != ColorType.MERAH }, "A = angka 1\nB = angka 2\nC = merah\nPilih semua yang bukan angka 1, 2, dan bukan merah")

            19 -> StageData(19, "¬((A ∧ B) ∨ C)", { !((it.color == ColorType.HIJAU && it.number == 1) || it.number == 3) }, "A = hijau\nB = angka 1\nC = angka 3\nPilih selain (hijau dan angka 1) atau angka 3")

            20 -> StageData(20, "¬A ∧ (¬B ∨ C)", { it.color != ColorType.MERAH && (it.number != 2 || it.color == ColorType.HIJAU) }, "A = merah\nB = angka 2\nC = hijau\nPilih bukan merah dan (bukan angka 2 atau hijau)")

            21 -> StageData(21, "¬((A ∧ B) ∨ (C ∧ D))", { !((it.number == 1 && it.color == ColorType.MERAH) || (it.number == 2 && it.color == ColorType.HIJAU)) }, "A = angka 1\nB = merah\nC = angka 2\nD = hijau\nPilih selain kombinasi (1 dan merah) atau (2 dan hijau)")

            22 -> StageData(22, "(A ∧ B) ∨ (C ∧ D)", { (it.number == 1 && it.color == ColorType.HIJAU) || (it.number == 2 && it.color == ColorType.ORANYE) }, "A = angka 1\nB = hijau\nC = angka 2\nD = oranye\nPilih (1 dan hijau) atau (2 dan oranye)")

            23 -> StageData(23, "¬A ∧ ¬B ∧ ¬C", { it.color != ColorType.MERAH && it.color != ColorType.ORANYE && it.number != 3 }, "A = merah\nB = oranye\nC = angka 3\nPilih selain merah, oranye dan angka 3")

            24 -> StageData(24, "(A ∨ B) ∧ (¬C ∨ D)", { (it.number == 1 || it.number == 3) && (it.color != ColorType.MERAH || it.color == ColorType.HIJAU) }, "A = angka 1\nB = angka 3\nC = merah\nD = hijau\nPilih angka 1 atau 3 dan bukan merah atau hijau")

            25 -> StageData(25, "¬((A ∨ B) ∧ C)", { !((it.number == 2 || it.color == ColorType.HIJAU) && it.color == ColorType.MERAH) }, "A = angka 2\nB = hijau\nC = merah\nPilih selain ((2 atau hijau) dan merah)")

            26 -> StageData(26, "¬((A ∨ B) ∧ (C ∨ D))", { !((it.number == 1 || it.number == 2) && (it.color == ColorType.MERAH || it.color == ColorType.HIJAU)) }, "A = angka 1\nB = angka 2\nC = merah\nD = hijau\nPilih selain (1 atau 2) dan (merah atau hijau)")

            27 -> StageData(27, "(¬A ∧ ¬B) ∨ ¬C", { (it.number != 1 && it.number != 2) || it.color != ColorType.MERAH }, "A = angka 1\nB = angka 2\nC = merah\nPilih bukan angka 1 dan 2, atau bukan merah")

            28 -> StageData(28, "(¬A ∨ B) ∧ ¬C", { (it.color != ColorType.MERAH || it.color == ColorType.HIJAU) && it.number != 3 }, "A = merah\nB = hijau\nC = angka 3\nPilih bukan merah atau hijau, dan bukan angka 3")

            29 -> StageData(29, "¬((A ∧ B) ∨ (C ∧ D))", { !((it.number == 1 && it.color == ColorType.MERAH) || (it.number == 3 && it.color == ColorType.HIJAU)) }, "A = angka 1\nB = merah\nC = angka 3\nD = hijau\nPilih selain (1 dan merah) atau (3 dan hijau)")

            30 -> StageData(30, "(¬A ∧ ¬B) ∨ (C ∧ ¬D)", { (it.number != 1 && it.color != ColorType.HIJAU) || (it.number == 2 && it.color != ColorType.MERAH) }, "A = angka 1\nB = hijau\nC = angka 2\nD = merah\nPilih bukan angka 1 dan bukan hijau, atau angka 2 dan bukan merah")

            else -> throw IllegalArgumentException("Stage tidak ditemukan: $stage")
        }
    }
}


//package com.digitallogic.halaman_kuis
//
//object GameStageManager {
//
//    val totalStages = 30
//
//    fun getStage(stage: Int): StageData {
//        return when (stage) {
//            1 -> StageData(1, "Identitas: A + 0 = A", { it.number == 1 || it.color == ColorType.MERAH }, "A = angka 1 atau merah\n0 = tidak memilih apapun\nA + 0 berarti pilih angka 1 atau merah saja")
//
//            2 -> StageData(2, "Dominasi: A + 1 = 1", { it.number == 1 || it.color == ColorType.MERAH }, "A = angka 1\n1 = merah\nA + 1 berarti pilih angka 1 atau warna merah")
//
//            3 -> StageData(3, "Komutatif: A + B = B + A", { it.number == 1 || it.color == ColorType.MERAH }, "A = angka 1\nB = warna merah\nPilih angka 1 atau merah, urutannya bebas")
//
//            4 -> StageData(4, "Komutatif\nA · B = B · A", { it.number == 1 && it.color == ColorType.MERAH }, "A = angka 1\nB = warna merah\nPilih angka 1 dan merah, urutannya tidak berpengaruh")
//
//            5 -> StageData(5, "Asosiatif\nA + (B + C) = (A + B) + C", { it.number == 1 || it.color == ColorType.MERAH || it.color == ColorType.ORANYE }, "A = angka 1\nB = merah\nC = oranye\nPilih kombinasi 1, merah, oranye, urutannya tidak penting")
//
//            6 -> StageData(6, "Asosiatif\nA · (B · C) = (A · B) · C", { it.number == 1 && it.color == ColorType.MERAH && it.color == ColorType.ORANYE }, "A = angka 1\nB = merah\nC = oranye\nPilih angka 1, merah, dan oranye secara bersamaan")
//
//            7 -> StageData(7, "Distributif\nA · (B + C) = A·B + A·C", { it.number == 1 && (it.color == ColorType.MERAH || it.color == ColorType.HIJAU) }, "A = angka 1\nB = merah\nC = hijau\nPilih angka 1 dan salah satu warna: merah atau hijau")
//
//            8 -> StageData(8, "Distributif\nA + (B · C) = (A + B) · (A + C)", { it.number == 1 || (it.color == ColorType.MERAH && it.color == ColorType.HIJAU) }, "A = angka 1\nB = merah\nC = hijau\nPilih angka 1 atau kombinasi merah dan hijau")
//
//            9 -> StageData(9, "Komplemen: A + A' = 1", { it.number == 2 || it.number == 3 }, "A = angka 2\nA' = angka 3\nPilih angka 2 atau 3 untuk menghasilkan 1")
//
//            10 -> StageData(10, "Komplemen: A · A' = 0", { it.number == 2 && it.number == 3 }, "A = angka 2\nA' = angka 3\nPilih angka 2 dan 3 sekaligus untuk hasil 0")
//
//            11 -> StageData(11, "Penyerapan: A + A·B = A", { it.number == 2 }, "A = angka 2\nB = warna merah\nPilih angka 2 saja karena A + A·B tetap A")
//
//            12 -> StageData(12, "Penyerapan: A·(A + B) = A", { it.color == ColorType.MERAH }, "A = merah\nB = hijau\nPilih warna merah saja karena hasil tetap A")
//
//            13 -> StageData(13, "Dominasi: A · 0 = 0", { it.number == 3 }, "A = angka 3\n0 = tidak memilih apapun\nPilih angka 3 saja karena hasil tetap 0")
//
//            14 -> StageData(14, "Idempoten: A + A = A", { it.color == ColorType.MERAH }, "A = merah\nPilih merah (tidak perlu dua kali), hasil tetap A")
//
//            15 -> StageData(15, "Distributif\nA + BC = (A + B)(A + C)", { it.number == 1 || (it.color == ColorType.MERAH && it.color == ColorType.HIJAU) }, "A = angka 1\nB = merah\nC = hijau\nPilih angka 1 atau merah dan hijau bersama")
//
//            16 -> StageData(16, "Identitas + Involusi\nA · 1 = A dan A'' = A", { it.number == 2 }, "A = angka 2\n1 = tidak memilih apapun (identitas)\nA'' = A\nPilih angka 2 saja\nkarena A tetap A meskipun dikalikan 1 atau dibalik dua kali")
//
//            17 -> StageData(17, "De Morgan: (A·B)' = A'+B'", { it.number == 2 || it.number == 3 }, "A = 2\nB = 3\nPilih angka 2 atau 3 untuk menghasilkan hasil logika terbalik")
//
//            18 -> StageData(18, "Komutatif + Dominasi\nA + B = B + A dan A + 1 = 1", { it.number == 1 || it.color == ColorType.MERAH }, "A = angka 1\nB = merah\nSoal: A + B = B + A (komutatif), A + 1 = 1 (dominasi)\nPilih angka 1 atau merah\nkarena hasilnya selalu 1 tanpa memperhatikan urutan")
//
//            19 -> StageData(19, "Penyerapan + Komplemen\nA + A' = 1 dan A + A·B = A", { it.color == ColorType.MERAH || it.color == ColorType.HIJAU }, "A = merah\nB = hijau\nSoal: A + A' = 1 (komplemen), A + A·B = A (penyerapan)\nPilih merah atau hijau\nkarena hasil selalu menyederhana ke A atau 1")
//
//            20 -> StageData(20, "Distributif + Involusi\nA·(B + C) = A·B + A·C dan A'' = A", { it.number == 1 && it.color == ColorType.MERAH }, "A = angka 1\nB = merah\nC = A''\nGunakan distributif dan involusi: A'' = A\nPilih angka 1 dan merah")
//
//            21 -> StageData(21, "Asosiatif + Komplemen\n(A + B) + C = A + (B + C) dan A + A' = 1", { it.number == 2 || it.color == ColorType.MERAH }, "A = angka 2\nB = merah\nSoal: A + A' = 1 dan urutan tidak memengaruhi (asosiatif)\nPilih angka 2 atau merah")
//
//            22 -> StageData(22, "Penyerapan + Distributif\nA + A·B = A dan A·(B + C) = A·B + A·C", { it.number == 1 || it.color == ColorType.MERAH }, "A = angka 1\nB = merah\nPilih angka 1 atau merah karena hasil tetap A meskipun digabung")
//
//            23 -> StageData(23, "Identitas + Komplemen\nA·1 = A dan A + A' = 1", { it.number == 1 || it.number == 2 }, "A = angka 1\nA' = angka 2\nSoal: A·1 = A (identitas), A + A' = 1 (komplemen)\nPilih angka 1 atau angka 2")
//
//            24 -> StageData(24, "Dominasi + Idempoten\nA + 1 = 1 dan A + A = A", { it.number == 2 || it.color == ColorType.MERAH }, "A = angka 2\n1 = merah\nSoal: A + 1 = 1 (dominasi), A + A = A (idempoten)\nPilih angka 2 atau merah")
//
//            25 -> StageData(25, "De Morgan + Komplemen\n(A·B)' = A' + B' dan A + A' = 1", { it.number == 1 || it.color == ColorType.MERAH }, "A = angka 1\nB = merah\nSoal: Gunakan De Morgan dan hukum komplemen\nPilih angka 1 atau merah")
//
//            26 -> StageData(26, "Idempoten + Involusi\nA = A dan A'' = A", { it.color == ColorType.ORANYE }, "A = orange\nSoal: A + A = A (idempoten), A'' = A (involusi)\nPilih orange karena tidak berubah")
//
//            27 -> StageData(27, "Komutatif + Involusi\nA + B = B + A dan A'' = A", { it.number == 1 || it.color == ColorType.HIJAU }, "A = angka 1\nB = hijau\nPilih angka 1 atau hijau\nkarena urutan tidak penting dan dua kali pembalikan tetap A")
//
//            28 -> StageData(28, "De Morgan + Idempoten\n(A + B)' = A'·B' dan A + A = A",  { it.number == 2 || it.color == ColorType.MERAH }, "A = angka 2\nB = merah\nPilih angka 2 atau merah karena hukum berlaku meski disusun ulang")
//
//            29 -> StageData(29, "Penyerapan + Identitas\nA + A·B = A dan A + 0 = A",  { it.number == 3 || it.color == ColorType.MERAH }, "A = angka 3\nB = merah\nPilih angka 3 atau merah karena hasil akhir tetap A")
//
//            30 -> StageData(30, "De Morgan + Involusi\n(A·B)' = A'+B' dan A'' = A",  { it.number == 1 || it.color == ColorType.HIJAU }, "A = angka 1\nB = hijau\nSoal: Gunakan De Morgan dan Involusi\nPilih angka 1 atau hijau")
//
//            else -> throw IllegalArgumentException("Stage tidak ditemukan: $stage")
//        }
//    }
//}
