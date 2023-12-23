import java.io.File

private enum class Card2(val value: Char) {
    AS('A'),
    KING('K'),
    QUEEN('Q'),
    TEN('T'),
    NINE('9'),
    EIGHT('8'),
    SEVEN('7'),
    SIX('6'),
    FIVE('5'),
    FOUR('4'),
    THREE('3'),
    TWO('2'),
    JOKER('J');

    val strength: Int
        get() = Card2.entries.size - Card2.entries.indexOf(this) - 1
}

private enum class Type2 {
    FIVE_OF_A_KIND,
    FOUR_OF_A_KIND,
    FULL_HOUSE,
    THREE_OF_A_KIND,
    TWO_PAIR,
    ONE_PAIR,
    HIGH_CARD
}

private data class Hand2(val cards: List<Card2>) : Comparable<Hand2> {
    private val groups = cards.groupBy { it.value }

    private val type: Type2 = run {
        fun resolveType(hand: Hand2): Type2 = when {
            hand.groups.size == 1 -> Type2.FIVE_OF_A_KIND
            hand.groups.any { it.value.size == 4 } -> Type2.FOUR_OF_A_KIND
            hand.groups.any { it.value.size == 3 } && hand.groups.any { it.value.size == 2 } -> Type2.FULL_HOUSE
            hand.groups.any { it.value.size == 3 } -> Type2.THREE_OF_A_KIND
            hand.groups.size == 3 -> Type2.TWO_PAIR
            hand.groups.size == 4 -> Type2.ONE_PAIR
            else -> Type2.HIGH_CARD
        }

        if (cards.any { it == Card2.JOKER }) {
            val otherCards = cards
                .filterNot { it == Card2.JOKER }
                .distinct()

            otherCards
                .associateWith { cardToCopy ->
                    resolveType(
                        copy(
                            cards = cards.map {
                                it.takeUnless { it == Card2.JOKER } ?: cardToCopy
                            }
                        )
                    )
                }
                .takeUnless { it.isEmpty() }
                ?.minBy { Type2.entries.indexOf(it.value) }
                ?.value
                ?: Type2.FIVE_OF_A_KIND // Every cards are Jokers
        } else {
            resolveType(this)
        }
    }

    private val strength = Type2.entries.size - Type2.entries.indexOf(type) - 1

    override fun compareTo(other: Hand2): Int {
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
        val hand = Hand2(
            parts[0].map { char ->
                Card2.entries.first { it.value == char }
            }
        )
        val bid = parts[1].toInt()
        hand to bid
    }

    println("Part 2 result: ${part2(handsWithBid)}")
}

private fun part2(handsWithBid: List<Pair<Hand2, Int>>): Int =
    handsWithBid
        .sortedBy { it.first }
        .mapIndexed { index, pair -> (index + 1) * pair.second }
        .sum()