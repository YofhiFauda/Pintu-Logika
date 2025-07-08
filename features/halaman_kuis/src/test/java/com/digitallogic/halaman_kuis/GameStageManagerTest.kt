package com.digitallogic.halaman_kuis


import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import java.util.*

class GameStageManagerTest  {

    private lateinit var testGridItems: List<GridItem>

    @Before
    fun setUp() {
        testGridItems = mutableListOf<GridItem>().apply {
            for (number in 1..3) {
                for (color in ColorType.values()) {
                    add(GridItem(number, color))
                }
            }
        }
    }

    @Test
    fun testStages1to30() {
        for (stageNum in 1..30) {
            val stage = GameStageManager.getStage(stageNum)
            println("Testing stage $stageNum: ${stage.description}")

            val results = testGridItems.map { stage.logicFunction(it) }
            val expected = when (stageNum) {
                1 -> listOf(false,false,false, true,true,true, true,true,true)
                2 -> listOf(false,true,true, false,true,true, false,true,true)
                3 -> listOf(false,true,false, false,false,false, false,false,false)
                4 -> listOf(false, true, false, true, true, true, false, true, false)
                5 -> listOf(false,false,false, false,true,true, false,true,true)
                6 -> listOf(
                    false,  // 1, MERAH
                    true,  // 1, HIJAU
                    true,  // 1, ORANYE
                    false, // 2, MERAH
                    false,  // 2, HIJAU
                    false,  // 2, ORANYE
                    false,  // 3, MERAH
                    true,  // 3, HIJAU
                    true   // 3, ORANYE
                )

                7 -> listOf(false,true,false, false,true,false, false,false,false)

                8 -> listOf(
                    true,  // 1, MERAH.
                    true,  // 1, HIJAU
                    true,  // 1, ORANYE.
                    true, // 2, MERAH
                    false,  // 2, HIJAU .
                    true,  // 2, ORANYE
                    true,  // 3, MERAH
                    false,  // 3, HIJAU .
                    true   // 3, ORANYE
                )

                9 -> listOf(false,false,true, false,false,true, false,false,true)
                10-> listOf(
                    false,  // 1, MERAH.
                    true,  // 1, HIJAU
                    true,  // 1, ORANYE
                    false,   // 2, MERAH
                    false,  // 2, HIJAU
                    false,   // 2, ORANYE
                    false,  // 3, MERAH
                    true,  // 3, HIJAU
                    true   // 3, ORANYE
                )

                11-> listOf(false, false, false, true, false, true, true, false, true)
                12-> listOf(false,false,true, false,false,false, false,false,true)
                13-> listOf(false,false,false, false,true,false, false,true,false)
                14-> listOf(false,false,false, false,true,true, false,true,true)
                15-> listOf(true,false,true, true,false,true, false,false,false)
                16-> listOf(true,false,true, true,false,false, true,false,false)
                17-> listOf(false,false,true, true,false,true, true,false,true)
                18-> listOf(false,false,false, false,false,false, false,true,true)
                19-> listOf(true,false,true, true,true,true, false,false,false)
                20-> listOf(false, true, true, false, true, false, false, true, true)
                21-> listOf(false, true, true, true, false, true, true, true, true)
                22-> listOf(false,true,false, false,false,true, false,false,false)
                23-> listOf(false, true, false, false, true, false, false, false, false)
                24-> listOf(false, true, true, false, false, false, false, true, true)
                25-> listOf(true,true,true, false,true,true, true,true,true)
                26-> listOf(false, false, true, false, false, true, true, true, true)
                27-> listOf(false, true, true, false, true, true, true, true, true)
                28-> listOf(false, true, true, false, true, true, false, false, false)
                29-> listOf(false, true, true, true, true, true, true, false, true)
                30-> listOf(false, false, false, true, true, true, true, false, true)
                else -> emptyList()
            }

            assertEquals("desc match stage $stageNum", GameStageManager.getStage(stageNum).description, stage.description)
            assertEquals("logic match stage $stageNum", expected, results)
        }
    }

    @Test fun testStageOutOfBounds() {
        val stage = GameStageManager.getStage(999)
        assertEquals("Selesai!", stage.description)
        assertFalse(stage.logicFunction(GridItem(1, ColorType.MERAH)))
    }

    @Test fun testLogicConsistency() {
        val item = GridItem(2, ColorType.HIJAU)
        for (i in 1..GameStageManager.totalStages) {
            val s = GameStageManager.getStage(i)
            assertEquals(s.logicFunction(item), s.logicFunction(item))
        }
    }

    @Test fun testDeMorganStage9() {
        val s9 = GameStageManager.getStage(9)
        testGridItems.filter { it.color == ColorType.ORANYE }.forEach {
            assertTrue("Stage9 orange should be true", s9.logicFunction(it))
        }
    }

    @Test fun testOrLogicStage4() {
        val stage = GameStageManager.getStage(4)
        val logic = stage.logicFunction

        // number = 2 OR color = HIJAU
        assertTrue(logic(GridItem(1, ColorType.HIJAU)))   // color match
        assertTrue(logic(GridItem(2, ColorType.MERAH)))   // number match
        assertTrue(logic(GridItem(2, ColorType.HIJAU)))   // both match
        assertFalse(logic(GridItem(1, ColorType.MERAH)))  // neither match
        assertFalse(logic(GridItem(3, ColorType.MERAH)))  // neither match
    }

    @Test fun testComplexStage21() {
        val s21 = GameStageManager.getStage(21)
        assertFalse(s21.logicFunction(GridItem(1, ColorType.MERAH)))
        assertFalse(s21.logicFunction(GridItem(2, ColorType.HIJAU)))
        assertTrue(s21.logicFunction(GridItem(3, ColorType.ORANYE)))
    }

    @Test fun testAllCombinationsExist() {
        assertEquals(9, testGridItems.size)
        assertEquals(setOf(1,2,3), testGridItems.map{it.number}.toSet())
        assertEquals(setOf(ColorType.MERAH, ColorType.HIJAU, ColorType.ORANYE),
            testGridItems.map{it.color}.toSet())
    }

//
//    private val testTiles = listOf(
//        GridItem(1, ColorType.MERAH),
//        GridItem(1, ColorType.HIJAU),
//        GridItem(1, ColorType.ORANYE),
//        GridItem(2, ColorType.MERAH),
//        GridItem(2, ColorType.HIJAU),
//        GridItem(2, ColorType.ORANYE),
//        GridItem(3, ColorType.MERAH),
//        GridItem(3, ColorType.HIJAU),
//        GridItem(3, ColorType.ORANYE),
//    )
//
//    private fun checkStage(stage: Int) {
//        val stageData = GameStageManager.getStage(stage)
//        val selected = testTiles.filter(stageData.logicFunction)
//
//        // Validasi logika tidak kosong dan tidak semua
//        assertTrue("❌ Stage $stage - tidak ada tile yang lolos", selected.isNotEmpty())
//        assertTrue("❌ Stage $stage - semua tile lolos", selected.size < testTiles.size)
//
//        // Validasi redudansi kombinasi number-color
//        val redundansi = selected.groupBy { it.number to it.color }
//            .filter { it.value.size > 1 }
//
//        assertTrue("❌ Stage $stage - redudansi kombinasi: ${redundansi.keys}", redundansi.isEmpty())
//    }
//
//    @Test fun testStage1_Not1() {
//        checkStage(1)
//        println("✓ testStage1_Not1 - PASSED")
//    }
//
//    @Test fun testStage2_NotMerah() {
//        checkStage(2)
//        println("✓ testStage2_NotMerah - PASSED")
//    }
//
//    @Test fun testStage3_1AndHijau() {
//        checkStage(3)
//        println("✓ testStage3_1AndHijau - PASSED")
//    }
//
//    @Test fun testStage4_2OrHijau() {
//        checkStage(4)
//        println("✓ testStage4_2OrHijau - PASSED")
//    }
//
//    @Test fun testStage5_Not1OrMerah() {
//        checkStage(5)
//        println("✓ testStage5_Not1OrMerah - PASSED")
//    }
//
//    @Test fun testStage6_Not2AndMerah() {
//        checkStage(6)
//        println("✓ testStage6_Not2AndMerah - PASSED")
//    }
//
//    @Test fun testStage7_HijauAndNot3() {
//        checkStage(7)
//        println("✓ testStage7_HijauAndNot3 - PASSED")
//    }
//
//    @Test fun testStage8_1OrNotHijau() {
//        checkStage(8)
//        println("✓ testStage8_1OrNotHijau - PASSED")
//    }
//
//    @Test fun testStage9_NotHijauOrMerah() {
//        checkStage(9)
//        println("✓ testStage9_NotHijauOrMerah - PASSED")
//    }
//
//    @Test fun testStage10_1Or3AndNotMerah() {
//        checkStage(10)
//        println("✓ testStage10_1Or3AndNotMerah - PASSED")
//    }
//
//    @Test fun testStage11_Not1AndHijau() {
//        checkStage(11)
//        println("✓ testStage11_Not1AndHijau - PASSED")
//    }
//
//    @Test fun testStage12_Not2AndOranye() {
//        checkStage(12)
//        println("✓ testStage12_Not2AndOranye - PASSED")
//    }
//
//    @Test fun testStage13_2Or3AndHijau() {
//        checkStage(13)
//        println("✓ testStage13_2Or3AndHijau - PASSED")
//    }
//
//    @Test fun testStage14_Not1Or3AndNotMerah() {
//        checkStage(14)
//        println("✓ testStage14_Not1Or3AndNotMerah - PASSED")
//    }
//
//    @Test fun testStage15_Not3OrHijau() {
//        checkStage(15)
//        println("✓ testStage15_Not3OrHijau - PASSED")
//    }
//
//    @Test fun testStage16_1OrMerahAndNotHijau() {
//        checkStage(16)
//        println("✓ testStage16_1OrMerahAndNotHijau - PASSED")
//    }
//
//    @Test fun testStage17_Not1AndMerahOrHijau() {
//        checkStage(17)
//        println("✓ testStage17_Not1AndMerahOrHijau - PASSED")
//    }
//
//    @Test fun testStage18_Not1Or2OrMerah() {
//        checkStage(18)
//        println("✓ testStage18_Not1Or2OrMerah - PASSED")
//    }
//
//    @Test fun testStage19_NotHijauAnd1Or3() {
//        checkStage(19)
//        println("✓ testStage19_NotHijauAnd1Or3 - PASSED")
//    }
//
//    @Test fun testStage20_NotMerahAndNot2OrHijau() {
//        checkStage(20)
//        println("✓ testStage20_NotMerahAndNot2OrHijau - PASSED")
//    }
//
//    @Test fun testStage21_Not1AndMerahOr2AndHijau() {
//        checkStage(21)
//        println("✓ testStage21_Not1AndMerahOr2AndHijau - PASSED")
//    }
//
//    @Test fun testStage22_1AndHijauOr2AndOranye() {
//        checkStage(22)
//        println("✓ testStage22_1AndHijauOr2AndOranye - PASSED")
//    }
//
//    @Test fun testStage23_NotMerahOrOranyeAndNot3() {
//        checkStage(23)
//        println("✓ testStage23_NotMerahOrOranyeAndNot3 - PASSED")
//    }
//
//    @Test fun testStage24_1Or3AndNotMerahOrHijau() {
//        checkStage(24)
//        println("✓ testStage24_1Or3AndNotMerahOrHijau - PASSED")
//    }
//
//    @Test fun testStage25_Not2OrHijauAndMerah() {
//        checkStage(25)
//        println("✓ testStage25_Not2OrHijauAndMerah - PASSED")
//    }
//
//    @Test fun testStage26_Not1Or2AndMerahOrHijau() {
//        checkStage(26)
//        println("✓ testStage26_Not1Or2AndMerahOrHijau - PASSED")
//    }
//
//    @Test fun testStage27_Not1And2OrNotMerah() {
//        checkStage(27)
//        println("✓ testStage27_Not1And2OrNotMerah - PASSED")
//    }
//
//    @Test fun testStage28_NotMerahOrHijauAndNot3() {
//        checkStage(28)
//        println("✓ testStage28_NotMerahOrHijauAndNot3 - PASSED")
//    }
//
//    @Test fun testStage29_Not1AndMerahOr3AndHijau() {
//        checkStage(29)
//        println("✓ testStage29_Not1AndMerahOr3AndHijau - PASSED")
//    }
//
//    @Test fun testStage30_Not1HijauOr2NotMerah() {
//        checkStage(30)
//        println("✓ testStage30_Not1HijauOr2NotMerah - PASSED")
//    }
//
//
//    @Test
//    fun testAllStagesHaveValidLogic() {
//        for (i in 1..GameStageManager.totalStages) {
//            checkStage(i)
//        }
//        println("✓ testAllStagesHaveValidLogic - PASSED")
//    }

}
