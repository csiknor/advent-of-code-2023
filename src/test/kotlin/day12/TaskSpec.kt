package day12

import day12.Task.arr
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
        R("###.???", listOf(3)) to R("???", listOf()),
        R("###.???", listOf(3,2)) to R("???", listOf(2)),
        R("###.???", listOf(2)) to null,
        R("###.???", listOf(4)) to null,
        R(".###.???", listOf(3)) to R("???", listOf()),
        R(".###.???", listOf(2)) to null,
        R(".###.???", listOf(4)) to null,
        R("#??", listOf(1)) to R("?", listOf()),
        R("#??", listOf(2)) to R("", listOf()),
        R("#??", listOf(3)) to R("", listOf()),
        R("#??", listOf(4)) to null,
        R("#?..?", listOf(1)) to R("?", listOf()),
        R("#?..?", listOf(2)) to R("?", listOf()),
        R("#?..?", listOf(3)) to null,
        R("#..#", listOf(1)) to null,
        R("#..#", listOf(1,1)) to R("", listOf()),
        R("..###..#..?##.#", listOf(3,1)) to R("?##.#", listOf()),
        R("..###..#..?##.#", listOf(2)) to null,
        R("..###..#..?##.#", listOf(4)) to null,
        R("..###..#..?##.#", listOf(3,1)) to R("?##.#", listOf()),
        R("..###..#..?##.#", listOf(3,1,2,1)) to R("?##.#", listOf(2,1)),

        R("#??.###", listOf(3,3)) to R("", listOf()),
        R("#??.###", listOf(3,2)) to null,
        R("#??.###", listOf(3,4)) to null,
        R(".??.###", listOf(3)) to R("??.###", listOf(3)),
        R("#?", listOf()) to null,
        R(".?", listOf()) to R("?", listOf()),

        R(".#?#?#?#?#?#?#?", listOf(1,3,1,6)) to R("", listOf()),
    ).forEach { (r, e) ->
        "reduces from left $r" {
            tryReduceL(r) shouldBe e
        }
    }

    listOf(
        R("???.###", listOf(3)) to R("???", listOf()),
        R("???.###", listOf(2,3)) to R("???", listOf(2)),
        R("???.###", listOf(2)) to null,
        R("???.###", listOf(4)) to null,
        R("???.###.", listOf(3)) to R("???", listOf()),
        R("???.###.", listOf(2)) to null,
        R("???.###.", listOf(4)) to null,
        R("??#", listOf(1)) to R("?", listOf()),
        R("??#", listOf(2)) to R("", listOf()),
        R("??#", listOf(3)) to R("", listOf()),
        R("??#", listOf(4)) to null,
        R("?..?#", listOf(1)) to R("?", listOf()),
        R("?..?#", listOf(2)) to R("?", listOf()),
        R("?..?#", listOf(3)) to null,
        R("#..#", listOf(1)) to null,
        R("#..#", listOf(1,1)) to R("", listOf()),
        R("#.##?..#..###..", listOf(1,3)) to R("#.##?", listOf()),
        R("#.##?..#..###..", listOf(2)) to null,
        R("#.##?..#..###..", listOf(4)) to null,
        R("#.##?..#..###..", listOf(1,3)) to R("#.##?", listOf()),
        R("#.##?..#..###..", listOf(1,2,1,3)) to R("#.##?", listOf(1,2)),
    ).forEach { (r, e) ->
        "reduces from right $r" {
            tryReduceR(r) shouldBe e
        }
    }

    listOf(
        R("..###..#..?##.#", listOf(3,1,2,1)) to R("", listOf()),
        R("..###..#..?##.#", listOf(3,1,3,1)) to R("", listOf()),
        R("..###..#..?##.#", listOf(3,1,4,1)) to null,
        R("..###..#..?##.#", listOf(3,1,1,1)) to null,
        R("..###.??.#..?##.#", listOf(3,1,1,2,1)) to R("??", listOf(1)),

        R("###.?#.#.??.?#", listOf(3,1,1,2,2)) to R("?#.#.??", listOf(1,1,2)),
        R("###.?#.#.??.?#", listOf(3,2)) to R("?#.#.??", listOf()),
        R("..#.###.?#.#.??.?#.##", listOf(1,3,1,2)) to R("?#.#.??", listOf()),

        R("#", emptyList()) to null,
        R("", listOf(1)) to null,
        R(".", emptyList()) to R("", emptyList()),
        R("", emptyList()) to R("", emptyList()),
        R("??", listOf(1,1)) to R("??", listOf(1,1)),

        R("?..?", listOf(1,1)) to R("?..?", listOf(1,1)),
            R("#..?", listOf(1,1)) to R("?", listOf(1)),
                R("#", listOf(1)) to R("", listOf()),
                R(".", listOf(1)) to null,
            R("...?", listOf(1,1)) to R("?", listOf(1,1)),
                R("#", listOf(1,1)) to null,
                R(".", listOf(1,1)) to null,

        R(".#?#?#?#?#?#?#?", listOf(1,3,1,6)) to R("", listOf()),
    ).forEach { (r, e) ->
        "reduces $r" {
            tryReduce(r) shouldBe e
        }
    }

    listOf(
        R("", listOf()) to 1,
        R("#", listOf()) to 0,
        R("#", listOf(1)) to 1,
        R("?", listOf()) to 1,
        R("?", listOf(1)) to 1,
        R("?.", listOf(1)) to 1,
        R(".?", listOf(1)) to 1,
        R(".?.", listOf(1)) to 1,
        R("#?", listOf(1)) to 1,
        R("#?", listOf(2)) to 1,
        R("??", listOf(2)) to 1,
        R("?..?", listOf(1,1)) to 1,
        R("#..?", listOf(1)) to 1,
        R("??.?", listOf(1,1)) to 2,
        R("???.?", listOf(1,1)) to 4,
        R("????.?", listOf(1,1)) to 7,
        R("?#?#?#", listOf(1,1,1)) to 1,
        R(".??..??...?##.", listOf(1,1,3)) to 4,
        R("?#?#?#?#?#?#?#?", listOf(1,3,1,6)) to 1,
        R("????.######..#####.", listOf(1,6,5)) to 4,
        R("?###????????", listOf(3,2,1)) to 10,
        R("???.###????.###????.###????.###????.###", listOf(1,1,3,1,1,3,1,1,3,1,1,3,1,1,3)) to 1,
        R(".??..??...?##.?.??..??...?##.?.??..??...?##.?.??..??...?##.?.??..??...?##.", listOf(1,1,3,1,1,3,1,1,3,1,1,3,1,1,3)) to 16384,
        R("?#?#?#?#?#?#?#???#?#?#?#?#?#?#???#?#?#?#?#?#?#???#?#?#?#?#?#?#???#?#?#?#?#?#?#?", listOf(1,3,1,6,1,3,1,6,1,3,1,6,1,3,1,6,1,3,1,6)) to 1,
        R("????.#...#...?????.#...#...?????.#...#...?????.#...#...?????.#...#...", listOf(4,1,1,4,1,1,4,1,1,4,1,1,4,1,1)) to 16,
        R("????.######..#####.?????.######..#####.?????.######..#####.?????.######..#####.?????.######..#####.", listOf(1,6,5,1,6,5,1,6,5,1,6,5,1,6,5)) to 2500,
        R("?###??????????###??????????###??????????###??????????###????????", listOf(3,2,1,3,2,1,3,2,1,3,2,1,3,2,1)) to 506250,
    ).forEach { (r, e) ->
        "arr $r" {
            arr(r) shouldBe e
        }
    }
})