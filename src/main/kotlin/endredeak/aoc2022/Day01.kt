package endredeak.aoc2022

fun main() {
    solve("Calorie Counting") {
        val input = text
            .split("\n\n")
            .map { it.split("\n").sumOf(String::toInt) }


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