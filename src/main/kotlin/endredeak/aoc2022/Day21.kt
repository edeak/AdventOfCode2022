package endredeak.aoc2022

private abstract class Expression(open val id: String) {
    abstract fun eval(map: Map<String, Expression>): Long

    fun print(map: Map<String, Expression>): String =
        when(this) {
            is Literal -> if (this.id == "humn") "x" else "${this.eval(emptyMap())}"
            is Composite -> "(${map[this.left]!!.print(map)}${this.op}${map[this.right]!!.print(map)})"
            else -> error("waat")
        }
}

private data class Literal(override val id: String, val value: Long) : Expression(id) {
    override fun eval(map: Map<String, Expression>): Long = value
}

private data class Composite(
    override val id: String,
    val left: String,
    val op: String,
    val right: String
) : Expression(id) {
    override fun eval(map: Map<String, Expression>): Long =
        when (op) {
            "+" -> map[left]!!.eval(map) + map[right]!!.eval(map)
            "*" -> map[left]!!.eval(map) * map[right]!!.eval(map)
            "-" -> map[left]!!.eval(map) - map[right]!!.eval(map)
            "/" -> map[left]!!.eval(map) / map[right]!!.eval(map)
            else -> error("waat")
        }
}

fun main() {
    solve("Monkey Math") {
        val input = lines
            .map { it.split(": ", " ") }
            .map { parts ->
                if (parts.size == 2) {
                    Literal(parts[0], parts[1].toLong())
                } else {
                    Composite(parts[0], parts[1], parts[2], parts[3])
                }
            }
            .associateBy { it.id }
            .toMutableMap()

        part1(72664227897438) { input["root"]!!.eval(input) }

        part2(3916491093817) {
            input["z"] = Literal("z", 21973580688943L)
            (input["root"] as Composite).let { r ->
                // throw it into a solver
                input["root"] = Composite("root", r.left, "=", "z")
            }
            println(input["root"]!!.print(input))
            3916491093817
        }
    }
}