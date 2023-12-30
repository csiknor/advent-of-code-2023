import java.time.LocalDate

private const val DEFAULT_INPUT_FILENAME = "input.txt"

fun main(args: Array<String>) {
    val day = args.getOrNull(0)?.toInt() ?: LocalDate.now().dayOfMonth

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
            day4.Task1.solve(DEFAULT_INPUT_FILENAME)
            day4.Task2.solve(DEFAULT_INPUT_FILENAME)
        }

        5 -> {
            day5.Task1.solve(DEFAULT_INPUT_FILENAME)
            day5.Task2.solve(DEFAULT_INPUT_FILENAME)
        }

        6 -> {
            day6.Task1.solve(DEFAULT_INPUT_FILENAME)
            day6.Task2.solve(DEFAULT_INPUT_FILENAME)
        }

        7 -> {
            println("Solution for day $day, task 1: " + day7.Task.solvePart1(DEFAULT_INPUT_FILENAME))
            println("Solution for day $day, task 2: " + day7.Task.solvePart2(DEFAULT_INPUT_FILENAME))
        }

        8 -> {
            println("Solution for day $day, task 1: " + day8.Task.solvePart1(DEFAULT_INPUT_FILENAME))
            println("Solution for day $day, task 2: " + day8.Task.solvePart2(DEFAULT_INPUT_FILENAME))
        }

        9 -> {
            println("Solution for day $day, task 1: " + day9.Task.solvePart1(DEFAULT_INPUT_FILENAME))
            println("Solution for day $day, task 2: " + day9.Task.solvePart2(DEFAULT_INPUT_FILENAME))
        }

        10 -> {
            println("Solution for day $day, task 1: " + day10.Task.solvePart1(DEFAULT_INPUT_FILENAME))
            println("Solution for day $day, task 2: " + day10.Task.solvePart2(DEFAULT_INPUT_FILENAME))
        }
        11 -> {
            println("Solution for day $day, task 1: " + day11.Task.solvePart1(DEFAULT_INPUT_FILENAME))
            println("Solution for day $day, task 2: " + day11.Task.solvePart2(DEFAULT_INPUT_FILENAME))
        }
        12 -> {
            println("Solution for day $day, task 1: " + day12.Task.solvePart1(DEFAULT_INPUT_FILENAME))
            println("Solution for day $day, task 2: " + day12.Task.solvePart2(DEFAULT_INPUT_FILENAME))
        }
        13 -> {
            println("Solution for day $day, task 1: " + day13.Task.solvePart1(DEFAULT_INPUT_FILENAME))
            println("Solution for day $day, task 2: " + day13.Task.solvePart2(DEFAULT_INPUT_FILENAME))
        }

        else -> println("No solution for day $day!")
    }
}