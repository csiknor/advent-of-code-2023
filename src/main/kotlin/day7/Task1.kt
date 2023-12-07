package day7

import java.io.File

enum class Card {
    CARD_2,
    CARD_3,
    CARD_4,
    CARD_5,
    CARD_6,
    CARD_7,
    CARD_8,
    CARD_9,
    CARD_T,
    CARD_J,
    CARD_Q,
    CARD_K,
    CARD_A,
}

enum class HandType {
    FIVE_OF_A_KIND,
    FOUR_OF_A_KIND,
    FULL_HOUSE,
    THREE_OF_A_KIND,
    TWO_PAIR,
    ONE_PAIR,
    HIGH_CARD,
}

data class Hand(val cards: List<Card>) : Comparable<Hand> {
    fun type() = cards.sorted().groupingBy { it }.eachCount().let {
        when (it.count()) {
            1 -> HandType.FIVE_OF_A_KIND
            2 -> when (it.values.max()) {
                4 -> HandType.FOUR_OF_A_KIND
                3 -> HandType.FULL_HOUSE
                else -> throw Error("Two groups???")
            }

            3 -> when (it.values.max()) {
                3 -> HandType.THREE_OF_A_KIND
                2 -> HandType.TWO_PAIR
                else -> throw Error("Three groups???")
            }

            4 -> HandType.ONE_PAIR
            5 -> HandType.HIGH_CARD
            else -> throw Error("Count???")
        }
    }

    override operator fun compareTo(other: Hand): Int = when {
        type() > other.type() -> -1
        type() < other.type() -> 1
        else -> cards.zip(other.cards).map { (a, b) -> a.compareTo(b) }
            .reduce { acc: Int, i: Int -> if (acc == 0) i else acc }
    }
}

fun handOf(input: String) = Hand(input.map { Card.valueOf("CARD_$it") })

object Task1 {
    fun solve(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) }

    fun process(lines: Sequence<String>) = lines
        .map { it.split(" ") }
        .map { (hand, bid) -> handOf(hand) to bid.toInt() }
        .sortedBy { (hand, _) -> hand }
        .mapIndexed { index, (_, bid) -> (index + 1) * bid }
        .sum()

}