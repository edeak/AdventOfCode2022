package endredeak.aoc2022.lib

import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Map starts from (0, 0) as a top left coordinate
 */
open class Point2d(var x: Int,
                   var y: Int,
                   var weight: Int,
                   var visited: Int,
                   var value: String = "") {
    constructor(x: Int, y: Int) : this(x, y, 0, 0)
    constructor(x: Int, y: Int, v: String) : this(x, y, 0, 0, v)

    operator fun component1() = x
    operator fun component2() = y

    fun manhattan(other: Point2d) = abs(x) + abs(other.x) + abs(y) + abs(other.y)

    fun distance(other: Point2d) = sqrt((x - other.x).toDouble().pow(2.0) + ((y - other.y).toDouble()).pow(2.0))

    fun angle(target: Point2d) = atan2((target.x - x).toDouble(), (target.y - y).toDouble())

    fun move(p: Point2d) = Point2d(x + p.x, y + p.y, weight, visited, value)

    operator fun times(p: Int) = Point2d(p*x, p*y)

    fun rotateRight() = Point2d(y, -x)

    fun rotateLeft() = Point2d(-y, x)

    fun inRange(from: Point2d, to: Point2d) = this.coordinate().inRange(from.coordinate(), to.coordinate())

    fun coordinatesEqual(other: Point2d) = x == other.x && y == other.y

    override fun hashCode(): Int {
        return 97 * (31 * x + 73 * y)
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }

        val o = other as Point2d

        return coordinatesEqual(o)
    }

    override fun toString(): String {
        return "Point($x, $y, $value)"
    }

    fun coordinate() = Coordinate(this.x, this.y)

    companion object {
        val UP = Point2d(0, 1)
        val RIGHT = Point2d(1, 0)
        val DOWN = Point2d(0, -1)
        val LEFT = Point2d(-1, 0)

        private val UP_RIGHT = Point2d(1,1)
        private val DOWN_RIGHT = Point2d(1, -1)
        private val UP_LEFT = Point2d(-1, 1)
        private val DOWN_LEFT = Point2d(-1, -1)

        val BASIC_DIRECTIONS = listOf(UP, DOWN, RIGHT, LEFT)
        val DIAGONAL_DIRECTIONS = listOf(UP_RIGHT, DOWN_RIGHT, UP_LEFT, DOWN_LEFT)

        val ALL_DIRECTIONS = BASIC_DIRECTIONS.plus(DIAGONAL_DIRECTIONS)

        val ORIGO = Point2d(0, 0)
    }
}