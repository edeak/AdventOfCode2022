package endredeak.aoc2022.lib.utils

import kotlin.math.abs

fun String.findRepeatingPatterns(length: Int, offset: Int, exactOffsetMatch: Boolean) =
    (0..this.lastIndex - length + 1)
        .groupBy { this.substring(it, it + length) }
        .filter { it.value.size > 1 }
        .map { it.value }
        .map {
            (0..it.lastIndex).map { a ->
                (0..it.lastIndex).map { b ->
                    abs(it[a] - it[b])
                }
            }
        }
        .flatMap { it.flatten() }
        .filter { it >= length }
        .any { if (exactOffsetMatch) it == offset else it >= offset }

fun String.hasDifferentVowels(n: Int) = this.toCharArray().count { it.isVowel() } >= n

fun String.hasNoBannedStrings(bannedStrings: List<String>) = bannedStrings.none { this.contains(it) }

fun String.binaryComplement() = this.map { if (it == '0') '1' else '0' }.joinToString("")