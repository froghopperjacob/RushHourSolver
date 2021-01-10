package main

enum class Direction {
    UP,
    DOWN,
    RIGHT,
    LEFT
}

data class Position(val x: Int, val y: Int)

/*
 * All functions in this car class expect a set board
 */
class Car(val name: String, val size: Int, var facing: Direction, var position: Position) {
    var movedLastTurn: Boolean = false
    var forceMove: Boolean = false
    var board: Board? = null
        set(board) {
            field = board
            setBoardCells()
        }

    /*
     * DANGEROUS FUNCTION
     * Doesn't check if its impeding other cells
     */
    fun setBoardCells() {
        // Set previous cells to null
        for (cell in board!!.getCellsWithCar(this)) {
            cell.car = null
        }

        if (facing == Direction.DOWN) {
            for (i in 0 until size) {
                board!!.cells[position.y + i][position.x].car = this
            }
        } else { // RIGHT
            for (i in 0 until size) {
                board!!.cells[position.y][position.x + i].car = this
            }
        }
    }

    /*
     * DANGEROUS FUNCTION
     * This will move the car and wont check if it can in the first place
     */
    fun move(direction: Direction, number: Int) {
        position = when (direction) {
            Direction.UP -> Position(position.x, position.y - number)
            Direction.DOWN -> Position(position.x, position.y + number)
            Direction.LEFT -> Position(position.x - number, position.y)
            Direction.RIGHT -> Position(position.x + number, position.y)
        }

        setBoardCells()
    }

    /*
     * A safe function that checks if it can do the operation first
     * Returns a success or not
     */
    fun tryMove(direction: Direction, number: Int): Boolean {
        if (canMoveInDirection(direction) >= number) {
            move(direction, number)

            return true
        }

        return false
    }

    /*
     * Checks if the car can move to a specific position without any car blocking it
     */
    fun canMoveTo(newPosition: Position): Boolean {
        if (facing == Direction.DOWN) {
            if (newPosition.x - position.x != 0)
                return false

            if (position.y - newPosition.y > 0) { // Direction is UP
                for (i in newPosition.y..position.y) {
                    if (board!!.cells[i][position.x].car != null && board!!.cells[i][position.x].car != this)
                        return false
                }
            } else { // DOWN
                for (i in position.y..newPosition.y) {
                    if (board!!.cells[i][position.x].car != null && board!!.cells[i][position.x].car != this)
                        return false
                }
            }
        } else { // RIGHT
            if (newPosition.y - position.y != 0)
                return false

            if (position.x - newPosition.x > 0) { // LEFT
                for (i in newPosition.x..position.x) {
                    if (board!!.cells[position.y][i].car != null && board!!.cells[position.y][i].car != this)
                        return false
                }
            } else { // RIGHT
                for (i in position.x..newPosition.x) {
                    if (board!!.cells[position.y][i].car != null && board!!.cells[position.y][i].car != this)
                        return false
                }
            }
        }

        return true
    }

    /*
     * Checks how much the car can move in the directions
     * the car is facing
     * Pair<Left, Right> OR Pair<Up, Down>
     */
    fun canMove(): Pair<Int, Int> {
        return if (facing == Direction.DOWN) {
            Pair(canMoveInDirection(Direction.UP), canMoveInDirection(Direction.DOWN))
        } else { // Right
            Pair(canMoveInDirection(Direction.LEFT), canMoveInDirection(Direction.RIGHT))
        }
    }

    /*
     * Checks if the car can move in a certain direction.
     * The number returned equals to amount it can move in that direction.
     */
    fun canMoveInDirection(direction: Direction): Int {
        // Check that the car is facing the direction way as the given direction
        if (facing == Direction.RIGHT) {
            if (direction == Direction.UP || direction == Direction.DOWN)
                return 0
        } else { // DOWN
            if (direction == Direction.LEFT || direction == Direction.RIGHT)
                return 0
        }

        var counter = 1
        val operation =
            if (direction == Direction.LEFT || direction == Direction.UP)
                -1
            else // RIGHT || DOWN
                1
        val startingPosition =
            if (direction == Direction.LEFT || direction == Direction.UP)
                position
            else if (direction == Direction.RIGHT)
                Position(position.x + (size - 1), position.y)
            else // DOWN
                Position(position.x, position.y + (size - 1))

        if (direction == Direction.UP || direction == Direction.DOWN) {
            while ((startingPosition.y + (operation * counter) < board!!.size) &&
                (startingPosition.y + (operation * counter) >= 0) &&
                board!!.cells[startingPosition.y + (operation * counter)][startingPosition.x].car == null) {
                counter++
            }
        } else { // LEFT || RIGHT
            while ((startingPosition.x + (operation * counter) < board!!.size) &&
                (startingPosition.x + (operation * counter) >= 0) &&
                board!!.cells[startingPosition.y][startingPosition.x + (operation * counter)].car == null) {
                counter++
            }
        }

        return counter - 1
    }

    fun clone(): Car {
        return Car(name, size, facing, position)
    }

    /*
     * The facing directions in the code only accept DOWN and RIGHT
     * This fixes if they gave UP or LEFT
     */
    init {
        facing = when (facing) {
            Direction.UP -> Direction.DOWN
            Direction.LEFT -> Direction.RIGHT
            else -> facing
        }
    }
}