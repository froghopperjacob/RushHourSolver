package main

import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

fun numberOfSteps(possibility: Possibility): Int {
    var currentPossibility = possibility
    var counter = 0

    while (!currentPossibility.isRoot) {
        currentPossibility = currentPossibility.parent!!
        counter++
    }

    return counter
}

fun solve(board: Board): Possibility? {
    val root = Possibility(board)
    root.addPossibilitiesToQueue()

    while (!root.queue.isEmpty()) {
        println("Queue length : ${root.queue.size}")

        val possibility = root.queue.dequeue()!!

        println("__-__-__-__-__")
        println(possibility.chainToString())
        println("__-__-__-__-__")

        //println(possibility.board)

        if (possibility.solved || root.solved)
            break

        possibility.addPossibilitiesToQueue()
    }

    val solutions = root.findSolutions()

    if (solutions.size == 0)
        return null

    var highestStep = numberOfSteps(solutions[0])
    var highestStepSolution = solutions[0]
    for (solution in solutions) {
        val stepCount = numberOfSteps(solutions[0])
        if (stepCount > highestStep) {
            highestStep = stepCount
            highestStepSolution = solution
        }
    }

    return highestStepSolution
}

@ExperimentalTime
fun main() {
    val board = Board()
    val mainCar = Car("X", 2, Direction.RIGHT, Position(0, 2))
    /*val cars: Array<Car> = arrayOf( // Beginner
        Car("O", 3, Direction.DOWN, Position(2, 0)),
        Car("Q", 3, Direction.RIGHT, Position(1, 3)),
        Car("A", 2, Direction.DOWN, Position(0, 3)),
        Car("B", 2, Direction.DOWN, Position(3, 4)),
        Car("P", 3, Direction.DOWN, Position(5, 2)),
        Car("C", 2, Direction.RIGHT, Position(4, 5))
    )*/
    /*val cars: Array<Car> = arrayOf( // Beginner
        Car("A", 2, Direction.DOWN, Position(0, 3)),
        Car("P", 3, Direction.DOWN, Position(3, 1)),
        Car("Q", 3, Direction.RIGHT, Position(3, 4)),
        Car("C", 2, Direction.RIGHT, Position(4, 5)),
        Car("B", 2, Direction.RIGHT, Position(4, 3)),
        Car("O", 3, Direction.DOWN, Position(5, 0))
    )*/
    val cars: Array<Car> = arrayOf( // Intermediate
        Car("A", 2, Direction.DOWN, Position(2, 0)),
        Car("B", 2, Direction.DOWN, Position(3, 0)),
        Car("C", 2, Direction.DOWN, Position(4, 1)),
        Car("O", 3, Direction.DOWN, Position(5, 0)),
        Car("F", 2, Direction.DOWN, Position(1, 3)),
        Car("D", 2, Direction.DOWN, Position(2, 2)),
        Car("E", 2, Direction.DOWN, Position(3, 2)),
        Car("G", 2, Direction.RIGHT, Position(4, 3)),
        Car("H", 2, Direction.RIGHT, Position(2, 4)),
        Car("P", 3, Direction.RIGHT, Position(3, 5))
    )
    /*val cars: Array<Car> = arrayOf( // Easy
        Car("T", 3, Direction.DOWN, Position(3, 0)),
        Car("Z", 2, Direction.DOWN, Position(2, 0)),
        Car("N", 2, Direction.RIGHT, Position(3, 3)),
        Car("M", 2, Direction.DOWN, Position(2, 3)),
        Car("A",3, Direction.RIGHT, Position(3, 5))
    )*/

    board.setCarOnBoard(mainCar)
    board.fillWithCars(cars)

    println(board)

    val elapsed: Duration = measureTime {
        val solution = solve(board)

        if (solution == null) {
            println("Unsolvable")
        } else {
            println(solution.chainToString())
        }
    }

    println("Time elapsed: $elapsed")
}