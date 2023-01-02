import java.lang.IllegalArgumentException
import java.util.*

object DAY07 {

    fun run(fileName: String, sizeLimit: Int): Int {
        val lines = readInput(fileName)
        val root: Directory = buildTree(lines)
        var result = 0
        root.visit {
            val size = it.size()
            when (it) {
                is Directory -> {
                    println("${it.name} (dir) -> $size")
                    if (size <= sizeLimit) result += size
                }

                is FileEntry -> println("${it.name} (file) -> $size")
            }
        }
        println("Result: $result")
        val targetSize = 30000000 - (70000000 - root.size())
        println("Target size: $targetSize")
        var bestDir: Directory = root
        var bestDiff = root.size()
        root.visit {
            when (it) {
                is Directory -> {
                    val size = it.size()
                    val diff = size - targetSize
                    if (size > targetSize && bestDiff > diff) {
                        bestDiff = diff
                        bestDir = it
                    }
                }
            }
        }
        println("Diff: $bestDiff Directory: ${bestDir.name} ${bestDir.size()}")
        return result
    }

    private fun buildTree(lines: List<String>): Directory {
        val root = Directory("/")
        var currentDirectory: Directory = root
        var isListing = false
        for (line in lines) {
            val parts = line.split(' ')
            when {
                isCommand(parts) -> {
                    if (isListing) isListing = false
                    when (val command = parts[1]) {
                        "cd" -> {
                            val parameter = parts[2]
                            currentDirectory = when (parameter) {
                                "/" -> root
                                ".." -> currentDirectory.parent ?: root
                                else -> currentDirectory.getDirectory(parameter) ?: currentDirectory
                            }
                        }

                        "ls" -> isListing = true
                        else -> throw IllegalArgumentException("Unknown command $command")
                    }
                }

                isListing -> {
                    currentDirectory.addChild(
                        when (parts[0]) {
                            "dir" -> Directory(parts[1], currentDirectory)
                            else -> FileEntry(
                                parts[1], Integer.parseInt(parts[0]), currentDirectory
                            )
                        }
                    )
                }

                else -> throw RuntimeException("Bad command state")
            }
        }
        return root
    }

    private fun isCommand(parts: List<String>): Boolean {
        return parts[0] == "$"
    }

}

typealias Visitor = (Node) -> Unit

abstract class Node(val name: String, val parent: Directory?) {
    abstract fun visit(visitor: Visitor)
    abstract fun size(): Int
}

class Directory(name: String, parent: Directory? = null) : Node(name, parent) {
    private val children = mutableListOf<Node>()

    fun addChild(child: Node) {
        if (children.firstOrNull { it.name == child.name } == null) {
            children.add(child)
        } else {
            println("Will not add new child $child since there already exists a child with the same name")
        }
    }

    override fun visit(visitor: Visitor) {
        visitor(this)
        children.forEach {
            it.visit(visitor)
        }
    }

    override fun size(): Int {
        var result = 0
        children.forEach { result += it.size() }
        return result
    }

    fun getDirectory(directoryName: String): Directory? {
        return children.filterIsInstance(Directory::class.java).firstOrNull { it.name == directoryName }
    }
}

class FileEntry(name: String, private val fileSize: Int, parent: Directory) : Node(name, parent) {
    override fun visit(visitor: Visitor) {
        visitor(this)
    }

    override fun size(): Int {
        return this.fileSize
    }
}

fun main() {
//    if (DAY07.run("Day07_test", 100000) != 95437) {
//        throw RuntimeException("Fail: Expected 95437")
//    }
//    if (DAY07.run("Day07", 100000) != 1443806) {
//        throw RuntimeException("Fail: Expected 1443806")
//    }

    DAY07.run("Day07", 100000)

//    70000000
//    30000000
//    21618835
//    8381165
//    70000000
//    40913445
}
