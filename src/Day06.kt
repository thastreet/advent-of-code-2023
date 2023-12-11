import java.io.File
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt


fun main() {
    val lines = File("Day06Input.txt").readLines()

    println("Part 1 result: ${part1(lines)}")
    println("Part 2 result: ${part2(lines)}")
}

private fun getNumberOfWays(raceTime: Long, distanceToBeat: Long): Int {
    val lowerRange = ((-raceTime + sqrt(raceTime.toDouble().pow(2) - 4 * distanceToBeat)) / -2).let {
        if (it % 1 == 0.0) {
            it + 1
        } else {
            it
        }
    }
    val upperRange = ((-raceTime - sqrt(raceTime.toDouble().pow(2) - 4 * distanceToBeat)) / -2).let {
        if (it % 1 == 0.0) {
            it - 1
        } else {
            it
        }
    }

    return IntRange(ceil(lowerRange).toInt(), floor(upperRange).toInt()).count()
}

private fun part1(lines: List<String>): Int {
    val raceTimes = lines[0]
        .substringAfter("Time:")
        .trim()
        .split(" +".toRegex())
        .map { it.toLong() }

    val distancesToBeat = lines[1]
        .substringAfter("Distance:")
        .trim()
        .split(" +".toRegex())
        .map { it.toLong() }

    return raceTimes.zip(distancesToBeat)
        .map { (raceTime, distanceToBeat) ->
            getNumberOfWays(raceTime, distanceToBeat)
        }
        .reduce { acc, numberOfWays ->
            acc * numberOfWays
        }
}

private fun part2(lines: List<String>): Int {
    val raceTime = lines[0]
        .substringAfter("Time:")
        .trim()
        .replace(" ", "")
        .toLong()

    val distanceToBeat = lines[1]
        .substringAfter("Distance:")
        .trim()
        .replace(" ", "")
        .toLong()

    return getNumberOfWays(raceTime, distanceToBeat)
}