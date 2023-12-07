package day7

import java.io.File

enum class RegularCard {
    CARD_2, CARD_3, CARD_4, CARD_5, CARD_6, CARD_7, CARD_8, CARD_9, CARD_T, CARD_J, CARD_Q, CARD_K, CARD_A,
}

enum class JokerCard {
    CARD_J, CARD_2, CARD_3, CARD_4, CARD_5, CARD_6, CARD_7, CARD_8, CARD_9, CARD_T, CARD_Q, CARD_K, CARD_A,
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

data class Hand<T : Enum<T>>(val cards: List<T>) : Comparable<Hand<T>> {
    fun type() = cards
        .sorted()
        .groupingBy { it }.eachCount()
        .filter { it.key !is JokerCard || it.key != JokerCard.CARD_J }
        .let {
            when (it.count()) {
                0 -> HandType.FIVE_OF_A_KIND
                1 -> HandType.FIVE_OF_A_KIND
                2 -> when (it.values.max() + numberOfJokers()) {
                    4 -> HandType.FOUR_OF_A_KIND
                    3 -> HandType.FULL_HOUSE
                    else -> throw Error("Two groups???")
                }

                3 -> when (it.values.max() + numberOfJokers()) {
                    3 -> HandType.THREE_OF_A_KIND
                    2 -> HandType.TWO_PAIR
                    else -> throw Error("Three groups???")
                }

                4 -> HandType.ONE_PAIR
                5 -> HandType.HIGH_CARD
                else -> throw Error("Count???")
            }
        }

    private fun numberOfJokers() = cards.count { it is JokerCard && it == JokerCard.CARD_J }

    override operator fun compareTo(other: Hand<T>): Int = when {
        type() > other.type() -> -1
        type() < other.type() -> 1
        else -> cards.zip(other.cards).map { (a, b) -> a.compareTo(b) }
            .reduce { acc: Int, i: Int -> if (acc == 0) i else acc }
    }
}

fun handOfRegular(input: String) = handOfType(input, RegularCard::class.java)
fun handOfJoker(input: String) = handOfType(input, JokerCard::class.java)

fun <T : Enum<T>> handOfType(input: String, clazz: Class<T>) =
    Hand(input.map { char -> clazz.enumConstants.find { it.name == "CARD_$char" }!! })

object Task {
    fun solvePart1(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) { handOfRegular(it) } }

    fun solvePart2(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) { handOfJoker(it) } }

    fun <T : Enum<T>> process(lines: Sequence<String>, handParser: (String) -> Hand<T>) = lines
        .map { it.split(" ") }
        .map { (hand, bid) -> handParser(hand) to bid.toInt() }
        .sortedBy { (hand, _) -> hand }
        .mapIndexed { index, (_, bid) -> (index + 1) * bid }
        .sum()

}