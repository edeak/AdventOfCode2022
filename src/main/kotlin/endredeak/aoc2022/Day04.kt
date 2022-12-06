package endredeak.aoc2022

fun main() {
    solve("Camp Cleanup") {
        infix fun Pair<Int, Int>.fullyContains(other: Pair<Int, Int>) =
            (this.first <= other.first && other.second <= this.second) ||
                    other.first <= this.first && this.second <= other.second

        infix fun Pair<Int, Int>.overlaps(other: Pair<Int, Int>) =
            (this.first <= other.first && other.first <= this.second) ||
                    (other.first <= this.first && this.first <= other.second)

        val input = lines
            .map { it.split(",", "-") }
            .map { it.map(String::toInt) }
            .map { (a, b, c, d) -> (a to b) to (c to d) }

        part1(471) { input.count { (l, r) -> l fullyContains r } }

        part2(888) { input.count { (l, r) -> l overlaps r } }
    }
}