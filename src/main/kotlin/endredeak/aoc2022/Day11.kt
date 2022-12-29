package endredeak.aoc2022

fun main() {
    solve("Monkey in the Middle") {
        data class Monkey(
            val items: ArrayDeque<Long>,
            val op: (Long) -> Long,
            val divider: Long,
            val ifTrue: Int,
            val ifFalse: Int,
            var activity: Long = 0
        )

        fun input() = text
            .split("\n\n")
            .associate { monkeyLines ->
                monkeyLines.split("\n")
                    .let { p ->
                        p[0].substringAfter(" ").replace(":", "").toInt() to
                        Monkey(
                            ArrayDeque(p[1].substringAfter(": ").split(", ").map { it.toLong() }),
                            p[2].substringAfter("new = old ")
                            .let {
                                if (it.contains("old")) {
                                    when (it[0]) {
                                        '+' -> { old -> old + old }
                                        '*' -> { old: Long -> old * old }
                                        else -> error("waat")
                                    }
                                } else {
                                    val c = it.substringAfter(" ").toLong()
                                    when (it[0]) {
                                        '+' -> { old -> old + c }
                                        '*' -> { old: Long -> old * c }
                                        else -> error("waat")
                                    }
                                }
                            },
                            p[3].substringAfter("by ").toLong(),
                            p[4].substringAfter("monkey ").toInt(),
                            p[5].substringAfter("monkey ").toInt()
                        )
                    }
            } to text.split("\n")
            .filter { it.contains("divisible by") }
            .map { it.substringAfter("by ") }
            .map { it.toLong() }
            .fold(1L) { f, s -> f * s }

        fun run(rounds: Int, calculateWorry: (Long) -> Long) =
            input()
                .let { (monkeys, d) ->
                    repeat(rounds) { _ ->
                        monkeys.forEach { (_, m) ->
                            while (m.items.isNotEmpty()) {
                                val n = calculateWorry(m.op(m.items.removeFirst()) % d)
                                run {
                                    if (n % m.divider == 0L) monkeys[m.ifTrue] else monkeys[m.ifFalse]
                                }!!.items.add(n)
                                m.activity++
                            }
                        }
                    }

                    monkeys
                        .map { (_, m) -> m.activity }
                        .sortedDescending()
                        .take(2)
                        .let { (f, s) -> f * s }
                }

        part1(64032) { run(20) { it / 3 } }

        part2(12729522272) { run(10000) { it } }
    }
}