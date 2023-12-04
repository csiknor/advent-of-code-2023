package day4

import java.io.File

object Task2 {

    // We parse each line of the input into a Card, we generate the copies based on how many we won for each card, and
    // finally count.
    fun solve(filename: String) =
        File(javaClass.getResource(filename)!!.toURI()).useLines { line -> process(line) }

    fun process(lines: Sequence<String>) = lines
        .parseCards()
        .generateCopies()
        .count()
        .also { println("Sum: $it") }

}

fun Sequence<String>.parseCards(): Sequence<Card> = map { Task1.cardOf(it) }

val emptyCard = Card(0, emptyList(), emptyList())

// Generating copies of cards won is calculated using a running list of copies to generate. For the current card we
// store all the wins from previous cards decreased by one, plus the win of the previous card as many times as many wins
// we accumulated on that card. We also remove any win if it ran out. E.g.:
//           match  previous -> decreased + wins * prev won -> ran out removed
//   empty,  0:     []
//   card 1, 0:     [] -> []+[0] -> []
//   card 2, 1:     [] -> []+[0] -> []
//   card 3, 1:     [] -> []+[1] -> [1]
//   card 4, 0:     [1] -> [0]+[1,1] -> [1,1]
// or
//   empty,  0:     []
//   card 1, 0:     [] -> []+[0] -> []
//   card 2, 2:     [] -> []+[0] -> []
//   card 3, 1:     [] -> []+[2] -> [2]
//   card 4, 0:     [2] -> [1]+[1,1] -> [1,1,1]
// Finally we generate each card as many times we won it.
fun Sequence<Card>.generateCopies() = this
    .runningFold(emptyCard to emptyList<Int>()) { (prev, wins), card ->
        card to wins
            .map { it - 1 }
            .plus(List(1 + wins.size) { _ -> prev.matchingCount() })
            .filter { it != 0 }
    }
    .drop(1)
    .onEach { (card, wins) -> println("$card -> ${wins.size} ") }
    .flatMap { (card, wins) -> List(wins.size + 1) { _ -> card } }