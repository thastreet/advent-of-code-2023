import java.io.File

private data class Data(
    val numbers: Map<Pair<Int, Int>, String>,
    val symbols: Set<Pair<Pair<Int, Int>, Char>>
)

fun main() {
    val lines = File("Day03Input.txt").readLines()
    println("Part 1 result: ${part1(lines)}")
    println("Part 2 result: ${part2(lines)}")
}

private fun parseData(lines: List<String>): Data {
    val numbers = mutableMapOf<Pair<Int, Int>, String>()
    val symbols = mutableSetOf<Pair<Pair<Int, Int>, Char>>()

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
                    symbols.add((y to x) to line[x])
                }
            }
        }

        addToNumbersIfNecessary()
    }

    return Data(numbers, symbols)
}

private fun Map.Entry<Pair<Int, Int>, String>.calculateProjections(lines: List<String>): Pair<Set<Pair<Int, Int>>, String> {
    val lineLength = lines[0].length
    val projections = mutableSetOf<Pair<Int, Int>>()
    val (y, startX) = key
    val number = value

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

    return projections to number
}

private fun part1(lines: List<String>): Int {
    val data = parseData(lines)
    val symbolsCoords = data.symbols.map { (coords, _) -> coords }.toSet()

    return data.numbers
        .map { it.value.toInt() to it.calculateProjections(lines) }
        .filter { (_, projections) ->
            val coordsSet = projections.first
            coordsSet.intersect(symbolsCoords).isNotEmpty()
        }
        .sumOf { (number, _) -> number }
}

private fun part2(lines: List<String>): Int {
    val (numbers, symbols) = parseData(lines)

    val projections = numbers.map {
        it.calculateProjections(lines)
    }

    return symbols
        .filter { (_, symbol) -> symbol == '*' }
        .map { (coords, _) -> coords }
        .mapNotNull { coords ->
            projections
                .filter { (coordsSet, _) -> coordsSet.contains(coords) }
                .takeIf { it.size == 2 }
                ?.map { (_, number) -> number.toInt() }
        }
        .sumOf { it.reduce { acc, s -> acc * s } }
}