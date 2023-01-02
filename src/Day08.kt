import java.lang.IllegalArgumentException
import java.util.*

object DAY08 {

    fun run(fileName: String): Int {
        val lines = readInput(fileName)
        val forest: List<List<Int>> = createForest(lines)
        val matrix: List<List<Int>> = createVisibilityMatrix(forest)
        return calculateVisableTrees(matrix)
    }

    fun run2(fileName: String): Int {
        val lines = readInput(fileName)
        val forest: List<List<Int>> = createForest(lines)
        val matrix: List<List<Int>> = createScenicScoreMatrix(forest)
        return getHeighestScenicScore(matrix)
    }

    private fun getHeighestScenicScore(matrix: List<List<Int>>): Int {
        var maxScore = 0
        matrix.forEach { row -> row.forEach { if (it > maxScore) maxScore = it } }
        return maxScore
    }

    private fun createScenicScoreMatrix(forest: List<List<Int>>): List<List<Int>> {
        val matrix: MutableList<MutableList<Int>> = mutableListOf()
        val columns = forest[0].size
        forest.forEach { _ -> matrix.add(ArrayList(Collections.nCopies(columns, 0))) }

        for (rowIndex in forest.indices) {
            val row = forest[rowIndex]
            for (colIndex in row.indices) {
                val treeHeight = forest[rowIndex][colIndex]
                var up = 0
                var down = 0
                var left = 0
                var right = 0

                // Up
                for (treeRow in rowIndex - 1 downTo 0) {
                    val height = forest[treeRow][colIndex]
                    up++
                    if (treeHeight <= height) break
                }

                // Down
                for (treeRow in rowIndex + 1 until forest.size) {
                    val height = forest[treeRow][colIndex]
                    down++
                    if (treeHeight <= height) break
                }

                // Left
                for (treeCol in colIndex - 1 downTo 0) {
                    val height = forest[rowIndex][treeCol]
                    left++
                    if (treeHeight <= height) break
                }

                // Right
                for (treeCol in colIndex + 1 until columns) {
                    val height = forest[rowIndex][treeCol]
                    right++
                    if (treeHeight <= height) break
                }

                matrix[rowIndex][colIndex] = up * down * left * right
            }
        }

        return matrix
    }

    private fun calculateVisableTrees(matrix: List<List<Int>>): Int {
        var result = 0
        matrix.forEach { row -> row.forEach { result += it } }
        return result
    }

    private fun createVisibilityMatrix(forest: List<List<Int>>): List<List<Int>> {
        val matrix: MutableList<MutableList<Int>> = mutableListOf()
        val columns = forest[0].size
        forest.forEach { _ -> matrix.add(ArrayList(Collections.nCopies(columns, 0))) }

        for (rowIndex in forest.indices) {
            matrix[rowIndex][0] = 1
            matrix[rowIndex][columns - 1] = 1
        }

        for (colIndex in 0 until columns) {
            matrix[0][colIndex] = 1
            matrix[forest.size - 1][colIndex] = 1
        }

        for (rowIndex in forest.indices) {
            var height = 0
            val row = forest[rowIndex]
            for (colIndex in row.indices) {
                val treeHeight = forest[rowIndex][colIndex]
                if (treeHeight > height) {
                    matrix[rowIndex][colIndex] = 1
                    height = treeHeight
                }
            }
        }

        for (rowIndex in forest.indices) {
            var height = 0
            val row = forest[rowIndex]
            for (colIndex in row.lastIndex downTo 0) {
                val treeHeight = forest[rowIndex][colIndex]
                if (treeHeight > height) {
                    matrix[rowIndex][colIndex] = 1
                    height = treeHeight
                }
            }
        }

        for (colIndex in 0 until columns) {
            var height = 0
            for (rowIndex in forest.indices) {
                val treeHeight = forest[rowIndex][colIndex]
                if (treeHeight > height) {
                    matrix[rowIndex][colIndex] = 1
                    height = treeHeight
                }
            }
        }

        for (colIndex in 0 until columns) {
            var height = 0
            for (rowIndex in forest.lastIndex downTo 0) {
                val treeHeight = forest[rowIndex][colIndex]
                if (treeHeight > height) {
                    matrix[rowIndex][colIndex] = 1
                    height = treeHeight
                }
            }
        }

        return matrix
    }

    private fun createForest(lines: List<String>): List<List<Int>> {
        val forest: MutableList<MutableList<Int>> = mutableListOf()
        for ((row, line) in lines.withIndex()) {
            forest.add(mutableListOf())
            for ((col, char) in line.withIndex()) {
                forest[row].add(Integer.parseInt(char.toString()))
            }
        }
        return forest
    }

}

fun main() {
    if (DAY08.run("Day08_test") != 21) {
        throw RuntimeException("Fail: Expected 21")
    }
    if (DAY08.run("Day08") != 1763) {
        throw RuntimeException("Fail: Expected 1763")
    }
    if (DAY08.run2("Day08_test") != 8) {
        throw RuntimeException("Fail: Expected 8")
    }
    if (DAY08.run2("Day08") != 671160) {
        throw RuntimeException("Fail: Expected 671160")
    }
}