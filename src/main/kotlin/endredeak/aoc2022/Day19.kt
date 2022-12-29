package endredeak.aoc2022

fun main() {
    val pattern = Regex("Blueprint (\\d+): Each ore robot costs (\\d+) ore. " +
            "Each clay robot costs (\\d+) ore. " +
            "Each obsidian robot costs (\\d+) ore and (\\d+) clay. " +
            "Each geode robot costs (\\d+) ore and (\\d+) obsidian.")

    solve( "Not Enough Minerals") {
        data class Blueprint(
            val id: Int,
            val oreCost: Int,
            val clayCost: Int,
            val obsidianCost: Pair<Int, Int>,
            val geodeCost: Pair<Int, Int>) {
            private val robots = listOf("ore", "clay", "obsidian", "geode")
                .mapIndexed { i, r -> i to r }
                .associate { it.second to if (it.first == 0) 1 else 0 }
                .toMutableMap()

            private val tokens = listOf("ore", "clay", "obsidian", "geode").associateWith { 0 }.toMutableMap()

            fun shouldProduceClay() = tokens["ore"]!! >= clayCost && robots["clay"]!! < 3
            fun shouldProduceObsidian() = tokens["ore"]!! >= obsidianCost.first && tokens["clay"]!! >= obsidianCost.second
            fun shouldProduceGeode() = tokens["clay"]!! >= geodeCost.first && tokens["obsidian"]!! >= geodeCost.second

            fun produceGeode(): String {
                tokens["ore"] = tokens["ore"]!! - geodeCost.first
                tokens["obsidian"] = tokens["obsidian"]!! - geodeCost.second
                return "geode"
//                robots["geode"] = robots["geode"]!! + 1
            }

            fun produceObsidian(): String {
                tokens["ore"] = tokens["ore"]!! - obsidianCost.first
                tokens["clay"] = tokens["clay"]!! - obsidianCost.second
//                robots["obsidian"] = robots["obsidian"]!! + 1
                return "obsidian"
            }

            fun produceClay(): String {
                tokens["ore"] = tokens["ore"]!! - clayCost
//                robots["clay"] = robots["clay"]!! + 1
                return "clay"
            }

            fun simulate(rounds: Int): Int {
                var i = 1

                while(i <= rounds) {
                    val r = if (shouldProduceGeode()) {
                        produceGeode()
                    }
                    else if (shouldProduceObsidian()) {
                        produceObsidian()
                    }
                    else if (shouldProduceClay()) {
                        produceClay()
                    } else {
                        ""
                    }

                    robots.forEach { (k, v) ->
                        tokens[k] = tokens[k]!! + v
                    }

                    if (robots.containsKey(r)) {
                        robots[r] = robots[r]!! + 1
                    }
                    i++
                }

                return tokens["geode"]!!
            }
        }

        val input = lines
            .map { l -> pattern.find(l)!!.destructured.toList().map { it.toInt()} }
            .map { l -> Blueprint(l[0], l[1], l[2], l[3] to l[4], l[5] to l[6]) }
            .onEach { println(it) }


        part1 {
            input.map { it.simulate(24) }
                .onEach { println(it) }
            -1
        }

        part2 {

        }
    }
}