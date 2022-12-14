package endredeak.aoc2022

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sign

fun main() {
    solve("Regolith Reservoir") {
        fun List<Pair<Int, Int>>.draw(): List<Pair<Int, Int>> {
            val (fx, fy) = this.first()
            val (tx, ty) = this.last()
            var (cx, cy) = fx to fy

            val ret = mutableListOf(fx to fy)

            repeat(max(abs(tx - fx), abs(ty - fy))) {
                cx += (tx - fx).sign
                cy += (ty - fy).sign
                ret.add(cx to cy)
            }

            return ret
        }

        fun input() = lines
            .map { l ->
                l.split(" -> ", ",")
                    .map { it.toInt() }
                    .windowed(2, 2) { it.first() to it.last()}
                    .windowed(2)
                    .map { it.draw() }
                    .flatten()
            }
            .flatten()
            .toMutableSet()

        fun simulate(grid: MutableSet<Pair<Int, Int>>): Int {
            val maxY = grid.maxOf { it.second }
            val start = 500 to 0
            var count = 0
            var infinite = false
            while (start !in grid && !infinite) {
                var (x, y) = start
                var rest = false
                while (!rest && !infinite) {
                    when {
                        y > maxY -> infinite = true
                        x to y + 1 !in grid -> y += 1
                        x - 1 to y + 1 !in grid -> { x -= 1; y += 1 }
                        x + 1 to y + 1 !in grid -> { x += 1; y += 1 }
                        else -> rest = true
                    }
                }

                if (!infinite) { grid.add(x to y); count++ }
            }

            return count
        }

        part1(1001) { simulate(input()) }

        part2(27976) {
            input()
                .let { input ->
                    val maxY = input.maxOf { it.second }
                    input.plus((500 - maxY - 2..500 + maxY + 2).map { x -> x to maxY + 2 }).toMutableSet()
                }
                .let { simulate(it) }
        }
    }
}
