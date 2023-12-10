import java.io.File


fun main() {
    val lines = File("Day06Input.txt").readLines()

    val times = lines[0]
        .substringAfter("Time:")
        .trim()
        .split(" +".toRegex())
        .map { it.toInt() }

    val distances = lines[1]
        .substringAfter("Distance:")
        .trim()
        .split(" +".toRegex())
        .map { it.toInt() }

    println("Part 1 result: ${part1(times, distances)}")
}

private fun part1(times: List<Int>, distances: List<Int>): Int =
    times.zip(distances)
        .map { (raceTime, distanceToBeat) ->
            (1..<raceTime).filter { timeToTest ->
                timeToTest * (raceTime - timeToTest) > distanceToBeat
            }
        }
        .map { it.size }
        .reduce { acc, size -> acc * size }
