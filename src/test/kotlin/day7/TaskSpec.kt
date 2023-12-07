package day7

import day7.RegularCard.*
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TaskSpec : StringSpec({
    listOf(
        "52T3K" to Hand(listOf(CARD_5, CARD_2, CARD_T, CARD_3, CARD_K)),
        "32T3K" to Hand(listOf(CARD_3, CARD_2, CARD_T, CARD_3, CARD_K)),
        "KK677" to Hand(listOf(CARD_K, CARD_K, CARD_6, CARD_7, CARD_7)),
    ).forEach { (input, hand) ->
        "parses hand $input" {
            handOfRegular(input) shouldBe hand
        }
    }

    listOf(
        "52T3K" to HandType.HIGH_CARD,
        "32T3K" to HandType.ONE_PAIR,
        "KK677" to HandType.TWO_PAIR,
        "T55J5" to HandType.THREE_OF_A_KIND,
        "AQQQA" to HandType.FULL_HOUSE,
        "AQQQQ" to HandType.FOUR_OF_A_KIND,
        "AAAAA" to HandType.FIVE_OF_A_KIND,
    ).forEach { (input, type) ->
        "identifies regular hand $input" {
            handOfRegular(input).type() shouldBe type
        }
    }

    listOf(
        "QJJQQ" to HandType.FIVE_OF_A_KIND,
        "QJJQ2" to HandType.FOUR_OF_A_KIND,
        "QJ2Q2" to HandType.FULL_HOUSE,
        "QJ3Q2" to HandType.THREE_OF_A_KIND,
        "32T3K" to HandType.ONE_PAIR,
        "KK677" to HandType.TWO_PAIR,
        "T55J5" to HandType.FOUR_OF_A_KIND,
        "KTJJT" to HandType.FOUR_OF_A_KIND,
        "QQQJA" to HandType.FOUR_OF_A_KIND,
        "JJJJJ" to HandType.FIVE_OF_A_KIND,
    ).forEach { (input, type) ->
        "identifies joker hand $input" {
            handOfJoker(input).type() shouldBe type
        }
    }

    listOf(
        "52T3K" to "AQQQA",
        "32T3K" to "KK677",
        "AQQQQ" to "AAAAA",
        "5T5J5" to "T55J5",
        "KK776" to "KK778",
    ).forEach { (a, b) ->
        "regular hand compare: $a < $b" {
            (handOfRegular(a) < handOfRegular(b)) shouldBe true
        }
    }

    listOf(
        "52T3K" to "AQQQA",
        "32T3K" to "KK677",
        "AQQQQ" to "AAAAA",
        "5T5J5" to "T55J5",
        "KK776" to "KK778",
        "JK789" to "KK789",
        "J7776" to "77778",
    ).forEach { (a, b) ->
        "joker hand compare: $a < $b" {
            (handOfJoker(a) < handOfJoker(b)) shouldBe true
        }
    }

    "hand equal" {
        (handOfRegular("52T3K") == handOfRegular("52T3K")) shouldBe true
    }


    "solves sample part 1" {
        Task.solvePart1("sample.txt") shouldBe 6440
    }

    "solves sample part 2" {
        Task.solvePart2("sample.txt") shouldBe 5905
    }
})