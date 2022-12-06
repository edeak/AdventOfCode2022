package endredeak.aoc2022

fun main() {
    solve("Tuning Trouble") {
        val input = lines[0]

        fun marker(n: Int) = input.windowed(n).indexOfFirst { it.toSet().size == n } + n

        part1(1300) { marker(4) }

        part2(3986) { marker(14) }
    }
}