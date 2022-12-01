package endredeak.aoc2022

import endredeak.aoc2022.lib.utils.splitBy

fun main() {
    solve("Calorie Counting") {
        val input = lines
            .splitBy { it.isEmpty() }
            .map { it.sumOf(String::toInt) }

        part1(72478) {
            input.max()
        }

        part2(210367) {
            input
                .sortedDescending()
                .take(3)
                .sum()
        }
    }
}