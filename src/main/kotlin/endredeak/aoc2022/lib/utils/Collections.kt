@file:Suppress("unused")

package endredeak.aoc2022.lib.utils

fun <T> Iterable<Iterable<T>>.print(separator: String = " ") =
    println(this.joinToString("\n") { it.joinToString(separator) })

fun <T> List<List<T>>.flipVertical() = this.reversed()
fun <T> List<List<T>>.rotateLeft(): List<List<T>> = this.transpose().reversed()
fun <T> List<List<T>>.rotateRight(): List<List<T>> = this.reversed().transpose()
fun <T> List<List<T>>.flipHorizontal(): List<List<T>> = this.map { it.reversed() }

fun <T> List<List<T>>.transpose(): List<List<T>> {
    val ret = mutableListOf<MutableList<T>>()

    this.first().indices.forEach { col ->
        ret.add(this.map { row -> row[col] }.toMutableList())
    }

    return ret
}

fun <T, K> MutableMap<T, K>.insertOrUpdate(key: T, insertValue: K, updateFunction: (K) -> K) {
    this[key]
        ?.let { this[key] = updateFunction.invoke(it) }
        ?: run { this[key] = insertValue }
}

fun <K, V> Iterable<Pair<K, V>>.toMutableMap() = this.toMap().toMutableMap()

fun <T> cartesianProduct(vararg lists: List<T>): List<List<T>> =
    lists
        .fold(listOf(listOf())) { acc, set ->
            acc.flatMap { list -> set.map { element -> list + element } }
        }

fun Collection<Int>.product() = this.fold(1) { acc, i -> acc * i }
fun <T> Collection<T>.productOf(selector: (T) -> Long) = this.fold(1L) { acc, i -> acc * selector(i) }

fun <T> Iterable<T>.splitBy(predicate: (T) -> Boolean): Iterable<Iterable<T>> =
    this.let { input ->
        val outer = mutableListOf(mutableListOf<T>())
        var inner = mutableListOf<T>()
        input.forEach { element ->
            if (predicate(element)) {
                outer.add(inner)
                inner = mutableListOf()
            } else {
                inner.add(element)
            }
        }

        outer
    }
