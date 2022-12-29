package endredeak.aoc2022

import kotlin.math.abs

fun main() {
    solve("Beacon Exclusion Zone") {

        fun Pair<Int, Int>.manhattanRange(m: Int): Pair<IntRange, IntRange> =
            (this.first - m..this.first + m) to (this.second - m..this.second + m)

        infix fun Pair<Int, Int>.manhattan(other: Pair<Int, Int>) =
            abs(this.first - other.first) + abs(this.second - other.second)

        val input = lines
            .map { Regex(".*x=(-?\\d+), y=(-?\\d+).*x=(-?\\d+), y=(-?\\d+)").find(it)!!.destructured }
            .map { m -> m.toList().map { it.toInt() } }
            .map { (sx, sy, bx, by) -> (sx to sy) to (bx to by) }
            .map { Triple(it.first, it.second, it.first manhattan it.second) }

        part1(4883971) {
            val y = 10

            input
                .map { it to it.first.manhattanRange(it.third) }
                .filter { (_, r) -> y in r.second }
                .flatMap { line ->
                    val (s, _, m) = line.first
                    val d = abs(m - abs(s.second - y))
                    (s.first - d..s.first + d).map { it to y }
                }
                .toSet()
                .minus(input.map { it.second }.toSet())
                .size
        }

        part2(12691026767556) {
            val max = 4000000
            (0..max)
                .mapNotNull { x ->
                    var y = 0
                    while (y <= max) {
                        val sensor = input.find { it.first manhattan (x to y) <= it.third } ?: return@mapNotNull x to y
                        y = sensor.first.second + sensor.third - abs(x - sensor.first.first) + 1
                    }
                    null
                }
                .first()
                .let { it.first.toLong() * 4000000L + it.second.toLong() }
        }
    }
}