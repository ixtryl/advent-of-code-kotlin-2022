fun main() {
    val lines = readInput("Day01")

    var elf = 1
    val elfs = mutableMapOf<Int, MutableList<Int>>()
    for (line in lines) {
        if (line.isBlank()) {
            elf++
            continue
        }
        (elfs.getOrPut(elf) { mutableListOf() }).add(Integer.parseInt(line))
    }
    val sums = elfs.mapValues { it.value.sum() }
    val maxElf = sums.maxBy { it.value }
    println("First: $maxElf")

    val descValues = sums.values.sortedDescending()
    println("Second: $descValues")
    val max3 = descValues[0] + descValues[1] + descValues[2]
    println("Second: $max3")
}
