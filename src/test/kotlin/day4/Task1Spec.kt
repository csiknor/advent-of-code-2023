package day4

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Task1Spec : StringSpec({

    listOf(
        "Card 1: 12 | 12" to Card(1, listOf(12), listOf(12)),
        "Card 2: 12 13 | 12" to Card(2, listOf(12, 13), listOf(12)),
        "Card 3: 12 | 12 13" to Card(3, listOf(12), listOf(12, 13)),
        "Card 4: 12 14 15 | 12 14 19" to Card(4, listOf(12, 14, 15), listOf(12, 14, 19)),
        "Card 5:  1  8 10 | 10  3 40  7" to Card(5, listOf(1, 8, 10), listOf(10, 3, 40, 7)),
        "Card  6: 12 | 12" to Card(6, listOf(12), listOf(12)),
        "Card 10: 12 | 12" to Card(10, listOf(12), listOf(12)),
        "Card   7: 12 | 12" to Card(7, listOf(12), listOf(12)),
        "Card  99: 12 | 12" to Card(99, listOf(12), listOf(12)),
    ).forEach { (line, card) ->
        "should parse line into card: $line" {
            Task1.cardOf(line) shouldBe card
        }
    }

    listOf(
        "Card 1: 83 | 83",
        "Card 2: 83 12 | 83 19",
        "Card 3: 26 83 | 47 83",
        "Card 4: 26 83 | 83 99",
    ).forEach { line ->
        "should find one winner of single line: $line" {
            Task1.process(sequenceOf(line)) shouldBe 1
        }
    }

    listOf(
        "Card 1: 83 90 | 83 90",
        "Card 1: 12 83 90 | 50 83 90",
        "Card 1: 83 90 95 | 83 90 99",
        "Card 1: 83 90 | 14 33 83 90 77",
    ).forEach { line ->
        "should find two winners of single line: $line" {
            Task1.process(sequenceOf(line)) shouldBe 2
        }
    }

    listOf(
        "Card 1: 83 90 | 12 14 40" to 0,
        "Card 1: 83 90 | 12 14 83" to 1,
        "Card 1: 83 90 | 12 83 90" to 2,
        "Card 1: 12 83 90 | 12 55 83 90 91" to 4,
    ).forEach { (line, points) ->
        "should find defined number of winners of single line: $line" {
            Task1.process(sequenceOf(line)) shouldBe points
        }
    }

    listOf(
        7 to sequenceOf(
            "Card 1: 55 60 70 90 |  1  6 45 56 78 98",
            "Card 2: 55 60 70 90 |  1  6 45 55 78 98",
            "Card 3: 55 60 70 90 |  1  6 45 55 60 98",
            "Card 4: 55 60 70 90 |  1  6 45 55 60 70",
        ),
        15 to sequenceOf(
            "Card 1: 55 60 70 90 |  1  6 45 56 78 98",
            "Card 2: 55 60 70 90 |  1  6 45 55 78 98",
            "Card 3: 55 60 70 90 |  1  6 45 55 60 98",
            "Card 4: 55 60 70 90 |  1  6 45 55 60 70",
            "Card 5: 55 60 70 90 |  6 45 55 60 70 90",
        ),
    ).forEach { (points, lines) ->
        "should calculate the right points of multiple lines: $points" {
            Task1.process(lines) shouldBe points
        }
    }

    "should solve sample correctly" {
        Task1.solve("sample.txt") shouldBe 13
    }

})