package com.digitallogic.halaman_kuis

object LevelManager {
    private val levels = mutableMapOf<Int, LevelConfig>()
    val maxLevel: Int get() = levels.size

    init {
        levels[1] = LevelConfig(
            level = 1,
            inputs = 4,
            expectedOutput = true,
            layoutResId = R.layout.item_level_1,
            gates = listOf(
                GateConfig("AND1", "AND", listOf("INPUT1", "INPUT2")),
                GateConfig("AND2", "AND", listOf("INPUT3", "INPUT4")),
                GateConfig("AND3", "AND", listOf("AND1", "AND2"), isFinal = true)
            ),
            connections = listOf(
                ConnectionConfig("input1", "dot_output", "AND1", "dot_input_0"),
                ConnectionConfig("input2", "dot_output", "AND1", "dot_input_1"),

                ConnectionConfig("input3", "dot_output", "AND2", "dot_input_0"),
                ConnectionConfig("input4", "dot_output", "AND2", "dot_input_1"),

                ConnectionConfig("AND1", "dot_output", "AND3", "dot_input_0"),

                ConnectionConfig("AND2", "dot_output", "AND3", "dot_input_1"),

                ConnectionConfig("AND3", "dot_output", "layout_Output", "output_dot2")
            )
        )

        levels[2] = LevelConfig(
            level = 2,
            inputs = 4,
            expectedOutput = true,
            layoutResId = R.layout.item_level_2,
            gates = listOf(
                GateConfig("OR1", "OR", listOf("INPUT1", "INPUT2")),
                GateConfig("OR2", "OR", listOf("INPUT3", "INPUT4")),
                GateConfig("AND1", "AND", listOf("OR1", "OR2"), isFinal = true)
            ),
            connections = listOf(
                ConnectionConfig("input1", "dot_output", "OR1", "dot_input_0"),
                ConnectionConfig("input2", "dot_output", "OR1", "dot_input_1"),

                ConnectionConfig("input3", "dot_output", "OR2", "dot_input_0"),
                ConnectionConfig("input4", "dot_output", "OR2", "dot_input_1"),

                ConnectionConfig("OR1", "dot_output", "AND1", "dot_input_0"),
                ConnectionConfig("OR2", "dot_output", "AND1", "dot_input_1"),

                ConnectionConfig("AND1", "dot_output", "layout_Output", "output_dot2")
            )
        )

        levels[3] = LevelConfig(
            level = 3,
            inputs = 4,
            expectedOutput = true,
            layoutResId = R.layout.item_level_3,
            gates = listOf(
                GateConfig("AND1", "AND", listOf("INPUT1", "INPUT2")),
                GateConfig("AND2", "AND", listOf("INPUT3", "INPUT4")),
                GateConfig("OR1", "OR", listOf("AND1", "AND2"), isFinal = true)
            ),
            connections = listOf(
                ConnectionConfig("input1", "dot_output", "AND1", "dot_input_0"),
                ConnectionConfig("input2", "dot_output", "AND1", "dot_input_1"),

                ConnectionConfig("input3", "dot_output", "AND2", "dot_input_0"),
                ConnectionConfig("input4", "dot_output", "AND2", "dot_input_1"),

                ConnectionConfig("AND1", "dot_output", "OR1", "dot_input_0"),
                ConnectionConfig("AND2", "dot_output", "OR1", "dot_input_1"),

                ConnectionConfig("OR1", "dot_output", "layout_Output", "output_dot2")
            )
        )

        levels[4] = LevelConfig(
            level = 4,
            inputs = 4,
            expectedOutput = true,
            layoutResId = R.layout.item_level_4,
            gates = listOf(
                GateConfig("AND1", "AND", listOf("INPUT1", "INPUT2")),
                GateConfig("OR1", "OR", listOf("INPUT3", "INPUT4")),
                GateConfig("OR2", "OR", listOf("AND1", "OR1")),
                GateConfig("NOT1", "NOT", listOf("OR2"), isFinal = true)
            ),
            connections = listOf(
                ConnectionConfig("input1", "dot_output", "AND1", "dot_input_0"),
                ConnectionConfig("input2", "dot_output", "AND1", "dot_input_1"),

                ConnectionConfig("input3", "dot_output", "OR1", "dot_input_0"),
                ConnectionConfig("input4", "dot_output", "OR1", "dot_input_1"),

                ConnectionConfig("AND1", "dot_output", "OR2", "dot_input_0"),
                ConnectionConfig("OR1", "dot_output", "OR2", "dot_input_1"),

                ConnectionConfig("OR2", "dot_output", "NOT1", "dot_input_0"),

                ConnectionConfig("NOT1", "dot_output", "layout_Output", "output_dot2")
            )
        )

        levels[5] = LevelConfig(
            level = 5,
            inputs = 4,
            expectedOutput = true,
            layoutResId = R.layout.item_level_5,
            gates = listOf(
                GateConfig("OR1", "OR", listOf("INPUT1", "INPUT2")),
                GateConfig("NOT1", "NOT", listOf("INPUT3")),
                GateConfig("AND1", "AND", listOf("NOT1", "INPUT4")),
                GateConfig("NOT2", "NOT", listOf("OR1")),
                GateConfig("NOT3", "NOT", listOf("AND1")),
                GateConfig("AND2", "AND", listOf("NOT2", "NOT3"), isFinal = true)
            ),
            connections = listOf(
                ConnectionConfig("input1", "dot_output", "OR1", "dot_input_0"),
                ConnectionConfig("input2", "dot_output", "OR1", "dot_input_1"),

                ConnectionConfig("input3", "dot_output", "NOT1", "dot_input_0"),
                ConnectionConfig("NOT1", "dot_output", "AND1", "dot_input_0"),
                ConnectionConfig("input4", "dot_output", "AND1", "dot_input_1"),


                ConnectionConfig("OR1", "dot_output", "NOT2", "dot_input_0"),
                ConnectionConfig("AND1", "dot_output", "NOT3", "dot_input_0"),

                ConnectionConfig("NOT2", "dot_output", "AND2", "dot_input_0"),
                ConnectionConfig("NOT3", "dot_output", "AND2", "dot_input_1"),

                ConnectionConfig("AND2", "dot_output", "layout_Output", "output_dot2")
            )
        )

        levels[6] = LevelConfig(
            level = 6,
            inputs = 4,
            expectedOutput = true,
            layoutResId = R.layout.item_level_6,
            gates = listOf(
                GateConfig("NAND1", "NAND", listOf("INPUT1", "INPUT2")),
                GateConfig("OR1", "OR", listOf("INPUT3", "INPUT4")),
                GateConfig("NOT1", "NOT", listOf("OR1")),
                GateConfig("OR2", "OR", listOf("NAND1", "NOT1")),
                GateConfig("NOT2", "NOT", listOf("OR2"), isFinal = true)
            ),
            connections = listOf(
                ConnectionConfig("input1", "dot_output", "NAND1", "dot_input_0"),
                ConnectionConfig("input2", "dot_output", "NAND1", "dot_input_1"),

                ConnectionConfig("input3", "dot_output", "OR1", "dot_input_0"),
                ConnectionConfig("input4", "dot_output", "OR1", "dot_input_1"),

                ConnectionConfig("OR1", "dot_output", "NOT1", "dot_input_0"),

                ConnectionConfig("NAND1", "dot_output", "OR2", "dot_input_0"),
                ConnectionConfig("NOT1", "dot_output", "OR2", "dot_input_1"),

                ConnectionConfig("OR2", "dot_output", "NOT2", "dot_input_0"),

                ConnectionConfig("NOT2", "dot_output", "layout_Output", "output_dot2")
            )
        )

        levels[7] = LevelConfig(
            level = 7,
            inputs = 6,
            expectedOutput = true,
            layoutResId = R.layout.item_level_7,
            gates = listOf(
                GateConfig("AND1", "AND", listOf("INPUT1", "INPUT2")),
                GateConfig("NOR1", "NOR", listOf("INPUT3", "INPUT4")),
                GateConfig("AND2", "AND", listOf("INPUT5", "NOT1")),
                GateConfig("NOT1", "NOT", listOf("INPUT6")),

                GateConfig("AND3", "AND", listOf("NOR1", "AND2")),

                GateConfig("AND4", "AND", listOf("AND1", "AND3"), isFinal = true),

            ),
            connections = listOf(
                ConnectionConfig("input1", "dot_output", "AND1", "dot_input_0"),
                ConnectionConfig("input2", "dot_output", "AND1", "dot_input_1"),

                ConnectionConfig("input3", "dot_output", "NOR1", "dot_input_0"),
                ConnectionConfig("input4", "dot_output", "NOR1", "dot_input_1"),

                ConnectionConfig("input5", "dot_output", "AND2", "dot_input_0"),
                ConnectionConfig("input6", "dot_output", "NOT1", "dot_input_0"),
                ConnectionConfig("NOT1", "dot_output", "AND2", "dot_input_1"),

                ConnectionConfig("NOR1", "dot_output", "AND3", "dot_input_0"),
                ConnectionConfig("AND2", "dot_output", "AND3", "dot_input_1"),

                ConnectionConfig("AND1", "dot_output", "AND4", "dot_input_0"),
                ConnectionConfig("AND3", "dot_output", "AND4", "dot_input_1"),

                ConnectionConfig("AND4", "dot_output", "layout_Output", "output_dot2")
            )
        )

        levels[8] = LevelConfig(
            level = 8,
            inputs = 6,
            expectedOutput = true,
            layoutResId = R.layout.item_level_8,
            gates = listOf(
                GateConfig("NOR1", "NOR", listOf("INPUT1", "INPUT2")),
                GateConfig("AND1", "AND", listOf("INPUT3", "INPUT4")),
                GateConfig("NOT1", "NOT", listOf("INPUT5")),
                GateConfig("OR1", "OR", listOf("INPUT6", "NOT1")),

                GateConfig("NOT2", "NOT", listOf("OR1")),

                GateConfig("OR2", "OR", listOf( "NOT2", "AND1")),

                GateConfig("OR3", "OR", listOf("NOR1", "OR2"), isFinal = true),

                ),
            connections = listOf(
                ConnectionConfig("input1", "dot_output", "NOR1", "dot_input_0"),
                ConnectionConfig("input2", "dot_output", "NOR1", "dot_input_1"),

                ConnectionConfig("input3", "dot_output", "AND1", "dot_input_0"),
                ConnectionConfig("input4", "dot_output", "AND1", "dot_input_1"),

                ConnectionConfig("input5", "dot_output", "NOT1", "dot_input_0"),
                ConnectionConfig("input6", "dot_output", "OR1", "dot_input_1"),
                ConnectionConfig("NOT1", "dot_output", "OR1", "dot_input_0"),

                ConnectionConfig("OR1", "dot_output", "NOT2", "dot_input_0"),

                ConnectionConfig("AND1", "dot_output", "OR2", "dot_input_0"),
                ConnectionConfig("NOT2", "dot_output", "OR2", "dot_input_1"),

                ConnectionConfig("OR2", "dot_output", "OR3", "dot_input_1"),
                ConnectionConfig("NOR1", "dot_output", "OR3", "dot_input_0"),

                ConnectionConfig("OR3", "dot_output", "layout_Output", "output_dot2")
            )
        )

        levels[9] = LevelConfig(
            level = 9,
            inputs = 6,
            expectedOutput = true,
            layoutResId = R.layout.item_level_9,
            gates = listOf(
                GateConfig("OR1", "OR", listOf("INPUT1", "INPUT2")),
                GateConfig("OR2", "OR", listOf("INPUT3", "INPUT4")),
                GateConfig("NOT1", "NOT", listOf("INPUT5")),
                GateConfig("AND1", "AND", listOf("INPUT6", "NOT1")),

                GateConfig("NOT2", "NOT", listOf("OR2")),

                GateConfig("AND2", "AND", listOf( "OR1", "NOT2")),

                GateConfig("NOR1", "NOR", listOf("AND2", "AND1"), isFinal = true),

                ),
            connections = listOf(
                ConnectionConfig("input1", "dot_output", "OR1", "dot_input_0"),
                ConnectionConfig("input2", "dot_output", "OR1", "dot_input_1"),

                ConnectionConfig("input3", "dot_output", "OR2", "dot_input_0"),
                ConnectionConfig("input4", "dot_output", "OR2", "dot_input_1"),

                ConnectionConfig("input5", "dot_output", "NOT1", "dot_input_0"),
                ConnectionConfig("input6", "dot_output", "AND1", "dot_input_1"),
                ConnectionConfig("NOT1", "dot_output", "AND1", "dot_input_0"),


                ConnectionConfig("OR2", "dot_output", "NOT2", "dot_input_0"),

                ConnectionConfig("NOT2", "dot_output", "AND2", "dot_input_1"),
                ConnectionConfig("OR1", "dot_output", "AND2", "dot_input_0"),

                ConnectionConfig("AND1", "dot_output", "NOR1", "dot_input_1"),
                ConnectionConfig("AND2", "dot_output", "NOR1", "dot_input_0"),

                ConnectionConfig("NOR1", "dot_output", "layout_Output", "output_dot2")
            )
        )

        levels[10] = LevelConfig(
            level = 10,
            inputs = 6,
            expectedOutput = true,
            layoutResId = R.layout.item_level_10,
            gates = listOf(
                GateConfig("AND1", "AND", listOf("INPUT1", "INPUT2")),
                GateConfig("AND2", "AND", listOf("INPUT3", "INPUT4")),
                GateConfig("OR1", "OR", listOf("INPUT5", "INPUT6")),

                GateConfig("OR2", "OR", listOf("AND2", "OR1")),

                GateConfig("AND3", "AND", listOf("AND1", "AND2")),

                GateConfig("AND4", "AND", listOf("AND3", "OR2"), isFinal = true),

                ),
            connections = listOf(
                ConnectionConfig("input1", "dot_output", "AND1", "dot_input_0"),
                ConnectionConfig("input2", "dot_output", "AND1", "dot_input_1"),

                ConnectionConfig("input3", "dot_output", "AND2", "dot_input_0"),
                ConnectionConfig("input4", "dot_output", "AND2", "dot_input_1"),

                ConnectionConfig("input5", "dot_output", "OR1", "dot_input_0"),
                ConnectionConfig("input6", "dot_output", "OR1", "dot_input_1"),


                ConnectionConfig("AND1", "dot_output", "AND3", "dot_input_0"),

                ConnectionConfig("AND2", "dot_output", "AND3", "dot_input_1"),
                ConnectionConfig("AND2", "dot_output", "OR2", "dot_input_0"),

                ConnectionConfig("OR1", "dot_output", "OR2", "dot_input_1"),


                ConnectionConfig("AND3", "dot_output", "AND4", "dot_input_0"),
                ConnectionConfig("OR2", "dot_output", "AND4", "dot_input_1"),

                ConnectionConfig("AND4", "dot_output", "layout_Output", "output_dot2")
            )
        )

        levels[11] = LevelConfig(
            level = 11,
            inputs = 6,
            expectedOutput = true,
            layoutResId = R.layout.item_level_11,
            gates = listOf(
                GateConfig("NOR1", "NOR", listOf("INPUT1", "INPUT2")),
                GateConfig("OR1", "OR", listOf("INPUT3", "INPUT4")),
                GateConfig("AND1", "AND", listOf("INPUT5", "INPUT6")),

                GateConfig("NOT1", "NOT", listOf("OR1")),

                GateConfig("OR2", "OR", listOf("NOR1", "OR1")),
                GateConfig("AND2", "AND", listOf( "NOT1", "AND1")),

                GateConfig("AND3", "AND", listOf("OR2", "AND2"), isFinal = true),

                ),
            connections = listOf(
                ConnectionConfig("input1", "dot_output", "NOR1", "dot_input_0"),
                ConnectionConfig("input2", "dot_output", "NOR1", "dot_input_1"),

                ConnectionConfig("input3", "dot_output", "OR1", "dot_input_0"),
                ConnectionConfig("input4", "dot_output", "OR1", "dot_input_1"),

                ConnectionConfig("input5", "dot_output", "AND1", "dot_input_0"),
                ConnectionConfig("input6", "dot_output", "AND1", "dot_input_1"),


                ConnectionConfig("NOR1", "dot_output", "OR2", "dot_input_0"),
                ConnectionConfig("OR1", "dot_output", "OR2", "dot_input_1"),
                ConnectionConfig("OR1", "dot_output", "NOT1", "dot_input_0"),

                ConnectionConfig("NOT1", "dot_output", "AND2", "dot_input_0"),
                ConnectionConfig("AND1", "dot_output", "AND2", "dot_input_1"),

                ConnectionConfig("OR2", "dot_output", "AND3", "dot_input_0"),
                ConnectionConfig("AND2", "dot_output", "AND3", "dot_input_1"),

                ConnectionConfig("AND3", "dot_output", "layout_Output", "output_dot2")
            )
        )

        levels[12] = LevelConfig(
            level = 12,
            inputs = 6,
            expectedOutput = true,
            layoutResId = R.layout.item_level_12,
            gates = listOf(
                GateConfig("AND1", "AND", listOf("INPUT1", "INPUT2")),
                GateConfig("AND2", "AND", listOf("INPUT3", "INPUT4")),
                GateConfig("OR1", "OR", listOf("INPUT5", "INPUT6")),

                GateConfig("NOR1", "NOR", listOf("AND1", "AND2")),
                GateConfig("OR2", "OR", listOf("AND2", "OR1")),

                GateConfig("NOT1", "NOT", listOf( "OR2")),

                GateConfig("AND3", "AND", listOf("NOR1", "NOT1"), isFinal = true),

                ),
            connections = listOf(

                //Baris Pertama
                ConnectionConfig("input1", "dot_output", "AND1", "dot_input_0"),
                ConnectionConfig("input2", "dot_output", "AND1", "dot_input_1"),

                ConnectionConfig("input3", "dot_output", "AND2", "dot_input_0"),
                ConnectionConfig("input4", "dot_output", "AND2", "dot_input_1"),

                ConnectionConfig("input5", "dot_output", "OR1", "dot_input_0"),
                ConnectionConfig("input6", "dot_output", "OR1", "dot_input_1"),

                //Baris Kedua
                ConnectionConfig("AND1", "dot_output", "NOR1", "dot_input_0"),
                ConnectionConfig("AND2", "dot_output", "NOR1", "dot_input_1"),

                ConnectionConfig("AND2", "dot_output", "OR2", "dot_input_0"),
                ConnectionConfig("OR1", "dot_output", "OR2", "dot_input_1"),

                //BARIS KETIDA
                ConnectionConfig("NOR1", "dot_output", "AND3", "dot_input_0"),
                ConnectionConfig("OR2", "dot_output", "NOT1", "dot_input_0"),

                ConnectionConfig("NOT1", "dot_output", "AND3", "dot_input_1"),

                ConnectionConfig("AND3", "dot_output", "layout_Output", "output_dot2")
            )
        )

        levels[13] = LevelConfig(
            level = 13,
            inputs = 6,
            expectedOutput = true,
            layoutResId = R.layout.item_level_13,
            gates = listOf(
                GateConfig("AND1", "AND", listOf("INPUT1", "INPUT2")),
                GateConfig("NOT1", "NOT", listOf("INPUT3")),
                GateConfig("OR1", "OR", listOf("INPUT4", "INPUT5")),

                GateConfig("OR2", "OR", listOf("NOT1", "OR1")),

                GateConfig("NOT2", "NOT", listOf("OR2")),

                GateConfig("AND2", "AND", listOf( "AND1", "OR2")),
                GateConfig("OR3", "OR", listOf( "NOT2", "INPUT6")),

                GateConfig("AND3", "AND", listOf("AND2", "OR3"), isFinal = true),

                ),
            connections = listOf(
                ConnectionConfig("input1", "dot_output", "AND1", "dot_input_0"),
                ConnectionConfig("input2", "dot_output", "AND1", "dot_input_1"),

                ConnectionConfig("input3", "dot_output", "NOT1", "dot_input_0"),

                ConnectionConfig("input4", "dot_output", "OR1", "dot_input_0"),
                ConnectionConfig("input5", "dot_output", "OR1", "dot_input_1"),

                ConnectionConfig("NOT1", "dot_output", "OR2", "dot_input_0"),
                ConnectionConfig("OR1", "dot_output", "OR2", "dot_input_1"),

                ConnectionConfig("OR2", "dot_output", "NOT2", "dot_input_0"),

                ConnectionConfig("AND1", "dot_output", "AND2", "dot_input_0"),
                ConnectionConfig("OR2", "dot_output", "AND2", "dot_input_1"),

                ConnectionConfig("NOT2", "dot_output", "OR3", "dot_input_0"),
                ConnectionConfig("input6", "dot_output", "OR3", "dot_input_1"),


                ConnectionConfig("AND2", "dot_output", "AND3", "dot_input_0"),
                ConnectionConfig("OR3", "dot_output", "AND3", "dot_input_1"),

                ConnectionConfig("AND3", "dot_output", "layout_Output", "output_dot2")
            )
        )

        levels[14] = LevelConfig(
            level = 14,
            inputs = 6,
            expectedOutput = true,
            layoutResId = R.layout.item_level_14,
            gates = listOf(
                GateConfig("NOT1", "NOT", listOf("INPUT1")),
                GateConfig("AND1", "AND", listOf("INPUT2", "INPUT3")),
                GateConfig("OR1", "OR", listOf("INPUT4", "INPUT5")),
                GateConfig("NOT2", "NOT", listOf("INPUT6")),

                GateConfig("OR2", "OR", listOf("NOT1", "AND1")),
                GateConfig("AND2", "AND", listOf( "AND1", "OR1")),
                GateConfig("AND3", "AND", listOf( "OR1", "NOT2")),

                GateConfig("OR3", "OR", listOf( "OR2", "AND2")),
                GateConfig("OR4", "OR", listOf( "AND2", "AND3")),

                GateConfig("OR5", "OR", listOf( "OR3", "OR4")),

                GateConfig("NOT3", "NOT", listOf("OR5"), isFinal = true),
                ),
            connections = listOf(

                //BARIS PERTAMA
                ConnectionConfig("input1", "dot_output", "NOT1", "dot_input_0"),

                ConnectionConfig("input2", "dot_output", "AND1", "dot_input_0"),
                ConnectionConfig("input3", "dot_output", "AND1", "dot_input_1"),

                ConnectionConfig("input4", "dot_output", "OR1", "dot_input_0"),
                ConnectionConfig("input5", "dot_output", "OR1", "dot_input_1"),

                ConnectionConfig("input6", "dot_output", "NOT2", "dot_input_0"),

                //BARIS KEDUA
                ConnectionConfig("NOT1", "dot_output", "OR2", "dot_input_0"),
                ConnectionConfig("AND1", "dot_output", "OR2", "dot_input_1"),

                ConnectionConfig("AND1", "dot_output", "AND2", "dot_input_0"),
                ConnectionConfig("OR1", "dot_output", "AND2", "dot_input_1"),

                ConnectionConfig("OR1", "dot_output", "AND3", "dot_input_0"),
                ConnectionConfig("NOT2", "dot_output", "AND3", "dot_input_1"),

                //BARIS KETIGA
                ConnectionConfig("OR2", "dot_output", "OR3", "dot_input_0"),
                ConnectionConfig("AND2", "dot_output", "OR3", "dot_input_1"),

                ConnectionConfig("AND2", "dot_output", "OR4", "dot_input_0"),
                ConnectionConfig("AND3", "dot_output", "OR4", "dot_input_1"),

                //BARIS KEEMPAT

                ConnectionConfig("OR3", "dot_output", "OR5", "dot_input_0"),
                ConnectionConfig("OR4", "dot_output", "OR5", "dot_input_1"),

                ConnectionConfig("OR5", "dot_output", "NOT3", "dot_input_0"),

                ConnectionConfig("NOT3", "dot_output", "layout_Output", "output_dot2")
            )
        )

        levels[15] = LevelConfig(
            level = 15,
            inputs = 6,
            expectedOutput = true,
            layoutResId = R.layout.item_level_15,
            gates = listOf(
                GateConfig("AND1", "AND", listOf("INPUT1", "INPUT2")),
                GateConfig("AND2", "AND", listOf("INPUT3", "INPUT4")),
                GateConfig("OR1", "OR", listOf("INPUT5", "INPUT6")),


                GateConfig("OR2", "OR", listOf("AND1", "AND2")),
                GateConfig("OR3", "OR", listOf("AND2", "OR1")),

                GateConfig("XOR1", "XOR", listOf("OR2", "OR3"), isFinal = true),
            ),
            connections = listOf(

                //BARIS PERTAMA
                ConnectionConfig("input1", "dot_output", "AND1", "dot_input_0"),
                ConnectionConfig("input2", "dot_output", "AND1", "dot_input_1"),

                ConnectionConfig("input3", "dot_output", "AND2", "dot_input_0"),
                ConnectionConfig("input4", "dot_output", "AND2", "dot_input_1"),

                ConnectionConfig("input5", "dot_output", "OR1", "dot_input_0"),
                ConnectionConfig("input6", "dot_output", "OR1", "dot_input_1"),

                //BARIS KEDUA
                ConnectionConfig("AND1", "dot_output", "OR2", "dot_input_0"),
                ConnectionConfig("AND2", "dot_output", "OR2", "dot_input_1"),

                ConnectionConfig("AND2", "dot_output", "OR3", "dot_input_0"),
                ConnectionConfig("OR1", "dot_output", "OR3", "dot_input_1"),

                //BARIS KETIGA
                ConnectionConfig("OR2", "dot_output", "XOR1", "dot_input_0"),
                ConnectionConfig("OR3", "dot_output", "XOR1", "dot_input_1"),

                ConnectionConfig("XOR1", "dot_output", "layout_Output", "output_dot2")
            )
        )

    }

    fun getLevelConfig(level: Int): LevelConfig? {
        return levels[level]
    }
}
