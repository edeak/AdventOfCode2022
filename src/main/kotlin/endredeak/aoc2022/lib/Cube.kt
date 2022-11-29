package endredeak.aoc2022.lib

data class Cube(val l: Int, val w: Int, val h: Int) {
    val sortedDimensions = mutableListOf(l, w, h).sorted()

    fun cubicVolume() = w * l * h

    fun surfaceSize() = 2 * (w * l + l * h + h * w)
}