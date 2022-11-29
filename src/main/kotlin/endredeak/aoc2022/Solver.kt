package endredeak.aoc2022

import java.io.File
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

private const val resourcePath = "src/main/resources"

data class Solution<T>(
    val name: String,
) {
    private val dayFormatted by lazy {
        Throwable().stackTrace
            .first { it.className.contains("Day") }
            .fileName!!
            .replace("Day", "")
            .replace(".kt", "")
            .let { "Day$it" }
    }

    private var part1: (List<T>) -> Any = { -1 }
    private var expected1 = -1

    private var part2: (List<T>) -> Any = { -1 }
    private var expected2 = -1

    private var mapper: (List<String>) -> List<T> = { emptyList() }

    fun mapper(block: (List<String>) -> List<T>) { mapper = block }

    fun part1(expected: Int = -1, block: (List<T>) -> Any) { expected1 = expected; part1 = block }
    fun part2(expected: Int = -1, block: (List<T>) -> Any) { expected2 = expected; part2 = block }

    fun execute() {
        File("$resourcePath/${dayFormatted}.txt")
            .readLines()
            .let { mapper(it) }
            .let { input ->
                measure(1, part1, input)
                measure(2, part2, input)
            }
    }

    @OptIn(ExperimentalTime::class)
    private fun measure(part: Int, block: (List<T>) -> Any, input: List<T>) {
        measureTimedValue { block(input) }.let { (answer, time) ->
            println("2022 $dayFormatted: $name -- part $part -- (in [${time.inWholeMilliseconds} ms]): $answer")
        }
    }
}

@JvmName("solveGeneric")
inline fun <reified T> solve(name: String, init: Solution<T>.() -> Unit) =
    Solution<T>(name).let { solution ->
        solution.init()
        solution.execute()
    }

fun solve(name: String, init: Solution<String>.() -> Unit) =
    solve<String>(name) {
        mapper { it }

        init(this)
    }