/**
 * A car that can be placed on a board.
 *
 * @param name The name of the car
 * @param size The size of the car
 * @param facing The way the car is facing
 * @param position The position of the car
 *
 * @author Jacob Selbo
 * @version 1.0
 * @since 1/12/21
 */
class Car(val name: String, val size: Int, val facing: Facing, var position: Position) {
    var board: Board? = null
        set(setBoard) {
            field = setBoard

            setCellsInBoard()
        }

    /**
     * Sets the cells that the car encapsulates
     */
    private fun setCellsInBoard() {
        // Remove the previous cells that the car encapsulated
        for (cell in board!!.getCellsWithCar(this))
            cell.car = null

        if (facing == Facing.HORIZONTAL) {
            for (i in 0 until size)
                board!!.cells[position.y][position.x + i].car = this
        } else { // VERTICAL
            for (i in 0 until size)
                board!!.cells[position.y + i][position.x].car = this
        }
    }

    /**
     * Moves the car to a given position.
     * Note, this doesn't check if it can move into the new space.
     *
     * @param newPosition The new position of the car
     */
    fun move(newPosition: Position) {
        position = newPosition

        setCellsInBoard()
    }

    /**
     * Moves the car in a direction, by a certain amount.
     * Note, this doesn't check where its facing before moving
     *
     * @param direction The direction to move in
     * @param amount How much to move by
     */
    fun move(direction: Direction, amount: Int) {
        position = position.move(direction, amount)

        setCellsInBoard()
    }

    /**
     * Checks how far the car can move from being HORIZONTAL or VERTICAL
     *
     * @return Pair<LEFT, RIGHT> or Pair<UP, DOWN>
     */
    fun amountOfMovement(): Pair<Int, Int> {
        return if (facing == Facing.HORIZONTAL) {
            Pair(
                    amountOfMovementInDirection(Direction.LEFT),
                    amountOfMovementInDirection(Direction.RIGHT)
            )
        } else { // VERTICAL
            Pair(
                    amountOfMovementInDirection(Direction.UP),
                    amountOfMovementInDirection(Direction.DOWN)
            )
        }
    }

    /**
     * Checks if the car can move to a specific position without a car impeding,
     * or hitting the limits.
     *
     * @return A boolean checking if the car can move to a position
     */
    fun canMoveTo(newPosition: Position): Boolean {
        if (facing == Facing.HORIZONTAL) {
            if (newPosition.y - position.y != 0)
                return false

            if (position.x - newPosition.x > 0) { // LEFT
                for (i in newPosition.x..position.x) {
                    if (board!!.cells[position.y][i].car != null &&
                            board!!.cells[position.y][i].car != this)
                        return false
                }
            } else { // RIGHT
                for (i in position.x..newPosition.x) {
                    if (board!!.cells[position.y][i].car != null &&
                            board!!.cells[position.y][i].car != this)
                        return false
                }
            }
        } else { // VERTICAL
            if (newPosition.x - position.x != 0)
                return false

            if (position.y - newPosition.y > 0) { // UP
                for (i in newPosition.y..position.y) {
                    if (board!!.cells[i][position.x].car != null &&
                            board!!.cells[i][position.x].car != this)
                        return false
                }
            } else { // DOWN
                for (i in position.y..newPosition.y) {
                    if (board!!.cells[i][position.x].car != null &&
                            board!!.cells[i][position.x].car != this)
                        return false
                }
            }
        }

        return true
    }

    /**
     * Checks how much the car can move in a specific direction.
     * Note, this doesn't check where its facing before checking
     *
     * @param direction The direction being checked
     *
     * @return The amount the car can move in the direction given
     */
    fun amountOfMovementInDirection(direction: Direction): Int {
        // The counter starts at one because of the third checking case
        // If it started at 0, it would stop at this car.
        var counter = 1
        val operation =
            if (direction == Direction.LEFT || direction == Direction.UP)
                -1
            else // RIGHT or DOWN
                1
        val startingPosition =
            if (direction == Direction.LEFT || direction == Direction.UP)
                position
            else // RIGHT or DOWN
                position.move(direction, size - 1)

        if (direction == Direction.DOWN || direction == Direction.UP) {
            /*
             * (startingPosition.y + (operation * counter)) is the position being checked
             * The first checking case checks that its inbounds from the max. A < is being used since the size is one more than the index
             * The second checking case checks that its inbounds from the minimum
             * The third checking case checks that there is no car impeding movement
             */
            while ((startingPosition.y + (operation * counter) < board!!.size) &&
                    (startingPosition.y + (operation * counter) >= 0) &&
                    board!!.cells[startingPosition.y + (operation * counter)][startingPosition.x].car == null) {
                counter++
            }
        } else { // LEFT or RIGHT
            // The cases are same as the y, but for the x
            while ((startingPosition.x + (operation * counter) < board!!.size) &&
                    (startingPosition.x + (operation * counter) >= 0) &&
                    board!!.cells[startingPosition.y][startingPosition.x + (operation * counter)].car == null) {
                counter++
            }
        }


        // We return with -1 because the counter starts at 1
        return counter - 1
    }

    /**
     * Creates an exact copy of the car.
     * Although, this doesn't set the board because its expected of the code cloning it
     *
     * @return An exact copy of this car
     */
    fun clone(): Car {
        return Car(name, size, facing, position)
    }

    init {
        if (size < 2) {
            throw error("A car size cannot be below 2")
        }
    }
}