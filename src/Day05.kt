import java.util.*

object DAY05 {

    fun run(fileName: String, crane: Crane): String {
        val lines = readInput(fileName)
        val stacks: MutableMap<Int, MutableList<Char>> = getInitialStacks(lines)
        val operations: List<Operation> = getOperations(lines)
        val finalStacks = crane.build(stacks, operations)
        var result = "";
        finalStacks.values.forEach { print(it[0]) }
        println()
        finalStacks.values.forEach { result += it[0] }
        return result
    }

    private fun getInitialStacks(lines: List<String>): SortedMap<Int, MutableList<Char>> {
        val stacks: MutableMap<Int, MutableList<Char>> = mutableMapOf()
        for (line in lines) {
            if (line.contains("[")) {
                var currentStack = 1
                var currentChar = 1
                while (currentChar < line.length) {
                    val box = line[currentChar]
                    if (box.isLetter()) {
                        val stack: MutableList<Char> = stacks.getOrPut(currentStack) { mutableListOf() }
                        stack.add(box)
                    }
                    currentStack++
                    currentChar += 4
                }
            }
        }
//        stacks.toSortedMap().keys.forEach { println(stacks.get(it)) }
        return stacks.toSortedMap()
    }

    private fun getOperations(lines: List<String>): List<Operation> {
        val operations = mutableListOf<Operation>()
        for (line in lines) {
            if (line.startsWith("move")) {
                val parts = line.split(" ")
                operations.add(
                    Operation(
                        Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[3]),
                        Integer.parseInt(parts[5])
                    )
                )
            }
        }
        return operations
    }

    interface Crane {
        fun build(
            stacks: MutableMap<Int, MutableList<Char>>,
            operations: List<Operation>
        ): MutableMap<Int, MutableList<Char>>
    }

    class Crane9000() : Crane {
        override fun build(
            stacks: MutableMap<Int, MutableList<Char>>,
            operations: List<Operation>
        ): MutableMap<Int, MutableList<Char>> {
            operations.forEach { op ->
                val source = stacks.getOrPut(op.sourceStack) { mutableListOf() }
                val target = stacks.getOrPut(op.targetStack) { mutableListOf() }
                for (i in 1..op.count) {
                    target.add(0, source.removeFirst())
                }
            }
            return stacks
        }
    }

    class Crane9001() : Crane {
        override fun build(
            stacks: MutableMap<Int, MutableList<Char>>,
            operations: List<Operation>
        ): MutableMap<Int, MutableList<Char>> {
            operations.forEach { op ->
                val source = stacks.getOrPut(op.sourceStack) { mutableListOf() }
                val target = stacks.getOrPut(op.targetStack) { mutableListOf() }
                val temp = mutableListOf<Char>()
                for (i in 1..op.count) {
                    temp.add(0, source.removeFirst())
                }
                for (i in 1..op.count) {
                    target.add(0, temp.removeFirst())
                }
            }
            return stacks
        }
    }

    class Operation(val count: Int, val sourceStack: Int, val targetStack: Int)
}

fun main() {
    if (DAY05.run("Day05", DAY05.Crane9000()) != "ZWHVFWQWW") {
        throw RuntimeException("Fail: Expected ZWHVFWQWW")
    }
    if (DAY05.run("Day05", DAY05.Crane9001()) != "HZFZCCWWV") {
        throw RuntimeException("Fail: Expected HZFZCCWWV")
    }
}
