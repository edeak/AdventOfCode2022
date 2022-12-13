package endredeak.aoc2022

import com.google.gson.Gson

fun main() {
    solve("Distress Signal") {
        fun String.toList(): List<*> = Gson().fromJson(this, List::class.java)

        fun List<*>.compareTo(other: List<*>): Int {
            if (this.isEmpty() && other.isNotEmpty()) return -1
            else if (this.isNotEmpty() && other.isEmpty()) return 1
            else if (this.isEmpty()) return 0

            val l = this[0]
            val r = other[0]

            val result =
                if (l is Double && r is Double) l.toInt() - r.toInt()
                else if (l is Double && r is List<*>) listOf(l).compareTo(r)
                else if (l is List<*> && r is Double) l.compareTo(listOf(r))
                else (l as List<*>).compareTo(r as List<*>)

            return if (result == 0) this.drop(1).compareTo(other.drop(1)) else result
        }


        val input = lines

        part1(5252) {
            input.chunked(3) { (l, r) -> l.toList().compareTo(r.toList())}
                .withIndex()
                .filter { it.value <= 0 }
                .sumOf { it.index+1 }
        }

        part2(20592) {
            val dividers = listOf("[[2]]", "[[6]]")
            input.plus(dividers)
                .filter { it.isNotEmpty() }
                .map { it.toList() }
                .sortedWith(List<*>::compareTo)
                .mapIndexedNotNull { i, l -> if (l in dividers.map { it.toList() }) i + 1 else null }
                .let { (i1, i2) -> i1 * i2 }
        }
    }
}