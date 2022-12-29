package endredeak.aoc2022

import kotlin.math.absoluteValue

fun main() {
    /**
     * Ported Todd's solution: https://todd.ginsberg.com/post/advent-of-code/2022/day17/
     */
    solve("Pyroclastic Flow") {
        fun Char.asMove() =
            when (this) {
                '>' -> 1 to 0
                '<' -> -1 to 0
                else -> error("waat")
            }

        infix fun Set<Pair<Int, Int>>.move(m: Pair<Int, Int>) =
            m.let { (mx, my) -> this.map { (x, y) -> x + mx to y + my } }.toSet()

        fun <T> List<T>.nth(n: Int) = this[n % this.size]

        val p = listOf(
            setOf(0 to 0, 1 to 0, 2 to 0, 3 to 0),
            setOf(1 to 0, 0 to -1, 1 to -1, 2 to -1, 1 to -2),
            setOf(0 to 0, 1 to 0, 2 to 0, 2 to -1, 2 to -2),
            setOf(0 to 0, 0 to -1, 0 to -2, 0 to -3),
            setOf(0 to 0, 1 to 0, 0 to -1, 1 to -1)
        )

        val input = lines.first().map { it.asMove() }

        val grid = (0..6).map { it to 0 }.toMutableSet()
        val jets = input

        var shapeIndex = 0
        var jetIndex = 0

        fun simulate() {
            var thisShape = p.nth(shapeIndex++) move (2 to (grid.minOf { it.second } - 4))
            do {
                val jettedShape = thisShape move jets.nth(jetIndex++)
                if (jettedShape.all { it.first in (0..6) } && jettedShape.intersect(grid).isEmpty()) {
                    thisShape = jettedShape
                }
                thisShape = thisShape move (0 to 1)
            } while (thisShape.intersect(grid).isEmpty())
            grid += thisShape move (0 to -1)
        }

        part1(3130) {
            repeat(2022) { simulate() }

            grid.minOf { it.second }.absoluteValue
        }

        fun Set<Pair<Int ,Int>>.norm() =
            groupBy { it.first }
                .entries
                .sortedBy { it.key }
                .map { pointList -> pointList.value.minBy { it.second } }
                .let { points ->
                    val normalTo = this.minOf { it.second }
                    points.map { normalTo - it.second }
                }

        shapeIndex = 0
        jetIndex = 0

        fun calculateHeight(n: Long): Long {
            val seen: MutableMap<Triple<List<Int>, Int, Int>, Pair<Int, Int>> = mutableMapOf()
            while (true) {
                simulate()
                val state = Triple(grid.norm(), shapeIndex % p.size, jetIndex % jets.size)
                if (state in seen) {
                    val (startShapeIndex, startHeight) = seen.getValue(state)
                    val shapesPerLoop: Long = shapeIndex - 1L - startShapeIndex
                    val totalLoops: Long = (n - startShapeIndex) / shapesPerLoop
                    val remainingBlocksFromClosestLoopToGoal: Long =
                        (n - startShapeIndex) - (totalLoops * shapesPerLoop)
                    val heightGainedSinceLoop = grid.minOf { it.second }.absoluteValue - startHeight
                    repeat(remainingBlocksFromClosestLoopToGoal.toInt()) {
                        simulate()
                    }
                    return grid.minOf { it.second }.absoluteValue + (heightGainedSinceLoop * (totalLoops - 1))
                }
                seen[state] = shapeIndex - 1 to grid.minOf { it.second }.absoluteValue
            }
        }

        part2(1556521739139) {
            calculateHeight(1000000000000L - 1)
        }
    }
}