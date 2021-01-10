package main

data class Possibility(val board: Board, val parent: Possibility? = null) {
    val possibilities: ArrayList<Possibility> = arrayListOf()
    val isRoot = parent == null
    val queue: Queue<Possibility> = Queue()
    val seenBoards: ArrayList<Board> = arrayListOf()
    var solved = false

    fun addPossibility(board: Board): Possibility {
        val newPossibility = Possibility(board, this)

        possibilities.add(newPossibility)

        return newPossibility
    }

    fun addPossibilitiesToQueue() {
        val root = getRoot()
        val rootQueue = root.queue
        val mainCar = board.getCarWithName("X")

        if (mainCar!!.canMoveTo(Position(board.size - 1, mainCar.position.y))) {
            this.solved = true
            getRoot().solved = true

            return
        }

        for (car in board.cars) {
            val (move1, move2) = car.canMove()
            var movementOptions = board.canMoveCarsFreely
            if (!movementOptions)
                movementOptions = car.forceMove

            if (move1 > 0 && !car.movedLastTurn && movementOptions) {
                for (i in move1 downTo 0) { // We descend because often the answer is the max move
                    val newBoard = board.clone()
                    val newCar = newBoard.getCarWithName(car.name)!!

                    if (newCar.facing == Direction.DOWN) {
                        newCar.move(Direction.UP, i)
                    } else { // RIGHT
                        newCar.move(Direction.LEFT, i)
                    }

                    val (sameBoards, movingCars) = board.differenceCanMove(newBoard, newCar)

                    if (!sameBoards) {
                        newBoard.clearForceMove()
                        newBoard.clearMovementLastTurn()
                        newCar.movedLastTurn = true

                        for (moveCar in movingCars) {
                            moveCar.forceMove = true
                        }

                        /*if (rootQueue.queue.find { possibility -> possibility.board == board } == null &&
                                !root.seenBoards.contains(newBoard)) {
                            rootQueue.enqueue(addPossibility(newBoard))
                            root.seenBoards.add(newBoard)
                        }*/
                        if (!root.seenBoards.contains(newBoard)) {
                            rootQueue.enqueue(addPossibility(newBoard))
                            root.seenBoards.add(newBoard)
                        }
                    }
                }
            }

            if (move2 > 0 && !car.movedLastTurn && movementOptions) {
                for (i in move2 downTo 0) {
                    val newBoard = board.clone()
                    val newCar = newBoard.getCarWithName(car.name)!!

                    if (newCar.facing == Direction.DOWN) {
                        newCar.move(Direction.DOWN, i)
                    } else { // RIGHT
                        newCar.move(Direction.RIGHT, i)
                    }

                    val (sameBoards, movingCars) = board.differenceCanMove(newBoard, newCar)

                    if (!sameBoards) {
                        newBoard.clearForceMove()
                        newBoard.clearMovementLastTurn()
                        newCar.movedLastTurn = true

                        for (moveCar in movingCars) {
                            moveCar.forceMove = true
                        }

                        /*if (rootQueue.queue.find { possibility -> possibility.board == board } == null &&
                                !root.seenBoards.contains(newBoard)) {
                            rootQueue.enqueue(addPossibility(newBoard))
                            root.seenBoards.add(newBoard)
                        }*/
                        if (!root.seenBoards.contains(newBoard)) {
                            rootQueue.enqueue(addPossibility(newBoard))
                            root.seenBoards.add(newBoard)
                        }
                    }
                }
            }
        }
    }

    fun chainToString(): String {
        val chainOfPossibilities: ArrayList<Possibility> = arrayListOf()
        var currentPossibility = this
        val string = StringBuilder()

        while (!currentPossibility.isRoot) {
            chainOfPossibilities.add(currentPossibility)
            currentPossibility = currentPossibility.parent!!
        }

        chainOfPossibilities.add(currentPossibility) // Add parent

        chainOfPossibilities.reverse()

        for (possibility in chainOfPossibilities) {
            string.append(possibility.board.toString() + "\n")
        }

        return string.toString()
    }

    private fun getRoot(): Possibility {
        var currentPossibility = this

        while (!currentPossibility.isRoot) {
            currentPossibility = currentPossibility.parent!!
        }

        return currentPossibility
    }

    fun findSolutions(): ArrayList<Possibility> { // FIX
        if (this.solved && !this.isRoot) {
            return arrayListOf(this)
        }

        val solutions: ArrayList<Possibility> = arrayListOf()
        for (possibility in possibilities) {
            solutions.addAll(possibility.findSolutions())
        }

        return solutions
    }
}