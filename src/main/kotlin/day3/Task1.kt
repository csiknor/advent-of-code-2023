package day3

import java.io.File

const val ANSI_RESET = "\u001B[0m"
const val ANSI_RED = "\u001B[31m"
const val ANSI_GREEN = "\u001B[32m"

data class Row(val index: Int, val source: String, val numbers: List<Pair<Int, IntRange>>, val symbols: List<Pair<String, Int>>)

data class PartNumber(val num: Int, val rowNum: Int, val range: IntRange)

object Task1 {

    // We parse the input text into a reasonable data format containing numbers and symbols with their respective
    // location (as well as the index and original text for logging). Then we consider two consecutive rows (previous
    // and current) at the same time to find if a symbol is adjacent to a number. Using the numbers' and symbols'
    // location we check if the previous row's numbers adjacent to the current row's symbols and if the current row's
    // numbers are adjacent to the current or previous row's symbols. With that we construct the part number. Given we
    // might find the same part number twice if it had a symbol above and below or next to and below, let alone the same
    // number multiple times in any row, we have to deduplicate using row index and the location of the number. Finally,
    // we sum the numbers.
    fun solve() {
        File(javaClass.getResource("input.txt")!!.toURI()).useLines { line -> process(line) }
    }

    private fun process(lines: Sequence<String>) = lines
        // We parse the numbers and the symbols in the row with the row index
        .mapIndexed { index, line -> Row(index, line, numbersIn(line), symbolsIn(line)) }
        // We pair two consecutive rows together
        .windowed(2)
        // We extract the part numbers in each row
        .flatMap { (prevRow, currRow) ->
            // part numbers in the previous row based on symbols below them (i.e.: in the current row)
            partNumbers(prevRow.index, prevRow.numbers, currRow.symbols)
                .also { printColorized(prevRow.source, it, ANSI_RED) }
                .plus(
                    // part numbers in the current row based on symbols next to (i.e.: in the current row) or above
                    // (i.e.: in the previous row)
                    partNumbers(currRow.index, currRow.numbers, currRow.symbols + prevRow.symbols)
                        .also { printColorized(currRow.source, it, ANSI_GREEN) }
                )
        }
        // De-duplicating, because part numbers might be duplicated because we can each row twice
        .distinct()
        // Calculating the sum of the numbers
        .sumOf { (num, _) -> num }
        .let { println("\nSum: $it") }

    private fun printColorized(source: String, numbers: List<PartNumber>, color: String) {
        // Colorization impacts the length of the text, so moving backwards so that numbers' range location is correct
        println(numbers.sortedByDescending { it.range.first }
            .fold(source) { acc, partNum -> colorize(acc, partNum.range, color) })
    }

    private fun colorize(acc: String, range: IntRange, color: String) =
        // Colorization is done by inserting ANSI control characters at the start and end of each range.
        // Inserting backwards so that range location is not impacted
        acc.replaceRange(range.last + 1, range.last + 1, ANSI_RESET)
            .replaceRange(range.first, range.first, color)

    // A number is a part number if its location is adjacent to any of the symbols location
    private fun partNumbers(index: Int, numbers: List<Pair<Int, IntRange>>, symbols: List<Pair<String, Int>>) =
        numbers.filter { (_, range) -> symbols.any { (_, symLoc) -> extendedRange(range).contains(symLoc) } }
            .map { (num, range) -> PartNumber(num, index, range) }

}


// In order to support diagonally adjacent symbols the range is extended by one in both directions
fun extendedRange(range: IntRange) = (range.first - 1).rangeTo(range.last + 1)

// Numbers are extracted from the text using a regular expression matching one or more digits
fun numbersIn(s: String) = """(\d+)""".toRegex().findAll(s).map { m -> m.value.toInt() to m.range }.toList()

// Symbols are extracted from the text using a regular expression matching any single character but word characters
// or the dot.
fun symbolsIn(s: String) = """([^\w.])""".toRegex().findAll(s).map { m -> m.value to m.range.first }.toList()
