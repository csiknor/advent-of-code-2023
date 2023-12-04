package day4

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Task2Spec: StringSpec({
    listOf(
        Card(1, listOf(4, 12, 77), listOf(33, 44, 55)) to 0,
        Card(1, listOf(4, 12, 77), listOf(33, 44, 77)) to 1,
        Card(1, listOf(4, 12, 77), listOf(4, 12, 55)) to 2,
    ).forEach { (card, count) ->
        "$card has count matching numbers" {
            card.matchingCount() shouldBe count
        }
    }

    listOf(
        sequenceOf(
            "Card 1: 55 60 70 90 |  1  6 45 56 78 98", // 0
            "Card 2: 55 60 70 90 |  1  6 45 55 60 98", // 2 -> Card 3, Card 4
            "Card 3: 55 60 70 90 |  1  6 45 55 78 98", // 1 -> Card 4
            "Card 4: 55 60 70 90 |  1  6 45 56 78 98", // 0
        ) to listOf(1, 2, 3, 3, 4, 4, 4, 4),
        sequenceOf(
            "Card 1: 55 60 70 90 |  1  6 55 60 70 98", // 3 -> Card 2, Card 3, Card 4
            "Card 2: 55 60 70 90 |  1  6 45 56 78 98", // 0
            "Card 3: 55 60 70 90 |  1  6 45 55 78 98", // 1 -> Card 4
            "Card 4: 55 60 70 90 |  1  6 45 56 78 98", // 0
        ) to listOf(1, 2, 2, 3, 3, 4, 4, 4, 4),
        sequenceOf(
            "Card 1: 55 60 70 90 |  1  6 45 56 78 98", // 0
            "Card 2: 55 60 70 90 |  1  6 45 55 78 98", // 1 -> Card 3
            "Card 3: 55 60 70 90 |  1  6 45 55 78 98", // 1 -> Card 4
            "Card 4: 55 60 70 90 |  1  6 45 56 78 98", // 0
        ) to listOf(1, 2, 3, 3, 4, 4, 4),
        sequenceOf(
            "Card 1: 55 60 70 90 |  1  6 45 55 78 98", // 1 -> Card 2
            "Card 2: 55 60 70 90 |  1  6 45 55 60 98", // 2 -> Card 3, Card 4
            "Card 3: 55 60 70 90 |  1  6 45 55 78 98", // 1 -> Card 4
            "Card 4: 55 60 70 90 |  1  6 45 56 78 98", // 0
        ) to listOf(1, 2, 2, 3, 3, 3, 4, 4, 4, 4, 4, 4),
        sequenceOf(
            "Card 1: 55 60 70 90 |  1  6 45 56 78 98", // 0
            "Card 2: 55 60 70 90 |  1  6 45 55 60 98", // 2 -> Card 3, Card 4
            "Card 3: 55 60 70 90 |  1  6 45 56 78 98", // 0
            "Card 4: 55 60 70 90 |  1  6 45 55 78 98", // 1 -> nothing
        ) to listOf(1, 2, 3, 3, 4, 4),
    ).forEach { (lines, cardIds) ->
        "card copies are generated" {
            lines.parseCards().generateCopies().map { it.id }.toList() shouldBe cardIds
        }
        "processing results in cards count" {
            Task2.process(lines) shouldBe cardIds.size
        }
    }

    "should solve sample correctly" {
        Task2.solve("sample.txt") shouldBe 30
    }

})
