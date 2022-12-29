package endredeak.aoc2022

import endredeak.aoc2022.lib.utils.print

enum class Dir {
    N, E, S, W, NE, NW, SE, SW;

    fun toPos(): Pair<Int, Int> =
        when(this) {
            N -> 0 to -1
            E -> 1 to 0
            S -> 0 to 1
            W -> -1 to 0
            NE -> 1 to -1
            NW -> -1 to -1
            SE -> 1 to 1
            SW -> -1 to 1
        }
}

        fun Pair<Int, Int>.adjacents() =
            (-1..1).flatMap { y ->
                (-1..1).map { x ->
                    x to y
                }
            }.filter { it != this  }
                .map { (dx, dy) -> this.first + dx to (this.second + dy)}

        operator fun List<List<Char>>.get(pos: Pair<Int, Int>) =
            this[pos.second][pos.first]

        operator fun Pair<Int, Int>.minus(other: Pair<Int, Int>): Dir=
            when ((this.first-other.first) to (this.second-other.second)) {
                0 to -1 -> Dir.N
                1 to 0 -> Dir.E
                0 to 1 -> Dir.S
                -1 to 0 -> Dir.W
                1 to -1 -> Dir.NE
                -1 to -1 -> Dir.NW
                1 to 1 -> Dir.SE
                -1 to 1 -> Dir.SW
                else -> error("waat")
            }

        fun MutableMap<Pair<Int, Int>, MutableSet<Pair<Int ,Int>>>.add(k: Pair<Int, Int>, e: Pair<Int, Int>): Unit {
            if (!this.containsKey(k))
                this[k] = mutableSetOf(e)
            else
                this[k]!!.add(e)
        }

        fun Pair<Int, Int>.move(dir: Dir): Pair<Int, Int> = dir.toPos().let { (dx, dy) -> this.first + dx to (this.second + dy) }

        fun MutableList<MutableList<Char>>.expand() = this.apply {
            val w = this.first().size

            this.forEach { it.add(0, '.'); it.add('.') }

            this.add(0, MutableList(w+2) { '.' } )
            this.add(MutableList(w+2) { '.' } )
        }

        fun MutableList<MutableList<Char>>.spread() {
            val w = this.first().size
            val h = this.size
            
            val candidates = mutableMapOf<Pair<Int, Int>, MutableSet<Pair<Int, Int>>>()

            this.indices.forEach { y ->
                this[y].indices.forEach { x ->
                    if (this[y][x] == '#') {
                        val pos = x to y
                        val adjacents = pos.adjacents()

                        when {
                            adjacents.any { it.second < 0 } ->
                                candidates.add(pos.move(Dir.N), pos)
                            adjacents.any { it.second > h - 1 } ->
                                candidates.add(pos.move(Dir.S), pos)
                            adjacents.any { it.first < 0 } ->
                                candidates.add(pos.move(Dir.W), pos)
                            adjacents.any { it.first > w - 1 } ->
                                candidates.add(pos.move(Dir.E), pos)
                            else -> {

                                val freeDirs = adjacents
                                    .filter { this[it] == '.' }
                                    .map { it - pos }

                                when {
                                    setOf(Dir.N, Dir.NW, Dir.NE).all { it in freeDirs } ->
                                        candidates.add(pos.move(Dir.N), pos)

                                    setOf(Dir.S, Dir.SW, Dir.SE).all { it in freeDirs } ->
                                        candidates.add(pos.move(Dir.S), pos)

                                    setOf(Dir.W, Dir.NW, Dir.SW).all { it in freeDirs } ->
                                        candidates.add(pos.move(Dir.W), pos)

                                    setOf(Dir.E, Dir.NE, Dir.SE).all { it in freeDirs } ->
                                        candidates.add(pos.move(Dir.E), pos)

                                    else -> {
                                        // no move
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (candidates.keys.any { (cx, cy) -> cx !in 0 until w || cy !in 0 until h}) {
                this.expand()
            }
            
            candidates.filter { (_, elves) -> elves.size == 1 }
                .also { if (it.isEmpty()) return }
                .forEach { (pos, elf) ->
                    val (ex, ey) = elf.first()
                    val (px, py) = pos

                    this[ey][ex] = '.'
                    this[py][px] = '#'
                }
        }

fun main() {
    solve( "") {
        var input = lines
            .map  { line -> line.map { it }.toMutableList() }
            .toMutableList()

        part1 {
            input.print()

            repeat(10) { input.spread() }

            println(" ========= ")
            println()
            
            input.print()
            -1
        }

        part2 {

        }
    }
}