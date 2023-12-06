import java.io.File
import kotlin.math.pow

fun main() {
    val lines = File("Day04Input.txt").readLines()
    println("Part 1 result: ${part1(lines)}")
}

private fun part1(lines: List<String>): Int = lines.map { line ->
    val parts = line.split(": ")[1].split(" | ")
    val winning = parts[0].chunked(3).map { it.trim().toInt() }.toSet()
    val other = parts[1].chunked(3).map { it.trim().toInt() }.toSet()

    other.intersect(winning)
        .size
        .takeIf { it > 0 }
        ?.let { winningNumbersCount -> (1 * 2.0.pow(winningNumbersCount.toDouble() - 1)).toInt() }
        ?: 0
}.sum()