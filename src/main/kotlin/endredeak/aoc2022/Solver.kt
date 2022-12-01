package endredeak.aoc2022

import java.io.File
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

private const val RESOURCE_PATH = "src/main/resources"

data class Solution(val name: String) {
    private val dayFormatted by lazy {
        Throwable().stackTrace
            .first { it.className.contains("Day") }
            .fileName!!
            .replace("Day", "")
            .replace(".kt", "")
            .let { "Day$it" }
    }

    private var part1: () -> Any = { -1 }
    private var expected1: Any = -1

    private var part2: () -> Any = { -1 }
    private var expected2: Any = -1

    fun part1(expected: Any = -1, block: () -> Any) {
        expected1 = expected; part1 = block
    }

    fun part2(expected: Any = -1, block: () -> Any) {
        expected2 = expected; part2 = block
    }

    val lines = File("$RESOURCE_PATH/${dayFormatted}.txt")
        .readLines()

    fun execute() {
        runPart(1, part1)
        runPart(2, part2)
    }

    @OptIn(ExperimentalTime::class)
    private fun runPart(part: Int, block: () -> Any) {
        measureTimedValue { block() }.let { (answer, time) ->
            println("2022 $dayFormatted: $name -- part $part -- " +
                    "(in [${time.inWholeMicroseconds} microsecs]):" +
                    " ${if (answer is Unit) "-1" else answer}")
        }
    }
}

fun solve(name: String, init: Solution.() -> Unit) =
    Solution(name).let { solution ->
        solution.init()
        solution.execute()
    }