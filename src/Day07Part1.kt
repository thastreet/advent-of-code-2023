import java.io.File

private enum class Card1(val value: Char) {
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
        get() = Card1.entries.size - Card1.entries.indexOf(this) - 1
}

private enum class Type1 {
    FIVE_OF_A_KIND,
    FOUR_OF_A_KIND,
    FULL_HOUSE,
    THREE_OF_A_KIND,
    TWO_PAIR,
    ONE_PAIR,
    HIGH_CARD
}

private data class Hand1(val cards: List<Card1>) : Comparable<Hand1> {
    private val groups = cards.groupBy { it.value }

    private val type = when {
        groups.size == 1 -> Type1.FIVE_OF_A_KIND
        groups.any { it.value.size == 4 } -> Type1.FOUR_OF_A_KIND
        groups.any { it.value.size == 3 } && groups.any { it.value.size == 2 } -> Type1.FULL_HOUSE
        groups.any { it.value.size == 3 } -> Type1.THREE_OF_A_KIND
        groups.size == 3 -> Type1.TWO_PAIR
        groups.size == 4 -> Type1.ONE_PAIR
        else -> Type1.HIGH_CARD
    }

    private val strength = Type1.entries.size - Type1.entries.indexOf(type) - 1

    override fun compareTo(other: Hand1): Int {
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
        val hand = Hand1(
            parts[0].map { char ->
                Card1.entries.first { it.value == char }
            }
        )
        val bid = parts[1].toInt()
        hand to bid
    }

    println("Part 1 result: ${part1(handsWithBid)}")
}

private fun part1(handsWithBid: List<Pair<Hand1, Int>>): Int =
    handsWithBid
        .sortedBy { it.first }
        .mapIndexed { index, pair -> (index + 1) * pair.second }
        .sum()