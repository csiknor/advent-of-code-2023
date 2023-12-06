import java.time.LocalDate

fun main(args: Array<String>) {
    val day = args.getOrNull(1)?.toInt() ?: LocalDate.now().dayOfMonth

    when (day) {
        1 -> {
            day1.Task1.solve()
            day1.Task2.solve()
        }

        2 -> {
            day2.Task1.solve()
            day2.Task2.solve()
        }

        3 -> {
            day3.Task1.solve()
            day3.Task2.solve()
        }

        4 -> {
            day4.Task1.solve("input.txt")
            day4.Task2.solve("input.txt")
        }

        5 -> {
            day5.Task1.solve("input.txt")
            day5.Task2.solve("input.txt")
        }

        6 -> {
            day6.Task1.solve("input.txt")
            day6.Task2.solve("input.txt")
        }

        else -> println("No solution for day $day!")
    }
}