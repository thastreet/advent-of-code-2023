import java.io.File

fun main() {
    val lines = File("Day08Input.txt").readLines()
    val instructions = lines.first()
    val network = lines
        .drop(2)
        .associate { line ->
            val parts = line.split(" = ")
            val origin = parts[0]
            val destination = parts[1]
                .removeSurrounding("(", ")")
                .split(", ")
                .let {
                    it.first() to it.last()
                }

            origin to destination
        }

    println("Part 1 result: ${part1(instructions, network)}")
    println("Part 2 result: ${part2(instructions, network)}")
}

private fun part1(instructions: String, network: Map<String, Pair<String, String>>): Int {
    var steps = 0
    var i = 0
    var currentKey = "AAA"

    do {
        val instruction = instructions[i]
        val destinations = network.getValue(currentKey)

        currentKey = if (instruction == 'R') {
            destinations.second
        } else {
            destinations.first
        }

        ++i
        ++steps
        if (i >= instructions.length) {
            i = 0
        }
    } while (currentKey != "ZZZ")

    return steps
}

private fun part2(instructions: String, network: Map<String, Pair<String, String>>): Long {
    val steps = network
        .keys
        .filter { it.endsWith("A") }
        .map { keyToMap ->
            var steps = 0L
            var i = 0
            var currentKey = keyToMap

            do {
                val instruction = instructions[i]
                val destinations = network.getValue(currentKey)

                currentKey = if (instruction == 'R') {
                    destinations.second
                } else {
                    destinations.first
                }

                ++i
                ++steps
                if (i >= instructions.length) {
                    i = 0
                }
            } while (!currentKey.endsWith("Z"))

            steps
        }

    return leastCommonMultiple(steps.toLongArray())
}

private fun greatestCommonDivider(a: Long, b: Long): Long {
    var currentA = a
    var currentB = b

    while (currentB > 0) {
        val temp = currentB
        currentB = currentA % currentB
        currentA = temp
    }

    return currentA
}

private fun leastCommonMultiple(a: Long, b: Long): Long =
    a * (b / greatestCommonDivider(a, b))

private fun leastCommonMultiple(input: LongArray): Long {
    var result = input[0]

    for (i in 1 until input.size) {
        result = leastCommonMultiple(result, input[i])
    }

    return result
}