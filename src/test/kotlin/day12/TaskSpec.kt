package day12

import day12.Task.arrangeCount
import day12.Task.tryReduce
import day12.Task.tryReduceL
import day12.Task.tryReduceR
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TaskSpec: StringSpec({
    "solves sample 1" {
        Task.solvePart1("sample.txt") shouldBe 21
    }

    "solves sample 2" {
        Task.solvePart2("sample.txt") shouldBe 525152
    }

    listOf(
        Row("###.???", listOf(3)) to Row("???", listOf()),
        Row("###.???", listOf(3,2)) to Row("???", listOf(2)),
        Row("###.???", listOf(2)) to null,
        Row("###.???", listOf(4)) to null,
        Row(".###.???", listOf(3)) to Row("???", listOf()),
        Row(".###.???", listOf(2)) to null,
        Row(".###.???", listOf(4)) to null,
        Row("#??", listOf(1)) to Row("?", listOf()),
        Row("#??", listOf(2)) to Row("", listOf()),
        Row("#??", listOf(3)) to Row("", listOf()),
        Row("#??", listOf(4)) to null,
        Row("#?..?", listOf(1)) to Row("?", listOf()),
        Row("#?..?", listOf(2)) to Row("?", listOf()),
        Row("#?..?", listOf(3)) to null,
        Row("#..#", listOf(1)) to null,
        Row("#..#", listOf(1,1)) to Row("", listOf()),
        Row("..###..#..?##.#", listOf(3,1)) to Row("?##.#", listOf()),
        Row("..###..#..?##.#", listOf(2)) to null,
        Row("..###..#..?##.#", listOf(4)) to null,
        Row("..###..#..?##.#", listOf(3,1)) to Row("?##.#", listOf()),
        Row("..###..#..?##.#", listOf(3,1,2,1)) to Row("?##.#", listOf(2,1)),

        Row("#??.###", listOf(3,3)) to Row("", listOf()),
        Row("#??.###", listOf(3,2)) to null,
        Row("#??.###", listOf(3,4)) to null,
        Row(".??.###", listOf(3)) to Row("??.###", listOf(3)),
        Row("#?", listOf()) to null,
        Row(".?", listOf()) to Row("?", listOf()),

        Row(".#?#?#?#?#?#?#?", listOf(1,3,1,6)) to Row("", listOf()),
    ).forEach { (r, e) ->
        "reduces from left $r" {
            tryReduceL(r) shouldBe e
        }
    }

    listOf(
        Row("???.###", listOf(3)) to Row("???", listOf()),
        Row("???.###", listOf(2,3)) to Row("???", listOf(2)),
        Row("???.###", listOf(2)) to null,
        Row("???.###", listOf(4)) to null,
        Row("???.###.", listOf(3)) to Row("???", listOf()),
        Row("???.###.", listOf(2)) to null,
        Row("???.###.", listOf(4)) to null,
        Row("??#", listOf(1)) to Row("?", listOf()),
        Row("??#", listOf(2)) to Row("", listOf()),
        Row("??#", listOf(3)) to Row("", listOf()),
        Row("??#", listOf(4)) to null,
        Row("?..?#", listOf(1)) to Row("?", listOf()),
        Row("?..?#", listOf(2)) to Row("?", listOf()),
        Row("?..?#", listOf(3)) to null,
        Row("#..#", listOf(1)) to null,
        Row("#..#", listOf(1,1)) to Row("", listOf()),
        Row("#.##?..#..###..", listOf(1,3)) to Row("#.##?", listOf()),
        Row("#.##?..#..###..", listOf(2)) to null,
        Row("#.##?..#..###..", listOf(4)) to null,
        Row("#.##?..#..###..", listOf(1,3)) to Row("#.##?", listOf()),
        Row("#.##?..#..###..", listOf(1,2,1,3)) to Row("#.##?", listOf(1,2)),
    ).forEach { (r, e) ->
        "reduces from right $r" {
            tryReduceR(r) shouldBe e
        }
    }

    listOf(
        Row("..###..#..?##.#", listOf(3,1,2,1)) to Row("", listOf()),
        Row("..###..#..?##.#", listOf(3,1,3,1)) to Row("", listOf()),
        Row("..###..#..?##.#", listOf(3,1,4,1)) to null,
        Row("..###..#..?##.#", listOf(3,1,1,1)) to null,
        Row("..###.??.#..?##.#", listOf(3,1,1,2,1)) to Row("??", listOf(1)),

        Row("###.?#.#.??.?#", listOf(3,1,1,2,2)) to Row("?#.#.??", listOf(1,1,2)),
        Row("###.?#.#.??.?#", listOf(3,2)) to Row("?#.#.??", listOf()),
        Row("..#.###.?#.#.??.?#.##", listOf(1,3,1,2)) to Row("?#.#.??", listOf()),

        Row("#", emptyList()) to null,
        Row("", listOf(1)) to null,
        Row(".", emptyList()) to Row("", emptyList()),
        Row("", emptyList()) to Row("", emptyList()),
        Row("??", listOf(1,1)) to Row("??", listOf(1,1)),

        Row("?..?", listOf(1,1)) to Row("?..?", listOf(1,1)),
            Row("#..?", listOf(1,1)) to Row("?", listOf(1)),
                Row("#", listOf(1)) to Row("", listOf()),
                Row(".", listOf(1)) to null,
            Row("...?", listOf(1,1)) to Row("?", listOf(1,1)),
                Row("#", listOf(1,1)) to null,
                Row(".", listOf(1,1)) to null,

        Row(".#?#?#?#?#?#?#?", listOf(1,3,1,6)) to Row("", listOf()),
    ).forEach { (r, e) ->
        "reduces $r" {
            tryReduce(r) shouldBe e
        }
    }

    listOf(
        Row("", listOf()) to 1,
        Row("#", listOf()) to 0,
        Row("#", listOf(1)) to 1,
        Row("?", listOf()) to 1,
        Row("?", listOf(1)) to 1,
        Row("?.", listOf(1)) to 1,
        Row(".?", listOf(1)) to 1,
        Row(".?.", listOf(1)) to 1,
        Row("#?", listOf(1)) to 1,
        Row("#?", listOf(2)) to 1,
        Row("??", listOf(2)) to 1,
        Row("?..?", listOf(1,1)) to 1,
        Row("#..?", listOf(1)) to 1,
        Row("??.?", listOf(1,1)) to 2,
        Row("???.?", listOf(1,1)) to 4,
        Row("????.?", listOf(1,1)) to 7,
        Row("?#?#?#", listOf(1,1,1)) to 1,
        Row(".??..??...?##.", listOf(1,1,3)) to 4,
        Row("?#?#?#?#?#?#?#?", listOf(1,3,1,6)) to 1,
        Row("????.######..#####.", listOf(1,6,5)) to 4,
        Row("?###????????", listOf(3,2,1)) to 10,
        Row("???.###????.###????.###????.###????.###", listOf(1,1,3,1,1,3,1,1,3,1,1,3,1,1,3)) to 1,
        Row(".??..??...?##.?.??..??...?##.?.??..??...?##.?.??..??...?##.?.??..??...?##.", listOf(1,1,3,1,1,3,1,1,3,1,1,3,1,1,3)) to 16384,
        Row("?#?#?#?#?#?#?#???#?#?#?#?#?#?#???#?#?#?#?#?#?#???#?#?#?#?#?#?#???#?#?#?#?#?#?#?", listOf(1,3,1,6,1,3,1,6,1,3,1,6,1,3,1,6,1,3,1,6)) to 1,
        Row("????.#...#...?????.#...#...?????.#...#...?????.#...#...?????.#...#...", listOf(4,1,1,4,1,1,4,1,1,4,1,1,4,1,1)) to 16,
        Row("????.######..#####.?????.######..#####.?????.######..#####.?????.######..#####.?????.######..#####.", listOf(1,6,5,1,6,5,1,6,5,1,6,5,1,6,5)) to 2500,
        Row("?###??????????###??????????###??????????###??????????###????????", listOf(3,2,1,3,2,1,3,2,1,3,2,1,3,2,1)) to 506250,
    ).forEach { (r, e) ->
        "arr $r" {
            arrangeCount(r) shouldBe e
        }
    }
})