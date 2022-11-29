package endredeak.aoc2022.lib

import endredeak.aoc2022.lib.utils.cartesianProduct
import endredeak.aoc2022.lib.utils.product


data class Coordinate2d(
    val x: Int,
    val y: Int) : Comparable<Coordinate2d> {
    companion object {
        val ORIGO = Coordinate2d(0, 0)
    }

    override fun hashCode(): Int {
        return 37 * this.x + this.y
    }
    
    override fun equals(other: Any?): Boolean = other is Coordinate2d && this.x == other.x && this.y == other.y

    override fun toString(): String {
        return "C($x, $y)"
    }

    operator fun plus(other: Coordinate2d): Coordinate2d = Coordinate2d(this.x + other.x, this.y + other.y)

    fun neighbours(includeDiagonal: Boolean = false, includeSelf: Boolean = false) =
        if (includeDiagonal) {
            Directions.ALL_DIRECTIONS
        } else {
            Directions.BASIC_DIRECTIONS
        }.map { d -> this + d }.let { if (includeSelf) it.plus(this) else it }

    override fun compareTo(other: Coordinate2d): Int {
        if (this.y == other.x) {
            return this.x.compareTo(other.x)
        }

        return this.y.compareTo(other.y)
    }

    fun inRange(min: Coordinate2d, max: Coordinate2d) = this.x in min.x .. max.x && this.y in min.y .. max.y
}

object Directions {
    val UP = Coordinate2d(0, 1)
    val RIGHT = Coordinate2d(1, 0)
    val DOWN = Coordinate2d(0, -1)
    val LEFT = Coordinate2d(-1, 0)

    val UP_RIGHT = Coordinate2d(1, 1)
    val DOWN_RIGHT = Coordinate2d(1, -1)
    val UP_LEFT = Coordinate2d(-1, 1)
    val DOWN_LEFT = Coordinate2d(-1, -1)

    val BASIC_DIRECTIONS = listOf(UP, DOWN, RIGHT, LEFT)
    val DIAGONAL_DIRECTIONS = listOf(UP_RIGHT, DOWN_RIGHT, UP_LEFT, DOWN_LEFT)

    val ALL_DIRECTIONS = BASIC_DIRECTIONS.plus(DIAGONAL_DIRECTIONS)

    val ORIGO = Coordinate(0, 0)
}

open class Coordinate(
    vararg coords: Int
) : Comparable<Coordinate> {
    private val coordinates: MutableList<Int>

    constructor(coords: Pair<Int, Int>) : this(coords.first, coords.second)
    constructor(coords: Triple<Int, Int, Int>) : this(coords.first, coords.second, coords.third)
    constructor(vararg coords: String) : this(*coords.map { it.toInt() }.toIntArray())

    init {
        coordinates = coords.toMutableList()
    }

    var x: Int
        get() = this.coordinates[0]
        set(value) {
            this.coordinates[0] = value
        }

    var y: Int
        get() = this.coordinates[1]
        set(value) {
            this.coordinates[1] = value
        }

    var z: Int
        get() = this.coordinates[2]
        set(value) {
            if (this.coordinates.size < 3) {
                this.coordinates.add(value)
            } else {
                this.coordinates[2] = value
            }
        }

    var w: Int
        get() = this.coordinates[3]
        set(value) {
            this.coordinates[3] = value
        }

    private var _dimension: Int? = null

    var dimension: Int
        get() = _dimension ?: this.coordinates.size
        set(value) {
            this._dimension = value
        }

    fun product(dimensions: Int? = null) = coordinates.takeOpt(dimensions).product()

    fun inc(dimension: Int, amount: Int) {
        coordinates[dimension] += amount
    }

    fun dec(dimension: Int, amount: Int) {
        coordinates[dimension] -= amount
    }

    fun inRange(from: Coordinate, to: Coordinate): Boolean {
        checkDimensions(from)
        checkDimensions(to)

        return this.coordinates.indices.all {
            coordinates[it] in from.coordinates[it]..to.coordinates[it]
        }
    }

    operator fun plus(other: Coordinate): Coordinate {
        return this.coordinates
            .takeOpt(_dimension)
            .mapIndexed { index, i -> i + (other.coordinates.getOrNull(index) ?: 0) }
            .asCoordinate()
    }

    operator fun minus(other: Coordinate): Coordinate {
        checkDimensions(other)

        return this.coordinates
            .takeOpt(_dimension)
            .mapIndexed { index, i -> i - (other.coordinates.getOrNull(index) ?: 0) }
            .asCoordinate()
    }

    operator fun times(p: Int) = Coordinate(*coordinates.map { it * p }.toIntArray())

    override fun toString(): String {
        return "C_$dimension(${coordinates})"
    }

    override fun hashCode(): Int {
        return coordinates.takeOpt(_dimension).hashCode()
    }

    override fun equals(other: Any?): Boolean =
        (other as? Coordinate)
            ?.let { o ->
                this.coordinates.takeOpt(_dimension) == o.coordinates.takeOpt(_dimension)
            }
            ?: false

    override fun compareTo(other: Coordinate): Int {
        this.coordinates.takeOpt(_dimension).mapIndexed { index, i ->
            other.coordinates.takeOpt(_dimension)[index].let { j ->
                if (i != j) {
                    return i - j
                }
            }
        }

        return 0
    }

    private fun List<Int>.takeOpt(n: Int?) = n?.let { this.take(it) } ?: this
    private fun List<Int>.asCoordinate() = Coordinate(*this.toIntArray())

    private fun checkDimensions(other: Coordinate) {
        if (this.dimension != other.dimension) {
            error("coordinates have different dimensions: this(${this.dimension}) vs other(${other.dimension})")
        }
    }

    fun neighbours(dimension: Int? = null): MutableSet<Coordinate> =
        this
            .coordinates
            .takeOpt(dimension)
            .map { (it - 1..it + 1).toList() }
            .let { cartesianProduct(*it.toTypedArray()) }
            .map { it.asCoordinate() }
            .filter { it != this }
            .toMutableSet()

    object D_2 {
        object Directions {
            val UP = Coordinate(0, 1)
            val RIGHT = Coordinate(1, 0)
            val DOWN = Coordinate(0, -1)
            val LEFT = Coordinate(-1, 0)

            val UP_RIGHT = Coordinate(1, 1)
            val DOWN_RIGHT = Coordinate(1, -1)
            val UP_LEFT = Coordinate(-1, 1)
            val DOWN_LEFT = Coordinate(-1, -1)

            val BASIC_DIRECTIONS = listOf(UP, DOWN, RIGHT, LEFT)
            val DIAGONAL_DIRECTIONS = listOf(UP_RIGHT, DOWN_RIGHT, UP_LEFT, DOWN_LEFT)

            val ALL_DIRECTIONS = BASIC_DIRECTIONS.plus(DIAGONAL_DIRECTIONS)

            val ORIGO = Coordinate(0, 0)
        }

        fun baseNeighbours(c: Coordinate) = Directions.BASIC_DIRECTIONS.map { c + it }
        fun neighbours(c: Coordinate) = Directions.ALL_DIRECTIONS.map { c + it }

        // takes horizontal, vertical and diagonal ranges
        fun range(start: Coordinate, end: Coordinate): List<Coordinate> {
            fun Pair<Int, Int>.withDir() = Triple(this.first, this.second, if (this.first > this.second) -1 else 1)

            val ret = mutableListOf<Coordinate>()

            val (sx, ex, xDir) = (start.x to end.x).withDir()
            val (sy, ey, yDir) = (start.y to end.y).withDir()

            var x = sx
            var y = sy

            ret.add(Coordinate(x, y))

            while (true) {
                if (x != ex) {
                    x += xDir
                }

                if (y != ey) {
                    y += yDir
                }

                ret.add(Coordinate(x, y))

                if (x == ex && y == ey) {
                    break
                }
            }

            return ret
        }
    }
}