import java.util.*

object DAY06 {

    fun run(fileName: String, markerLength: Int): Int {
        val lines = readInput(fileName)
        var result = 0
        for (line in lines) {
            result = calculateFirstMarker(line, markerLength)
            println("$result ($markerLength) <- $line")
        }
        return result
    }

    private fun calculateFirstMarker(line: String, markerLength: Int): Int {
        val buf = mutableListOf<Char>()
        initiateBuffer(markerLength, buf)
        var chars = 0
        for (char in line) {
            chars++
            updateBuffer(buf, char, markerLength)
            if (chars > markerLength && allDifferentInBuffer(buf, markerLength)) {
                return chars
            }
        }
        return -1
    }

    private fun allDifferentInBuffer(buf: List<Char>, markerLength: Int): Boolean {
        for (base in 0 until markerLength) {
            for (compare in base + 1 until markerLength) {
                if (buf[base] == buf[compare]) {
                    return false
                }
            }
        }
        return true
    }

    private fun initiateBuffer(markerLength: Int, buf: MutableList<Char>) {
        for (i in 0 until markerLength) {
            buf.add(' ')
        }
    }

    private fun updateBuffer(buf: MutableList<Char>, char: Char, markerLength: Int) {
        for (i in 0 until markerLength) {
            if (i == markerLength - 1) {
                buf[i] = char
            } else {
                buf[i] = buf[i + 1]
            }
        }
    }

}

fun main() {
    if (DAY06.run("Day06", 4) != 1779) {
        throw RuntimeException("Fail: Expected 1779")
    }
    if (DAY06.run("Day06", 14) != 2635) {
        throw RuntimeException("Fail: Expected 2635")
    }
}
