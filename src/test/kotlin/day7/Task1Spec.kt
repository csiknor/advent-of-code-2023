package day7

import day7.Card.*
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Task1Spec : StringSpec({
    listOf(
        "52T3K" to Hand(listOf(CARD_5, CARD_2, CARD_T, CARD_3, CARD_K)),
        "32T3K" to Hand(listOf(CARD_3, CARD_2, CARD_T, CARD_3, CARD_K)),
        "KK677" to Hand(listOf(CARD_K, CARD_K, CARD_6, CARD_7, CARD_7)),
    ).forEach { (input, hand) ->
        handOf(input) shouldBe hand
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
        "identifies hands" {
            handOf(input).type() shouldBe type
        }
    }

    listOf(
        "52T3K" to "AQQQA",
        "32T3K" to "KK677",
        "AQQQQ" to "AAAAA",
        "5T5J5" to "T55J5",
        "KK776" to "KK778",
    ).forEach { (a, b) ->
        "hand compare: $a < $b" {
            (handOf(a) < handOf(b)) shouldBe true
        }
    }

    "hand equal" {
        (handOf("52T3K") == handOf("52T3K")) shouldBe true
    }


    "solves sample" {
        Task1.solve("sample.txt") shouldBe 6440
    }
})