package template

import io.kotest.core.annotation.Ignored
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

@Ignored
class TaskSpec: StringSpec({
    "solves sample 1" {
        Task.solvePart1("sample.txt") shouldBe TODO()
    }

    "solves sample 2" {
        Task.solvePart2("sample.txt") shouldBe TODO()
    }
})