fun main(args: Array<String>) {
    val day = args.getOrNull(1)?.toInt() ?: 2

    when (day) {
        1 -> {
            day1.Task1.solve()
            day1.Task2.solve()
        }

        2 -> {
            day2.Task1.solve()
            day2.Task2.solve()
        }
    }
}