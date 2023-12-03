package day2

import java.io.File

object Task2 {

    // We need to parse the input into a reasonable data type, then for each game we group all hands by color and find
    // the maximum count per color. Once we have the max count per color for each game, we just multiply the counts
    // together per hand and sum them. E.g.:
    //   - Game 4: [(green,1), (red,3), (blue,6)]; [(green,3), (red,6)]; [(green,3), (blue,15), (red,14)]
    //   - Game 4: (green,1), (red,3), (blue,6), (green,3), (red,6), (green,3), (blue,15), (red,14)
    //   - Game 4: (green,[1,3,3]); (red,[3,6,14]); (blue,[6,15])
    //   - Game 4: (green,3); (red,14); (blue,15)
    //   - Game 4: 630
    fun solve() {
        File(javaClass.getResource("input.txt")!!.toURI()).readLines().asSequence()
            .onEach { print("$it ") }
            // Parsing the game into a data class Game which contains a set of Hands which consists of a number of
            // colored cubes
            .map { gameOf(it) }
            // Calculating the max count per color for each game
            .map {
                // List all cubes in all the hands of the game
                it.hands.flatMap { h -> h.cubes.entries }
                    // Group them by color so that we'll have a list of counts for each color
                    .groupBy({ e -> e.key }, { e -> e.value })
                    // We keep the max count for each color
                    .mapValues { e -> e.value.max() }
            }
            .onEach { print("$it ") }
            // Calculate the product of the max counts
            .map { it.values.reduce { a, b -> a * b } }
            .onEach { println("$it ") }
            // Summing all up
            .let { println("Sum: ${it.sum()}") }
    }
}