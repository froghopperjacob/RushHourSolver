/**
 * A node that contains a board, parent, and node children.
 * This is used to create a tree of possibilities for the cars
 *
 * @param board The board
 * @param parent The parent of this node
 * @param movedCar The car moved last Node
 *
 * @author Jacob Selbo
 * @version 1.0
 * @since 1/12/21
 */
class Node(val board: Board, val parent: Node? = null, val movedCar: Car? = null) {
    val nodes: ArrayList<Node> = arrayListOf()
    val isRoot = parent == null
    var solved = false
    var rootNode = getRoot()
    // These are null because they are only used in the root
    var queue: Queue<Node>? = null
    var processedBoards: ArrayList<Board>? = null

    /**
     * Creates a new node, and adds it to the children in this node
     *
     * @return The new node
     */
    fun addNode(board: Board, movedCar: Car): Node {
        val node = Node(board, this, movedCar)

        nodes.add(node)

        return node
    }

    /**
     * Adds nodes to the queue to be processed.
     * We use a queue system to avoid recursive functions
     */
    fun addNodesToQueue() {
        val rootQueue = rootNode.queue!!
        val rootProcessedBoards = rootNode.processedBoards!!
        val checkCar = board.getCarWithName("X")!!

        // Check if this node is solved
        if (checkCar.canMoveTo(checkCar.position.move(newX = board.size - 1))) {
            solved = true
            return
        }

        for (car in board.cars) {
            if (car != movedCar) {
                val (move1, move2) = car.amountOfMovement()
                var doingMove1 = true
                var counter = move1

                if (counter == 0) {
                    counter = move2
                    doingMove1 = false
                }

                while ((doingMove1 && (counter > 0)) || (!doingMove1 && (counter > 0))) {
                    val newBoard = board.clone()
                    val newCar = newBoard.getCarWithName(car.name)!!

                    if (doingMove1) {
                        if (newCar.facing == Facing.HORIZONTAL) {
                            newCar.move(Direction.LEFT, counter)
                        } else { // VERTICAL
                            newCar.move(Direction.UP, counter)
                        }
                    } else {
                        if (newCar.facing == Facing.HORIZONTAL) {
                            newCar.move(Direction.RIGHT, counter)
                        } else { // VERTICAL
                            newCar.move(Direction.DOWN, counter)
                        }
                    }

                    if (!rootProcessedBoards.contains(newBoard)) {
                        rootQueue.enqueue(addNode(newBoard, newCar))
                        rootProcessedBoards.add(newBoard)
                    }

                    if (counter == 0 && doingMove1) {
                        doingMove1 = false
                        counter = move2
                    } else {
                        counter--
                    }
                }
            }
        }
    }

    /**
     * Returns the chain from the root to this node
     *
     * @return The formatted string
     */
    fun nodeToTreeString(): String {
        val nodes: ArrayList<Node> = arrayListOf()
        var currentNode = this
        val string = StringBuilder()

        while (!currentNode.isRoot) {
            nodes.add(currentNode)
            currentNode = currentNode.parent!!
        }

        nodes.add(currentNode) // Add root
        nodes.reverse()

        for (node in nodes) {
            string.append(node.board.toString() + "\n")
        }

        return string.toString()
    }

    /**
     * Gets the root of the tree
     *
     * @return The root of the tree
     */
    private fun getRoot(): Node {
        var currentNode = this

        while (!currentNode.isRoot)
            currentNode = currentNode.parent!!

        return currentNode
    }

    init {
        if (isRoot) {
            queue = Queue()
            processedBoards = arrayListOf()
        }
    }
}