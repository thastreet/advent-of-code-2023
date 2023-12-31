import java.io.File

fun main() {
    val lines = File("Day09Input.txt").readLines()
    val sequences = lines
        .map { line ->
            line
                .split(" ")
                .map { it.toInt() }
        }

    val sequencesWithDifferences = sequences.map { sequence ->
        val differences = mutableListOf<List<Int>>()
        var current = sequence

        do {
            val diff = current
                .zipWithNext()
                .map { it.second - it.first }
            differences.add(diff)
            current = diff
        } while (!current.all { it == 0 })

        listOf(sequence) + differences
    }

    println("Part 1 result: ${part1(sequencesWithDifferences)}")
    println("Part 2 result: ${part2(sequencesWithDifferences)}")
}

private fun part1(sequencesWithDifferences: List<List<List<Int>>>): Int =
    sequencesWithDifferences
        .map { sequenceWithDifferences ->
            sequenceWithDifferences.reduceRightIndexed { index, differences, acc ->
                val toAppend: Int = differences.last() + if (index == sequenceWithDifferences.lastIndex - 1) 0 else acc.last()
                differences + listOf(toAppend)
            }
        }
        .sumOf { it.last() }

private fun part2(sequencesWithDifferences: List<List<List<Int>>>): Int =
    sequencesWithDifferences
        .map { sequenceWithDifferences ->
            sequenceWithDifferences.reduceRightIndexed { index, differences, acc ->
                val toPrepend: Int = differences.first() - if (index == sequenceWithDifferences.lastIndex - 1) 0 else acc.first()
                listOf(toPrepend) + differences
            }
        }
        .sumOf { it.first() }