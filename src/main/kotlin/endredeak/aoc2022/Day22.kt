package endredeak.aoc2022

fun main() {
    solve("") {
        fun <T> List<List<T>>.transpose(): List<List<T>> {
            val ret = mutableListOf<MutableList<T>>()

            this.first().indices.forEach { col ->
                ret.add(this.map { row -> row[col] }.toMutableList())
            }

            return ret
        }

        val input = run {
            val grid = lines
                .dropLast(2)

            val max = grid.maxOf { it.length }
            grid
                .map { if (it.length != max) "$it${" ".repeat(max - it.length)}" else it }
                .map { line -> line.map { it } } to lines.last()
                .replace("R", ",R,")
                .replace("L", ",L,")
                .split(",")
        }

        val dirs = listOf(
            1 to 0,
            0 to 1,
            -1 to 0,
            0 to -1
        )

        part1 {
            val (grid, inst) = input
            var pos = grid[0].indexOfFirst { it == '.' } to 0
            var dir = dirs[0]


            inst.onEach { println(it) }
            -1
        }

        part2 {

        }
    }
}