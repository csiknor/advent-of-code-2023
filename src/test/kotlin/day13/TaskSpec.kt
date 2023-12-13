package day13

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TaskSpec: StringSpec({
    "solves sample 1" {
        Task.solvePart1("sample.txt") shouldBe 405
    }

    "solves sample 2" {
        Task.solvePart2("sample.txt") shouldBe 400
    }

    "solves" {
        Task.solvePart1("example.txt") shouldBe 100
        Task.solvePart2("example.txt") shouldBe 800
    }

    listOf(
        listOf("a") to false,
        listOf("a", "a") to true,
        listOf("a", "b", "b", "a") to true,
        listOf("a", "b", "b", "a", "c") to false,
        listOf("a", "b", "c", "b", "a") to false,
        listOf("a", "b") to false,
        listOf("a", "b", "c") to false,
    ).forEach { (list, expected) ->
        "mirrored $list is $expected" {
            Task.mirrored(list) shouldBe expected
        }
    }

    listOf(
        listOf("a", "a") to Pair(0,1),
        listOf("a", "b", "b", "a") to Pair(0,3),
        listOf("a", "b", "b", "a", "c", "c") to Pair(0,3),
        listOf("c", "d", "a", "b", "b", "a") to Pair(2,5),
        listOf("c", "d", "a", "b", "b", "a", "c") to null,
        listOf("d", "a", "b", "b", "a", "c") to null,
        listOf("a", "b", "c", "b", "a") to null,
        listOf("a", "b") to null,
        listOf("a", "b", "c") to null,
        listOf(
            "#...##..#",
            "#....#..#",
            "..##..###",
            "#####.##.",
            "#####.##.",
            "..##..###",
            "#....#..#",
        ) to Pair(1,6),
        Task.transpose(listOf(
            "#.##..##.",
            "..#.##.#.",
            "##......#",
            "##......#",
            "..#.##.#.",
            "..##..##.",
            "#.#.##.#.",
        )) to Pair(1,8),
    ).forEach { (list, expected) ->
        "find mirrored $list is $expected" {
            Task.findMirrored(list) shouldBe expected
        }
    }

    listOf(
        listOf("ab", "cd") to listOf("ac", "bd"),
        listOf("123", "abc") to listOf("1a", "2b", "3c"),
    ).forEach { (input, expected) ->
        "transposes $input" {
            Task.transpose(input) shouldBe expected
        }
    }

    "parse" {
        Task.parse(listOf(
            "aaa",
            "bbb",
            "ccc",
            "",
            "123",
            "456",
            "789",
        )) shouldBe listOf(listOf(
            "aaa",
            "bbb",
            "ccc",
        ), listOf(
            "123",
            "456",
            "789",
        ))
    }
})