import java.io.File

val digits = mapOf(
    "one" to "1",
    "two" to "2",
    "three" to "3",
    "four" to "4",
    "five" to "5",
    "six" to "6",
    "seven" to "7",
    "eight" to "8",
    "nine" to "9"
)

fun main() {
    val lines = File("Day01Input.txt").readLines()

    println("Part 1 result: ${part1(lines)}")
    println("Part 2 result: ${part2(lines)}")
}

private fun part1(lines: List<String>): Int =
    lines
        .map { line ->
            val digits = line.filter { it.isDigit() }
            "${digits.first()}${digits.last()}"
        }
        .sumOf { it.toInt() }

private fun part2(lines: List<String>): Int =
    lines
        .map { line ->
            val textOccurrences = mutableMapOf<Int, String>()

            digits.forEach { digit ->
                line.windowed(digit.key.length).forEachIndexed { index, s ->
                    if (s == digit.key) {
                        textOccurrences[index] = digit.value
                    }
                }
            }

            val digitsOccurrences = line.mapIndexedNotNull { index, char ->
                char.takeIf { it.isDigit() }?.let {
                    index to char.toString()
                }
            }

            val allOccurrences = textOccurrences + digitsOccurrences

            allOccurrences.toSortedMap()
                .values
                .fold("") { acc, value ->
                    acc + value
                }
        }
        .let { linesDigitsOnly ->
            part1(linesDigitsOnly)
        }