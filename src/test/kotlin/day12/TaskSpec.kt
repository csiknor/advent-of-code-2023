package day12

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TaskSpec: StringSpec({
    "solves sample 1" {
        Task.solvePart1("sample.txt") shouldBe 21
    }

    "solves sample 2" {
        Task.solvePart2("sample.txt") shouldBe TODO()
    }

    "parses" {
        Task.parse(sequenceOf("..#?.#.? 2,1,1")).toList() shouldBe listOf(Row(listOf(false, false, true, null, false, true, false, null), listOf(2, 1, 1)))
    }

    listOf(
        "..# 1" to true,
        "#.# 1,1" to true,
        "..#..###. 1,3" to true,
        "#.# 1" to false,
        "..# 1,1" to false,
        "..##.###. 1,3" to false,
    ).forEach { (input, expected) ->
        "valid [$input] $expected" {
            Task.valid(Task.parse(sequenceOf(input)).first()) shouldBe expected
        }
    }

    listOf(
        "..? 1" to true,
        "#.? 1,1" to true,
        "..#..###. 1,3" to true,
        "..#..#??. 1,3" to true,
        "..#..##?. 1,3" to true,
        "##? 1" to false,
        "..? 1,1" to true, //!
        "..#?.###. 1,3" to true,
        "..#...??. 1,3" to true, //!
        "..##..??. 1,3" to false,
        "..#...#?. 1,3" to true, //!
    ).forEach { (input, expected) ->
        "can [$input] $expected" {
            Task.can(Task.parse(sequenceOf(input)).first()) shouldBe expected
        }
    }

    listOf(
        "???.### 1,1,3" to 1,
        ".??..??...?##. 1,1,3" to 4,
        "?#?#?#?#?#?#?#? 1,3,1,6" to 1,
        "????.#...#... 4,1,1" to 1,
        "????.######..#####. 1,6,5" to 4,
        "?###???????? 3,2,1" to 10,
    ).forEach { (input, expected) ->
        "arrangement [$input] $expected" {
            Task.arrangements(Task.parse(sequenceOf(input)).first()).count() shouldBe expected
        }
    }
})