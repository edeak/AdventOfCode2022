package endredeak.aoc2022.lib.graph

data class Edge<T : Comparable<T>>(
    var first : Node<T>,
    var second : Node<T>,
    var weight: Int = 1) {
    fun reverse() = Edge(second, first, weight)

    override fun equals(other: Any?) = other is Edge<*> && other.first == this.first && other.second == this.second
    override fun hashCode() = 31 * this.first.value.hashCode() + 79 * this.second.value.hashCode()
}
