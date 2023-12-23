import java.io.File

private enum class Card(val value: Char) {
    AS('A'),
    KING('K'),
    QUEEN('Q'),
    JACK('J'),
    TEN('T'),
    NINE('9'),
    EIGHT('8'),
    SEVEN('7'),
    SIX('6'),
    FIVE('5'),
    FOUR('4'),
    THREE('3'),
    TWO('2');

    val strength: Int
        get() = Card.entries.size - Card.entries.indexOf(this) - 1
}

private enum class Type {
    FIVE_OF_A_KIND,
    FOUR_OF_A_KIND,
    FULL_HOUSE,
    THREE_OF_A_KIND,
    TWO_PAIR,
    ONE_PAIR,
    HIGH_CARD
}

private data class Hand(val cards: List<Card>) : Comparable<Hand> {
    private val groups = cards.groupBy { it.value }

    private val type = when {
        groups.size == 1 -> Type.FIVE_OF_A_KIND
        groups.any { it.value.size == 4 } -> Type.FOUR_OF_A_KIND
        groups.any { it.value.size == 3 } && groups.any { it.value.size == 2 } -> Type.FULL_HOUSE
        groups.any { it.value.size == 3 } -> Type.THREE_OF_A_KIND
        groups.size == 3 -> Type.TWO_PAIR
        groups.size == 4 -> Type.ONE_PAIR
        else -> Type.HIGH_CARD
    }

    private val strength = Type.entries.size - Type.entries.indexOf(type) - 1

    override fun compareTo(other: Hand): Int {
        val diff = strength - other.strength

        if (diff == 0) {
            val distinct = cards
                .zip(other.cards)
                .first { it.first.strength != it.second.strength }

            return distinct.first.strength - distinct.second.strength
        }

        return diff
    }
}

fun main() {
    val lines = File("Day07Input.txt").readLines()

    val handsWithBid = lines.map { line ->
        val parts = line.split(" ")
        val hand = Hand(
            parts[0].map { char ->
                Card.entries.first { it.value == char }
            }
        )
        val bid = parts[1].toInt()
        hand to bid
    }

    println("Part 1 result: ${part1(handsWithBid)}")
}

private fun part1(handsWithBid: List<Pair<Hand, Int>>): Int =
    handsWithBid
        .sortedBy { it.first }
        .mapIndexed { index, pair -> (index + 1) * pair.second }
        .sum()