package endredeak.aoc2022

fun main() {
    solve("No Space Left On Device") {
        data class Dir(
            val name: String,
            var size: Long = 0,
            val parent: Dir? = null,
            val children: MutableList<Dir> = mutableListOf()
        ) {
            operator fun get(name: String) = this.children.first { it.name == name }

            fun addSize(size: Long) {
                var curr: Dir? = this
                while (curr != null) {
                    curr.size += size
                    curr = curr.parent
                }
            }
        }


        val input = run {
            val root = Dir("/")
            var curr = root

            lines
                .drop(1)
                .forEach { line ->
                    when {
                        line.contains("cd ..") -> curr = curr.parent ?: error("waat")
                        line.contains("cd ") -> curr = curr[line.drop(5)]
                        line.startsWith("dir ") -> curr.children.add(Dir(line.drop(4), parent = curr))
                        line.first().isDigit() -> line.split(" ").first().let { curr.addSize(it.toLong()) }
                        else -> {}
                    }
                }

            val dirs = mutableListOf(Dir("/", root.size))
            fun flatten(dir: Dir) {
                dir.children
                    .forEach {
                        dirs.add(Dir(it.name, it.size))
                        flatten(it)
                    }
            }
            flatten(root)

            dirs
        }

        part1(1770595) { input.filter { it.size <= 100000 }.sumOf { it.size } }

        part2(2195372) { input.map { it.size }.sorted().first { it >= input.first().size - 40_000_000 } }
    }
}