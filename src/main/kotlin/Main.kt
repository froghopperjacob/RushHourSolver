import java.util.*
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

/**
 * A solver for the Rush Hour game.
 *
 * @author Jacob Selbo
 * @version 1.0
 * @since 1/12/21
 */

/**
 * Solves a given board
 *
 * @param board A board
 *
 * @return The deepest solved node
 */
fun solve(board: Board): Node? {
    val rootNode = Node(board)
    val rootQueue = rootNode.queue!!
    var solution: Node? = null

    rootNode.addNodesToQueue()

    while (!rootQueue.isEmpty()) {
        val node = rootQueue.dequeue()!!

        node.addNodesToQueue()

        if (node.solved) {
            solution = node
            break
        }
    }

    if (rootNode.solved) // The root is solved
        return rootNode

    return solution
}

/**
 * A repeating function for console interactions
 */
@ExperimentalTime
fun console() {
    val scanner = Scanner(System.`in`)

    println("Welcome to the Rush Hour game solver. It can solve any given Rush hour board, with any size.")
    println("(Solutions can take a while to process)")
    print("What is the name of the board? ")

    val name = scanner.nextLine()

    print("How big is the board? ")

    val size = scanner.nextInt()

    if (size < 3) {
        println("The board isn't big enough")
        return
    }

    val stringCells: Array<Array<String>> = Array(size) { Array(size) { "" } }

    println("Input board: ")
    for (y in 0 until size) {
        for (x in 0 until size) {
            val cell = scanner.next()
            stringCells[y][x] = cell
        }
    }

    val board = Board.from(name, size, stringCells)

    val elapsed: Duration = measureTime {
        val solution = solve(board)

        if (solution == null) {
            println("Unsolvable")
        } else {
            println("Solution :")
            println(solution.nodeToTreeString())
        }
    }

    println("Time elapsed: $elapsed")

    return
}

@ExperimentalTime
fun main() {
    val scanner = Scanner(System.`in`)

    while (true) {
        console()
        println("Would you like to continue? (yes to continue)")

        if (scanner.next().toLowerCase() != "yes")
            break
    }
}