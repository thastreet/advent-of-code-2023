import java.io.File

fun main() {
    val lines = File("Day03Input.txt").readLines()
    println("Part 1 result: ${part1(lines)}")
}

private fun part1(lines: List<String>): Int {
    val numbers = mutableMapOf<Pair<Int, Int>, String>()
    val symbols = mutableSetOf<Pair<Int, Int>>()
    val lineLength = lines[0].length

    lines.forEachIndexed { y, line ->
        var currentNumber = ""
        var startX: Int? = null

        fun addToNumbersIfNecessary() {
            val localStartX = startX
            if (currentNumber.isNotEmpty() && localStartX != null) {
                numbers[Pair(y, localStartX)] = currentNumber
            }
        }

        line.indices.forEach { x ->
            if (line[x].isDigit()) {
                if (startX == null) {
                    startX = x
                }

                currentNumber += line[x]
            } else {
                addToNumbersIfNecessary()

                currentNumber = ""
                startX = null

                if (line[x] != '.') {
                    symbols.add(Pair(y, x))
                }
            }
        }

        addToNumbersIfNecessary()
    }

    return numbers.map { (coords, number) ->
        val projections = mutableSetOf<Pair<Int, Int>>()
        val (y, startX) = coords

        number.indices.forEach { i ->
            val x = startX + i

            if (x - 1 >= 0 && i == 0) { // left
                projections += Pair(y, x - 1)
            }
            if (x + 1 <= lineLength - 1 && i == number.indices.last) { // right
                projections += Pair(y, x + 1)
            }
            if (y - 1 >= 0) { // top
                projections += Pair(y - 1, x)
            }
            if (y + 1 <= lines.size - 1) { // bottom
                projections += Pair(y + 1, x)
            }

            if (x - 1 >= 0 && i == 0 && y - 1 >= 0) { // top left
                projections += Pair(y - 1, x - 1)
            }
            if (x + 1 <= lineLength - 1 && y - 1 >= 0) { // top right
                projections += Pair(y - 1, x + 1)
            }
            if (x - 1 >= 0 && i == 0 && y + 1 <= lines.size - 1) { // bottom left
                projections += Pair(y + 1, x - 1)
            }
            if (x + 1 <= lineLength - 1 && i == number.indices.last && y + 1 <= lines.size - 1) { // bottom right
                projections += Pair(y + 1, x + 1)
            }
        }

        number.toInt() to projections
    }
        .filter { it.second.intersect(symbols).isNotEmpty() }
        .sumOf { it.first }
}