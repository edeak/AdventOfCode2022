package endredeak.aoc2022

import endredeak.aoc2022.lib.utils.toMutableMap

fun main() {
    solve("Supply Stacks") {
        val regex = Regex("move (\\d+) from (\\d+) to (\\d+)")

        data class Move(val size: Int, val from: Int, val to: Int)

        fun input() = run {
            val stackLines = lines.filter { it.contains("[") }
            val ops = lines.filter { it.contains("move") }

            val stacks = mutableMapOf<Int, MutableList<Char>>()

            stackLines
                .forEach {
                    it.chunked(4)
                        .mapIndexed { i, r -> i to r }
                        .filterNot { (_, r) -> r.isBlank() }
                        .forEach { (i, r) -> stacks[i + 1]?.add(r[1]) ?: run { stacks[i + 1] = mutableListOf(r[1]) } }
                }

            val moves = ops
                .map {
                    regex.find(it)!!
                        .destructured
                        .let { (s, f, t) -> Move(s.toInt(), f.toInt(), t.toInt()) }
                }

            stacks.map { (k, v) -> k to v.reversed().toMutableList() }.toMutableMap() to moves
        }

        fun runMoving(reversed: Boolean) =
            input()
                .let { (stacks, moves) ->
                    moves.forEach { (s, f, t) ->
                        val toMove = stacks[f]!!.takeLast(s).let { if (reversed) it.reversed() else it }
                        val remaining = stacks[f]!!.dropLast(s)
                        val new = stacks[t]!!.apply { addAll(toMove) }
                        stacks[f] = remaining.toMutableList()
                        stacks[t] = new.toMutableList()
                    }

                    stacks
                }
                .toSortedMap()
                .map { it.value.last() }
                .joinToString("")

        part1("GRTSWNJHH") { runMoving(true) }

        part2("QLFQDBBHM") { runMoving(false) }
    }
}