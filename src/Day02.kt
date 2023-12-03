import Cube.BLUE
import Cube.GREEN
import Cube.RED
import java.io.File

private enum class Cube {
    BLUE,
    RED,
    GREEN
}

fun main() {
    val lines = File("Day02Input.txt").readLines()
    val cubes = parseCubes(lines)

    println("Part 1 result: ${part1(cubes, mapOf(RED to 12, GREEN to 13, BLUE to 14))}")
    println("Part 2 result: ${part2(cubes)}")
}

private fun parseCubes(lines: List<String>): List<Pair<Int, List<Map<Cube, Int>>>> =
    lines.map { line ->
        val parts = line.split(": ")
        val id = parts[0].removePrefix("Game ").toInt()
        val subsets = parts[1].split("; ")
        id to subsets.map { subset ->
            subset
                .split(", ")
                .associate {
                    val countAndCube = it.split(" ")
                    Cube.valueOf(countAndCube.last().uppercase()) to countAndCube.first().toInt()
                }
        }
    }

private fun part1(cubes: List<Pair<Int, List<Map<Cube, Int>>>>, maximum: Map<Cube, Int>): Int =
    cubes.filter { (_, cubes) ->
        cubes.all { cubeToCount ->
            cubeToCount.all cubeToCountAll@{ (cube, count) ->
                val max = maximum[cube] ?: return@cubeToCountAll true
                count <= max
            }
        }
    }.sumOf { it.first }

private fun part2(cubes: List<Pair<Int, List<Map<Cube, Int>>>>) =
    cubes.map { entry ->
        entry.second.fold(mutableMapOf(RED to 0, GREEN to 0, BLUE to 0)) { acc, map ->
            map.entries.forEach {
                if (it.value > (acc[it.key] ?: 0)) {
                    acc[it.key] = it.value
                }
            }
            acc
        }
    }.sumOf { it.getValue(RED) * it.getValue(GREEN) * it.getValue(BLUE) }