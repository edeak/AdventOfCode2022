package endredeak.aoc2022

data class P(val x: Int, val y: Int, val w: Int)
fun List<P>.linesOfSight(p: P, reversed: Boolean = false) =
    listOf(this.filter { it.x == p.x }.sortedBy { it.x }, this.filter { it.y == p.y }.sortedBy { it.y })
        .flatMap { line ->
            val before = line.takeWhile { it != p }.let { if (reversed) it.reversed() else it }
            val after = line.drop(before.size + 1)
            listOf(before, after)
        }

fun main() {
    solve("Treetop Tree House") {
        val input = lines
            .map { it.toCharArray().map { c -> c.digitToInt() } }
            .mapIndexed { y, row -> row.mapIndexed { x, w -> P(x, y, w) } }
            .flatten()

        part1(1711) {
            input.count { p -> input.linesOfSight(p).any { it.all { t -> t.w < p.w } } }
        }

        part2(301392) {
            input
                .maxOf { p ->
                    input.linesOfSight(p, true)
                        .map { l -> l.indexOfFirst { it.w >= p.w }.let { if (it < 0) l.size else it + 1 } }
                        .fold(1L) { acc, i -> acc * i }
                }
        }
    }
}