package endredeak.aoc2022.lib.utils

fun ByteArray.hexString() =
    "0123456789ABCDEF".toCharArray().let { hexCode ->
        this.fold("") { s, b ->
            "$s${(hexCode[b.toInt() shr 4 and 0xF])}${(hexCode[b.toInt() and 0xF])}"
        }
    }
