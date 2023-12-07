package day5

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Task2Spec: StringSpec({

    listOf(
        2L..3L to Triple(null, null, 10L..20L),
        5L..10L to Triple(null, 10L..10L, 11L..20L),
        5L..15L to Triple(null, 10L..15L, 16L..20L),
        10L..15L to Triple(null, 10L..15L, 16L..20L),
        12L..15L to Triple(10L..11L, 12L..15L, 16L..20L),
        12L..20L to Triple(10L..11L, 12L..20L, null),
        12L..23L to Triple(10L..11L, 12L..20L, null),
        20L..23L to Triple(10L..19L, 20L..20L, null),
        22L..23L to Triple(10L..20L, null, null),

        8L..23L to Triple(null, 10L..20L, null),
        10L..23L to Triple(null, 10L..20L, null),
    ).forEach { (range, overlaps) ->
        "Range overlaps with $range" {
            (10L..20L).overlaps(range) shouldBe overlaps
        }
    }

    "solves sample" {
        Task2.solve("sample.txt") shouldBe 46
    }
})
