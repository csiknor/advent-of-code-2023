package day2

import java.io.File

enum class Color { RED, GREEN, BLUE }
data class Hand(val cubes: Map<Color, Int>)
data class Game(val id: Int, val hands: Set<Hand>)

fun gameOf(s: String) = """Game (\d+): (.*)""".toRegex().find(s)!!
    .let { Game(it.groupValues[1].toInt(), handsOf(it.groupValues[2])) }

private fun handsOf(s: String) = s.split(";")
    .map { handOf(it.trim()) }
    .toSet()

private fun handOf(s: String) = Hand(s.split(",").associate { cubeOf(it.trim()) })

private fun cubeOf(s: String) = """(\d+) (.*)""".toRegex().find(s)!!
    .let { Color.valueOf(it.groupValues[2].uppercase()) to it.groupValues[1].toInt() }

object Task1 {

    fun solve() {
        File(javaClass.getResource("input.txt")!!.toURI()).readLines().asSequence()
            .onEach { print("$it ") }
            .map { gameOf(it) }
            .onEach { println(if (it.hands.all { h -> h.validHand(12, 13, 14) }) "PASSED" else "FAILED") }
            .filter { it.hands.all { h -> h.validHand(12, 13, 14) } }
            .sumOf { it.id }
            .let { println("Sum: $it") }
    }

    private fun Hand.validHand(red: Int, green: Int, blue: Int) =
        validCube(Color.RED, red)
                && validCube(Color.GREEN, green)
                && validCube(Color.BLUE, blue)

    private fun Hand.validCube(color: Color, count: Int) = cubes[color]?.compareTo(count) != 1
}