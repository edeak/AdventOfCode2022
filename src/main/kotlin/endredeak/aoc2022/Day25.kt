package endredeak.aoc2022


fun main() {
    solve( "Full of Hot Air") {
        fun Long.pow(i: Int): Long {
            var ret = 1L

            repeat(i) { ret *= this }

            return ret
        }

        fun String.toSnofuDigit() =
            this.reversed().mapIndexed { i, c ->
                when(c) {
                    '=' -> -2
                    '-' -> -1
                    else -> c.digitToInt().toLong()
                } * (5L.pow(i))
            }.sum()

        val DIGITS = "=-012"

        fun Long.toSnofu() = run {
            if (this == 0L) "0"
            else {
                var b = this
                buildString {
                    while (b > 0) {
                        val m = (b + 2) % 5
                        b = (b + 2) / 5
                        append(DIGITS[m.toInt()])
                    }
                }.reversed()
            }
        }

        val input = lines

        part1("2-=2==00-0==2=022=10") {
            input
                .map { it to it.toSnofuDigit() }
                .sumOf { it.second }
                .toSnofu()
        }

        part2 {

        }
    }
}
