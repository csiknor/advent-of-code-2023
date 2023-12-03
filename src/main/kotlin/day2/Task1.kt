package day2

import java.io.File

enum class Color { RED, GREEN, BLUE }
data class Hand(val cubes: Map<Color, Int>)
data class Game(val id: Int, val hands: Set<Hand>)

// A Game is created from the text input by matching its id and the created hands of the remaining text
fun gameOf(s: String) = """Game (\d+): (.*)""".toRegex().find(s)!!
    .let { Game(it.groupValues[1].toInt(), handsOf(it.groupValues[2])) }

// Hands are created from the text input by splitting it and creating a hand from each split
private fun handsOf(s: String) = s.split(";")
    .map { handOf(it.trim()) }
    .toSet()

// A Hand is created from the text input by splitting it and creating cubes from each split
private fun handOf(s: String) = Hand(s.split(",").associate { cubeOf(it.trim()) })

// A cube is created from the text input by matching the color and the count and creating a Pair out of them
private fun cubeOf(s: String) = """(\d+) (.*)""".toRegex().find(s)!!
    .let { Color.valueOf(it.groupValues[2].uppercase()) to it.groupValues[1].toInt() }

object Task1 {

    // We need to parse the input into a reasonable data type and then filter out those that doesn't match the expected
    // amounts.
    fun solve() {
        File(javaClass.getResource("input.txt")!!.toURI()).readLines().asSequence()
            .onEach { print("$it ") }
            // Parsing the game into a data class Game which contains a set of Hands which consists of a number of
            // colored cubes
            .map { gameOf(it) }
            .onEach { println(if (it.hands.all { h -> h.validHand(12, 13, 14) }) "PASSED" else "FAILED") }
            // Keeping only the games where all hands contain less or equal cubes as defined
            .filter { it.hands.all { h -> h.validHand(12, 13, 14) } }
            // Summing the id of the remaining games
            .sumOf { it.id }
            .let { println("Sum: $it") }
    }

    // A hand is valid if it contains only a predefined number of cubes for each color
    private fun Hand.validHand(red: Int, green: Int, blue: Int) =
        validCube(Color.RED, red)
                && validCube(Color.GREEN, green)
                && validCube(Color.BLUE, blue)

    private fun Hand.validCube(color: Color, count: Int) = cubes[color]?.compareTo(count) != 1
}