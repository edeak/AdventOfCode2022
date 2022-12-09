package endredeak.aoc2022

import kotlin.math.absoluteValue
import kotlin.math.sign

fun main() {
    solve("Rope Bridge") {
        val input = lines
            .map { it.split(" ") }
            .map { (d, a) -> d to a.toInt() }

        operator fun Pair<Int, Int>.plus(p: Pair<Int, Int>) = this.first + p.first to this.second + p.second
        operator fun Pair<Int, Int>.minus(p: Pair<Int, Int>) = this.first - p.first to this.second - p.second
        infix fun Pair<Int, Int>.follow(p: Pair<Int, Int>) = this.first + (p.first-this.first).sign to this.second + (p.second-this.second).sign
        fun Pair<Int, Int>.notInRange() = listOf(this.first.absoluteValue, this.second.absoluteValue).any { it > 1}

        fun move(dir: String) =
            when (dir) {
                "R" -> 1 to 0
                "U" -> 0 to 1
                "L" -> -1 to 0
                else -> 0 to -1
            }

        fun simulateRope(n: Int = 2): Int {
            val knots = MutableList(n) { 0 to 0 }
            val path = mutableSetOf(0 to 0)

            input.forEach { (dir, dist) ->
                repeat(dist) {
                    knots[0] += move(dir)
                    repeat(knots.size-1) { i ->
                        val h = knots[i]
                        val t = knots[i+1]

                        if ((h-t).notInRange()) { knots[i+1] = t follow h }

                        path += knots.last()
                    }
                }
            }

            return path.size
        }

        part1(6314) { simulateRope(2) }

        part2(2504) { simulateRope(10) }
    }
}