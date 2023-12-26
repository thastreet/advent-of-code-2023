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
}

private fun part1(instructions: String, network: Map<String, Pair<String, String>>): Int {
    var steps = 0
    var i = 0
    var currentKey = "AAA"
    
    do {
        val instruction = instructions[i]

        currentKey = if (instruction == 'R') {
            network.getValue(currentKey).second
        } else {
            network.getValue(currentKey).first
        }

        ++i
        ++steps
        if (i >= instructions.length) {
            i = 0
        }
    } while (currentKey != "ZZZ")

    return steps
}