package endredeak.aoc2022.lib.utils

import kotlin.math.max
import kotlin.math.min

fun gcd(x: Long, y: Long): Long = if (x == 0L || y == 0L) x + y else gcd(min(x, y), max(x, y) % min(x, y))

fun lcm(vararg values: Long): Long = values.fold(values[0]) { acc, curr -> acc * curr / gcd(acc, curr) }

fun List<Int>.isTriangle() = this.size == 3 && this.all { it < this.minus(it).sum() }