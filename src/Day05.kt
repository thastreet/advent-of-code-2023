import java.io.File

private fun parseRanges(text: String, name: String): List<Pair<LongRange, LongRange>> =
    text
        .substringAfter("$name map:")
        .lines()
        .drop(1)
        .takeWhile { it.isNotEmpty() }
        .map { line ->
            val parts = line.split(" ").map { it.toLong() }
            val destinationRangeStart = parts[0]
            val sourceRangeStart = parts[1]
            val rangeLength = parts[2]
            LongRange(sourceRangeStart, sourceRangeStart + rangeLength) to LongRange(destinationRangeStart, destinationRangeStart + rangeLength)
        }

private fun getDestination(source: Long, ranges: List<Pair<LongRange, LongRange>>): Long =
    ranges
        .firstOrNull { it.first.contains(source) }
        ?.let {
            val offset = it.second.first - it.first.first
            source + offset
        }
        ?: source

fun main() {
    val text = File("Day05Input.txt").readText()

    println("Part 1 result: ${part1(text)}")
}

private fun part1(text: String): Long {
    val seeds = text
        .substringAfter("seeds: ")
        .lines()
        .first()
        .split(" ")
        .map { it.toLong() }

    val seedToSoil = parseRanges(text, "seed-to-soil")
    val soilToFertilizer = parseRanges(text, "soil-to-fertilizer")
    val fertilizerToWater = parseRanges(text, "fertilizer-to-water")
    val waterToLight = parseRanges(text, "water-to-light")
    val lightToTemperature = parseRanges(text, "light-to-temperature")
    val temperatureToHumidity = parseRanges(text, "temperature-to-humidity")
    val humidityToLocation = parseRanges(text, "humidity-to-location")

    val list = listOf(
        seedToSoil,
        soilToFertilizer,
        fertilizerToWater,
        waterToLight,
        lightToTemperature,
        temperatureToHumidity,
        humidityToLocation
    )

    val locations = seeds.associateWith { seed ->
        list.fold(seed) { acc, pairs ->
            getDestination(acc, pairs)
        }
    }

    return locations.minOf { it.value }
}