package endredeak.aoc2022

fun main() {
    solve("Grove Positioning System") {
        fun input(key: Long? = null) = lines
            .map { it.toLong() }
            .mapIndexed { ix, i -> ix to (key?.let { it * i } ?: i) }
            .toMutableList()

        fun MutableList<Pair<Int, Long>>.decrypt() {
            indices.forEach { originalIdx ->
                val idx = indexOfFirst { originalIdx == it.first }
                val toMove = removeAt(idx)
                add((idx + toMove.second).mod(size), toMove)
            }
        }

        fun MutableList<Pair<Int, Long>>.grove() =
            this.indexOfFirst { it.second == 0L }
                .let { zero -> listOf(1000, 2000, 3000).sumOf { this[(zero + it) % this.size].second } }

        part1(13967) {
            input()
                .apply { decrypt() }
                .grove()
        }

        part2(1790365671518) {
            input(811589153)
                .apply { repeat(10) { decrypt() } }
                .grove()
        }
    }
}