package day19

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TaskSpec : StringSpec({
    "solves sample 1" {
        Task.solvePart1("sample.txt") shouldBe 19114
    }

    "solves sample 2" {
        Task.solvePart2("sample.txt") shouldBe 167409079868000L
    }

    listOf(
        "{x=787,m=2655,a=1222,s=2876}" to Part(mapOf('x' to 787, 'm' to 2655, 'a' to 1222, 's' to 2876)),
    ).forEach { (input, expected) ->
        "parses part" {
            input.toPart() shouldBe expected
        }
    }

    listOf(
        "px{a<2006:qkq,m>2090:A,rfg}" to Rule("px",
            listOf(
                Condition('a', false, 2006, "qkq"),
                Condition('m', true, 2090, "A"),
            ), "rfg"
        )
    ).forEach { (input, expected) ->
        "parses rule" {
            input.toRule() shouldBe expected
        }
    }

    listOf(
        (Condition('a', false, 2006, "qkq") to Part(mapOf('a' to 1222))) to true,
        (Condition('a', false, 2006, "qkq") to Part(mapOf('x' to 787, 'm' to 2655, 'a' to 1222, 's' to 2876))) to true,
        (Condition('m', true, 2090, "A") to Part(mapOf('x' to 787, 'm' to 2655, 'a' to 1222, 's' to 2876))) to true,
        (Condition('a', true, 2090, "A") to Part(mapOf('x' to 787, 'm' to 2655, 'a' to 1222, 's' to 2876))) to false,
    ).forEach { (input, expected) ->
        input.let { (condition, part) ->
            "$part matches condition $condition" {
                condition.matches(part.rating) shouldBe expected
            }
        }
    }

    listOf(
        "px{a<2006:qkq,m>2090:A,rfg}" to "px{a>2005:qkq,m<2091:A,rfg}",
        "pv{a>1716:R,A}" to "pv{a<1717:R,A}"
    ).forEach { (input, expected) ->
        "negates $input" {
            input.toRule()?.negate() shouldBe expected.toRule()
        }
    }
})