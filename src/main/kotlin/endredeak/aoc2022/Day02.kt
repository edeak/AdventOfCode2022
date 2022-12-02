package endredeak.aoc2022

fun main() {
    solve("Rock Paper Scissors") {
        val input = lines.map { it.replace(" ", "") }

        val rounds = listOf("AX", "AY", "AZ", "BX", "BY", "BZ", "CX", "CY", "CZ")
        val points = listOf(4, 8, 3, 1, 5, 9, 7, 2, 6)

        part1(14069) {
            input.sumOf { points[rounds.indexOf(it)] }
        }

        part2(12411) {
            input.sumOf {
                when {
                    it.startsWith("A") -> points[(rounds.indexOf(it) + 2) % 3]
                    it.startsWith("C") -> points[(rounds.indexOf(it) + 1) % 3 + 6]
                    else -> points[rounds.indexOf(it)]
                }
            }
        }
    }
}