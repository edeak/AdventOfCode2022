package endredeak.aoc2022.lib.utils

private const val vowels = "aeiou"

fun Char.isVowel() = vowels.toCharArray().contains(this)