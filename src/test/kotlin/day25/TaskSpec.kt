package day25

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TaskSpec: StringSpec({
    "solves sample 1" {
        Task.solvePart1("sample.txt") shouldBe 54
    }

    "parses" {
        Task.parse("""
            jqt: rhn xhk
            rhn: frs pzl
            xhk: jqt
        """.trimIndent().split("\n").asSequence()) shouldBe mapOf(
            "jqt" to listOf("rhn", "xhk"),
            "rhn" to listOf("jqt", "frs", "pzl"),
            "frs" to listOf("rhn"),
            "pzl" to listOf("rhn"),
            "xhk" to listOf("jqt"),
        )
    }

    "dijkstra" {
        Task.shortestPathSize(mapOf(
            "A" to setOf("B", "C"),
            "B" to setOf("A", "D"),
            "C" to setOf("A"),
            "D" to setOf("B"),
        ), "A", "D") shouldBe 2
    }
})