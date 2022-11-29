package endredeak.aoc2022.lib.graph

data class Node<T : Comparable<T>>(
    var value: T,
    var edges: MutableSet<Edge<T>> = mutableSetOf(),
    var weight: Int = 1
) : Comparable<Node<T>> {
    fun degree() = edges.size

    fun incomingEdges(): List<Edge<T>> = edges.filter { it.second == this }
    fun outgoingEdges(): List<Edge<T>> = edges.filter { it.first == this && it.second != this }
    fun adjacents(): List<Node<T>> = outgoingEdges().map { it.second }

    override fun compareTo(other: Node<T>) = value.compareTo(other.value)

    override fun toString(): String = "Node(v:$value, w:$weight)"
}