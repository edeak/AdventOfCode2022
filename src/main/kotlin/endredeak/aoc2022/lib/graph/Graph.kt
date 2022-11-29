package endredeak.aoc2022.lib.graph

import java.util.PriorityQueue

data class Graph<T : Comparable<T>>(val edges: MutableList<Edge<T>>) {
    private val nodes = mutableMapOf<T, Node<T>>()

    init {
        edges
            .forEach { edge ->
                listOf(edge.first, edge.second).map { node ->
                    if (nodes[node.value] == null) {
                        nodes[node.value] = node
                    }

                    getNode(node.value).apply { this.edges.add(edge) }
                }

                edge.first = getNode(edge.first.value)
                edge.second = getNode(edge.second.value)
            }
    }

    fun removeNodeByValue(value: T) = nodes.remove(value).also {
        edges.removeIf { e -> e.first.value == value || e.second.value == value }
    }

    fun getNode(value: T) = nodes[value] ?: error("no point in graph with value [$value]")

    fun nonDirected() = Graph(edges.map { listOf(it, it.reverse()) }.flatten().toMutableList())

    fun reverse() = Graph(this.edges.map { it.reverse() }.toMutableList())

    fun dijkstra(source: T): Map<Node<T>, Int> {
        val dist = mutableMapOf<Node<T>, Int>()
        val prev = mutableMapOf<Node<T>, Node<T>?>()
        dist[getNode(source)] = 0

        val q = PriorityQueue<Pair<Node<T>, Int>>(PairComparator())

        this.nodes.values
            .toSet()
            .forEach { v ->
                if (v != this[source]) {
                    dist[v] = Int.MAX_VALUE
                    prev[v] = null
                }

                q.add(Pair(v, dist[v]!!))
            }

        while (q.isNotEmpty()) {
            val u = q.poll()

            u.first.adjacents()
                .forEach { v ->
                    val alt = dist[u.first]!! + v.weight

                    if (alt < dist[v]!!) {
                        dist[v] = alt
                        prev[v] = u.first

                        q.removeIf { it.first == v }
                        q.add(Pair(v, alt))
                    }
                }
        }

        return dist/*.map { (k, v) -> k to v - 2 }*/.toMap()
    }

    @Suppress("unused")
    fun dfs(source: T): Set<Node<T>> {
        val discovered = mutableSetOf<Node<T>>()
        val s = ArrayDeque<Node<T>>()

        s.addFirst(getNode(source))
        while (s.isNotEmpty()) {
            val v = s.removeLast()
            if (!discovered.contains(v)) {
                discovered.add(v)
                edges
                    .filter { it.first == v }
                    .map { it.second }
                    .forEach {
                        s.addFirst(it)
                    }
            }
        }

        return discovered
    }

    fun bfs(source: T): Set<Pair<Node<T>, Int>> {
        val discovered = mutableSetOf<Pair<Node<T>, Int>>()
        val q = ArrayDeque<Pair<Node<T>, Int>>()
        val initial = Pair(getNode(source), 0)

        discovered.add(initial)
        q.add(initial)

        while (q.isNotEmpty()) {
            val v = q.removeFirst()

            edges
                .filter { it.first == v.first }
                .map { it.second }
                .forEach {
                    if (discovered.none { d -> d.first == it }) {
                        val current = Pair(it, v.second + 1)
                        discovered.add(current)
                        q.add(current)
                    }
                }
        }

        return discovered
    }

    fun allPaths(
        current: T,
        start: T,
        end: T,
        visited: List<T>,
        condition: (T, T, List<T>) -> Boolean = { curr, n, visitedList -> curr !in visitedList + curr }
    ): List<List<T>> =
        when (current) {
            end -> listOf(visited + end)
            else ->
                this[current].adjacents()
                    .filter { it.value != start }
                    .filter { condition(it.value, current, visited) }
                    .flatMap { allPaths(it.value, start, end, visited + current, condition) }

        }

    operator fun get(value: T): Node<T> = this.nodes[value] ?: error("no node with value $value")

    internal class PairComparator<T : Comparable<T>> : Comparator<Pair<Node<T>, Int>> {
        override fun compare(o1: Pair<Node<T>, Int>, o2: Pair<Node<T>, Int>) = o1.second.compareTo(o2.second)
    }

    override fun toString() = edges.joinToString("\n") { it.toString() }
}
