import java.io.File
import kotlin.math.pow

fun main() {
    val lines = File("Day04Input.txt").readLines()
    val data = parseData(lines)

    println("Part 1 result: ${part1(data)}")
    println("Part 2 result: ${part2(data)}")
}

private fun parseData(lines: List<String>): Map<Int, Pair<Set<Int>, Set<Int>>> =
    lines.associate { line ->
        val gamePart = line.split(": ")
        val cardNumber = gamePart[0].removePrefix("Card").trim().toInt()
        val parts = gamePart[1].split(" | ")
        val winning = parts[0].chunked(3).map { it.trim().toInt() }.toSet()
        val other = parts[1].chunked(3).map { it.trim().toInt() }.toSet()

        cardNumber to Pair(winning, other)
    }

private fun part1(data: Map<Int, Pair<Set<Int>, Set<Int>>>): Int = data.map { entry ->
    entry.value.second.intersect(entry.value.first)
        .size
        .takeIf { it > 0 }
        ?.let { winningNumbersCount -> (1 * 2.0.pow(winningNumbersCount.toDouble() - 1)).toInt() }
        ?: 0
}.sum()

private fun part2(data: Map<Int, Pair<Set<Int>, Set<Int>>>): Int {
    val copies: MutableMap<Int, Int> = data.entries.associate { it.key to 1 }.toMutableMap()

    data.map { entry ->
        val winningCount = entry.value.second.intersect(entry.value.first).size
        val numberOfCardsForCurrentEntry = copies.getValue(entry.key)
        val cardNumbersToIncrement = entry.key + 1..entry.key + winningCount

        cardNumbersToIncrement.forEach { cardNumberToIncrement ->
            if (copies.containsKey(cardNumberToIncrement)) {
                copies[cardNumberToIncrement] = copies.getValue(cardNumberToIncrement) + numberOfCardsForCurrentEntry
            }
        }
    }

    return copies.values.sum()
}