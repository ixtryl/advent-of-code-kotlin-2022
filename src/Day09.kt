import java.util.*
import kotlin.math.abs

fun main() {
//    if (DAY09().run("Day09_test", 2) != 13) {
//        throw RuntimeException("Fail: Expected 13")
//    }
//    if (DAY09().run("Day09", 2) != 6081) {
//        throw RuntimeException("Fail: Expected 6081")
//    }
    if (DAY09().run("Day09_test2", 10) != 36) {
        throw RuntimeException("Fail: Expected 1")
    }
//    if (DAY09().run("Day09", 10) != 278) {
//        throw RuntimeException("Fail: Expected 278")
//    }
}

private const val START_ROW = 0
private const val START_COL = 0

private class DAY09 {

    fun run(fileName: String, knotsCount: Int): Int {
        val knots = initializeKnots(knotsCount)
        val lines = readInput(fileName)
        val moves = getMoves(lines)
        executeMoves(knots, moves)
        val tail = knots[knotsCount - 1]
        println(tail.visited)
        dumpTrace(tail)
        println()
        println(tail.visited.size)
        return tail.visited.size
    }

    private fun dumpTrace(knot: Knot) {
        var maxRow = 0
        var minRow = 0
        var maxCol = 0
        var minCol = 0
        knot.visited.forEach {
            if (it.row > maxRow) maxRow = it.row
            if (it.row < minRow) minRow = it.row
            if (it.col > maxCol) maxCol = it.col
            if (it.col < minCol) minCol = it.col
        }
        var offsetRow = 0
        var offsetCol = 0
        if (minRow < 0) offsetRow = 0 - minRow
        if (minCol < 0) offsetCol = 0 - minCol
        val rangeRow = offsetRow + maxRow + 1
        val rangeCol = offsetCol + maxCol + 1
        val matrix: MutableList<MutableList<Char>> = mutableListOf()
        for (i in 0 until rangeRow) {
            matrix.add(ArrayList(Collections.nCopies(rangeCol, '.')))
        }

        knot.visited.forEach { matrix[it.row + offsetRow][it.col + offsetCol] = '#' }
        matrix[0 + offsetRow][0 + offsetCol] = 's'
        println()
        matrix.reverse()
        matrix.forEach { row ->
            println()
            row.forEach { col -> print(col) }
        }
        println()
        println()
    }

    private fun initializeKnots(knotsCount: Int): List<Knot> {
        val knots = mutableListOf<Knot>()
        for (i in 1..knotsCount) {
            knots.add(Knot(Position(START_ROW, START_COL)))
        }
        return knots
    }

    private fun executeMoves(knots: List<Knot>, moves: List<Move>) {
        val head = knots[0]
        for ((direction, steps) in moves) {
            for (i in 1..steps) {
                head.move(direction)
                for (j in 1 until knots.size) {
                    if (knots[j].inProximityOf(knots[j - 1]).not()) {
                        knots[j].moveTowards(knots[j - 1])
                    }
                }
            }
            dumpTrace(knots.last())
        }
    }

    private fun getMoves(lines: List<String>): List<Move> {
        val moves = mutableListOf<Move>()
        for (line in lines) {
            val parts = line.split(" ")
            moves.add(Move(parts[0].toDirection(), Integer.parseInt(parts[1])))
        }
        return moves
    }

    private enum class DIRECTION(val string: String) {
        UP("U"), DOWN("D"), LEFT("L"), RIGHT("R");

        companion object {
            fun fromString(value: String): DIRECTION {
                return values().first { it.string == value }
            }
        }
    }

    private fun String.toDirection(): DIRECTION {
        return DIRECTION.fromString(this)
    }

    private data class Move(val direction: DIRECTION, val steps: Int)

    private class Knot(startPosition: Position) {
        val visited = mutableSetOf<Position>()
        var pos: Position = startPosition
            set(value) {
                visited.add(value)
                field = value
            }
        val row get() = pos.row
        val col get() = pos.col

        init {
            this.visited.add(startPosition)
        }

        fun move(direction: DIRECTION) {
            pos = when (direction) {
                DIRECTION.UP -> Position(row + 1, col)
                DIRECTION.DOWN -> Position(row - 1, col)
                DIRECTION.LEFT -> Position(row, col - 1)
                DIRECTION.RIGHT -> Position(row, col + 1)
            }
        }

        fun inProximityOf(other: Knot): Boolean = pos.inProximityOf(other.pos)

        override fun toString(): String = "(${pos.row}, ${pos.col})"

        fun moveTowards(other: Knot) {
            var rows = 0
            var cols = 0

            if (row + 1 < other.row && abs(col - other.col) < 2) {
                rows += 1
                if (col < other.col) {
                    cols += 1
                }
                if (col > other.col) {
                    cols -= 1
                }
            }
            if (row - 1 > other.row && abs(col - other.col) < 2) {
                rows -= 1
                if (col < other.col) {
                    cols += 1
                }
                if (col > other.col) {
                    cols -= 1
                }
            }
            if (col + 1 < other.col && abs(row - other.row) < 2) {
                cols += 1
                if (row < other.row) {
                    rows += 1
                }
                if (row > other.row) {
                    rows -= 1
                }
            }
            if (col - 1 > other.col && abs(row - other.row) < 2) {
                cols -= 1
                if (row < other.row) {
                    rows += 1
                }
                if (row > other.row) {
                    rows -= 1
                }
            }

            pos = Position(pos.row + rows, pos.col + cols)
        }
    }

    private data class Position(val row: Int, val col: Int) {
        fun inProximityOf(other: Position): Boolean =
            row in (other.row - 1)..(other.row + 1) && col in (other.col - 1)..(other.col + 1)
    }
}




