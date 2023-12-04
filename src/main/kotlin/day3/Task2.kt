package day3

import java.io.File

data class Gear(val rowNum: Int, val loc: Int, val nums: List<Pair<Int, IntRange>>) {
    fun ratio() = nums.component1().first * nums.component2().first
}

object Task2 {

    // We pretty much doing the same as in Task1, except identifying gears not part numbers. Gears can span three lines
    // so this time we consider three consecutive rows (previous, current and next) at the same time. Using the numbers'
    // and symbols' location we check if the current row's symbols adjacent to previous, current or next row's numbers,
    // and if the next row's symbols are adjacent to current or next row's numbers. With that we construct the gear.
    // Since we can find the same gear multiple times, we remove any duplicate. Finally, we sum the numbers.
    fun solve() {
        File(javaClass.getResource("input.txt")!!.toURI()).useLines { line -> process(line) }
    }

    private fun process(lines: Sequence<String>) = lines
        // We parse the numbers and the symbols in the row with the row index
        .mapIndexed { index, line -> Row(index, line, numbersIn(line), symbolsIn(line)) }
        // We combine three consecutive rows together
        .windowed(3)
        // We extract the gears in the current and next rows
        // Note: the previous row is already checked in a previous iteration when it was the next row
        .flatMap { (prev, curr, next) ->
            // Gears in the current row based on numbers above, next to or below
            gears(curr.index, curr.symbols, prev.numbers + curr.numbers + next.numbers)
                // Gears in the next row based on numbers above or next to
                .plus(gears(next.index, next.symbols, curr.numbers + next.numbers))

        }
        // Removes duplicates found in current and next rows
        .distinct()
        // Removes gears found multiple times with different numbers around them
        .groupBy { it.rowNum to it.loc }.filterValues { it.size == 1 }.flatMap { it.value }
        // Removes gears with more than two numbers around them
        .filter { it.nums.size == 2 }
        .onEach { println("$it ") }
        // Calculating the sum of the gear ratio
        .sumOf { it.ratio() }
        .let { println("Sum: $it") }

    // An asterisk (*) symbol is a gear if its location is adjacent to multiple numbers' location
    private fun gears(index: Int, symbols: List<Pair<String, Int>>, numbers: List<Pair<Int, IntRange>>) =
        symbols.filter { it.first == "*" }
            .map { (_, loc) -> loc to numbers.filter { (_, range) -> extendedRange(range).contains(loc) } }
            // We don't want to limit it to 2 here, because that would remove symbols with 3 adjacent numbers identified
            // in consecutive rows, but we need them to identify duplicates
            .filter { (_, nums) -> nums.size > 1 }
            .map { (loc, nums) -> Gear(index, loc, nums) }

}