package endredeak.aoc2022

fun main() {
    solve("Boiling Boulders") {
        fun Triple<Int, Int, Int>.neighbours() = run {
            val (x, y, z) = this

            setOf(
                Triple(0, 0, 1),
                Triple(0, 1, 0),
                Triple(1, 0, 0),
                Triple(0, 0, -1),
                Triple(0, -1, 0),
                Triple(-1, 0, 0),
            ).map { (dx, dy, dz) ->
                Triple(x + dx, y + dy, z + dz)
            }.toSet()
        }

        val input = lines
            .map { it.split(",") }
            .map { Triple(it[0].toInt(), it[1].toInt(), it[2].toInt()) }
            .toSet()

        part1(4504) { (input.size * 6) - input.sumOf { it.neighbours().intersect(input.minus(it)).size } }

        part2(2556) {
            val (xRange, yRange, zRange) =
                Triple(
                    input.minOf { it.first } - 1..input.maxOf { it.first } + 1,
                    input.minOf { it.second } - 1..input.maxOf { it.second } + 1,
                    input.minOf { it.third } - 1..input.maxOf { it.third } + 1
                )

            val queue = ArrayDeque<Triple<Int, Int, Int>>()
            queue.add(Triple(xRange.first, yRange.first, zRange.first))

            val seen = mutableSetOf<Triple<Int, Int, Int>>()
            var found = 0

            queue.forEach { next ->
                if (next !in seen) {
                    next.neighbours()
                        .filter { it.first in xRange && it.second in yRange && it.third in zRange }
                        .forEach { neighbor ->
                            seen += next
                            if (neighbor in input) {
                                found++
                            } else {
                                queue.add(neighbor)
                            }
                        }
                }
            }

            found
        }
    }
}