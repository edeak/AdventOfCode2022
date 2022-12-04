package endredeak.aoc2022

fun main() {
    solve( "Rucksack Reorganization") {
        fun Set<Char>.priority() = this.first().let { c ->
            ('a'..'z').indexOf(c.lowercaseChar()) + if (c.isLowerCase()) 1 else 27
        }

        val input = lines

        part1(8088) {
            input
                .map { it.chunked(it.length/2) }
                .map { (f, s) -> f.toSet() intersect s.toSet() }
                .sumOf { it.priority() }
        }

        part2(2522) {
            lines.chunked(3)
                .map { (f, s, t) -> f.toSet() intersect s.toSet() intersect t.toSet() }
                .sumOf { it.priority() }
        }
    }
}