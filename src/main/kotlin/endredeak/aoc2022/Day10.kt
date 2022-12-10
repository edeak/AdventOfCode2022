package endredeak.aoc2022

fun main() {
    solve("Cathode-Ray Tube") {
        val input = lines.map { l ->
            if (l[0] == 'n') 1 to 0 else {
                2 to l.substringAfter(" ").toInt()
            }
        }

        var x = 1
        var c = 0

        fun run(func: () -> Unit) {
            input.forEach { (w, v) ->
                repeat(w) { _ -> c++; func() }
                x += v
            }
        }

        part1(13520) {
            var s = 0
            run {
                if (c % 40 == 20) {
                    s += c * x
                }
            }
            s
        }

        part2("PGPHBEAB") {
            x = 1
            c = 0
            run {
                val char = if ((c % 40) - 1 in (x - 1..x + 1)) "#" else "."
                if (c % 40 == 0) println(char) else print(char)
            }
        }
    }
}