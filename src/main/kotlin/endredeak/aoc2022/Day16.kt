package endredeak.aoc2022

import kotlin.math.max

fun main() {
    solve("Proboscidea Volcanium") {
        println("PART 2 takes around a minute!")

        val input = lines
            .map { it.split(" ", "=", "; ", ", ").filter { s -> s.none { c -> c.isLowerCase() } } }
            .map { l -> Triple(l[0], l[1].toInt(), l.drop(2)) }

        val valves = input.associateBy { it.first }

        fun floydWarshall(): MutableMap<String, MutableMap<String, Int>> {
            val res = input
                .associate {
                    it.first to it.third
                        .associateWith { 1 }
                        .toMutableMap()
                }.toMutableMap()

            for (k in res.keys) {
                for (i in res.keys) {
                    for (j in res.keys) {
                        val ik = res[i]?.get(k) ?: 9999
                        val kj = res[k]?.get(j) ?: 9999
                        val ij = res[i]?.get(j) ?: 9999
                        if (ik + kj < ij) res[i]?.set(j, ik + kj)
                    }
                }
            }

            res.values.forEach { paths ->
                paths.keys.filter { valves[it]?.second == 0 }.forEach { paths.remove(it) }
            }

            return res
        }

        val shortestPaths = floydWarshall()

        var score = 0

        fun dfs(maxTime: Int, s: Int, curr: String, seen: Set<String>, t: Int, e: Int = 0) {
            score = max(score, s)
            shortestPaths[curr]!!
                .filter { (v, d) -> v !in seen && t + d + 1 < maxTime }
                .forEach { (v, d) ->
                    val diff = t+d+1
                    dfs(maxTime, s + (maxTime-diff) * valves[v]!!.second, v, seen.plus(v), diff, e)
                }

            repeat(e) { dfs(maxTime, s, "AA", seen, 0, 0) }
        }

        part1(2080) { dfs(30, 0, "AA", emptySet(), 0); score }

        score = 0
        part2(2752) { dfs(26, 0, "AA", emptySet(), 0, 1); score }
    }
}