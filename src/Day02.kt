import java.lang.IllegalArgumentException
import java.lang.RuntimeException

enum class TOKEN(val value: Int) {
    ROCK(1), SCISSORS(2), PAPER(3)
}

fun matchPoints(player: TOKEN, opponent: TOKEN): Int {
    when {
        player == TOKEN.ROCK && opponent == TOKEN.SCISSORS
                || player == TOKEN.PAPER && opponent == TOKEN.ROCK
                || player == TOKEN.SCISSORS && opponent == TOKEN.PAPER -> {
            print("WIN ")
            return 6
        }

        player == opponent -> {
            print("DRAW ")
            return 3
        }

        else -> {
            print("LOOSE ")
            return 0
        }
    }
}

fun String.convertToToken(): TOKEN {
    return when (this) {
        "A", "X" -> TOKEN.ROCK
        "B", "Y" -> TOKEN.PAPER
        "C", "Z" -> TOKEN.SCISSORS
        else -> throw IllegalArgumentException("Bad input $this")
    }
}

fun getPoints(fileName: String): Int {
    val lines = readInput(fileName)
    var points = 0
    for (line in lines) {
        val parts = line.split(" ")
        val opponent = parts[0].convertToToken()
        val player = parts[1].convertToToken()
        val currentMatchPoints = player.value + matchPoints(player, opponent)
        points += currentMatchPoints
        println("$line -> $opponent $player $currentMatchPoints $points")
    }
    println("File $fileName -> Points: $points")
    return points
}

fun main() {
    if (getPoints("Day02_test2") != (18 + 27)) {
        throw RuntimeException("Failed...")
    }
    if (getPoints("Day02_test") != 15) {
        throw RuntimeException("Failed...")
    }
    getPoints("Day02")
}
