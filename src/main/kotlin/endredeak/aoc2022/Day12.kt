package endredeak.aoc2022

fun main() {
    solve("Hill Climbing Algorithm") {
        fun Char.value(): Int =
            when (this) {
                'S' -> 0
                'E' -> 25
                else -> this - 'a'
            }

        val dirs = setOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)

        data class P(val x: Int, val y: Int, val c: Char) {
            fun neighbours(grid: List<List<P>>, cond: (Int, Int) -> Boolean) =
                dirs
                    .mapNotNull { (x1, y1) -> grid.getOrNull(this.y + y1)?.getOrNull(this.x + x1) }
                    .filter { cond(this.c.value(), it.c.value()) }
        }

        val input = lines.mapIndexed { y, line -> line.mapIndexed { x, c -> P(x, y, c) } }

        fun bfs(start: Char, terminateIf: (P) -> Boolean, isValidNeighbour: (Int, Int) -> Boolean): Int {
            val startPoint = input.flatten().first { it.c == start }
            val discovered = mutableMapOf(startPoint to 0)
            val q = ArrayDeque<P>().apply { add(startPoint) }
            while (q.isNotEmpty()) {
                q.removeFirst().let { v ->
                    val neighbours = v.neighbours(input, isValidNeighbour)
                    if (neighbours.any { terminateIf(it) }) return discovered[v]!! + 1
                    neighbours
                        .filter { it !in discovered }
                        .forEach {
                            discovered[it] = discovered[v]!! + 1
                            q.add(it)
                        }
                }
            }

            error("waat")
        }

        part1(440) { bfs('S', { p -> p.c == 'E' }) { f, t -> t - f <= 1 } }
        part2(439) { bfs('E', { p -> p.c == 'a' }) { f, t -> f - t <= 1 } }
    }
}