import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
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

private fun parseSeeds(text: String): List<Long> =
    text
        .substringAfter("seeds: ")
        .lines()
        .first()
        .split(" ")
        .map { it.toLong() }

private fun getDestination(source: Long, ranges: List<Pair<LongRange, LongRange>>): Long =
    ranges
        .firstOrNull { it.first.contains(source) }
        ?.let {
            val offset = it.second.first - it.first.first
            source + offset
        }
        ?: source

private fun resolveLocation(seed: Long, list: List<List<Pair<LongRange, LongRange>>>): Long =
    list.fold(seed) { acc, pairs ->
        getDestination(acc, pairs)
    }

suspend fun main() {
    val text = File("Day05Input.txt").readText()

    val seeds = parseSeeds(text)

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

    println("Part 1 result: ${part1(seeds, list)}")
    println("Part 2 result: ${part2(seeds, list)}")
}

private fun part1(seeds: List<Long>, list: List<List<Pair<LongRange, LongRange>>>): Long =
    seeds.minOf { resolveLocation(it, list) }

private suspend fun part2(seeds: List<Long>, list: List<List<Pair<LongRange, LongRange>>>): Long = coroutineScope {
    var min = Long.MAX_VALUE

    val ranges = seeds
        .chunked(2)
        .map { LongRange(it[0], it[0] + it[1]) }

    ranges
        .mapIndexed { index, range ->
            async {
                println("Range $index started")

                val location = range.minOf { resolveLocation(it, list) }

                if (location < min) {
                    min = location
                }

                println("Range $index ended")
            }
        }
        .awaitAll()

    min
}
