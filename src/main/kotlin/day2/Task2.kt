package day2

import java.io.File

object Task2 {
    fun solve() {
        File(javaClass.getResource("input.txt")!!.toURI()).readLines().asSequence()
            .onEach { print("$it ") }
            .map { gameOf(it) }
            .map {
                it.hands.flatMap { h -> h.cubes.entries }
                    .groupBy({ e -> e.key }, { e -> e.value })
                    .mapValues { e -> e.value.max() }
            }
            .onEach { print("$it ") }
            .map { it.values.reduce { a, b -> a * b } }
            .onEach { println("$it ") }
            .let { println("Sum: ${it.sum()}") }
    }
}