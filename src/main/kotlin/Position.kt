/**
 * A position with a X and Y depicting where it is on a plane.
 *
 * @param x The X coordinate
 * @param y The Y coordinate
 *
 * @author Jacob Selbo
 * @version 1.0
 * @since 1/12/21
 */
data class Position(val x: Int, val y: Int) {
    /**
     * Creates a new position from the new X and Y
     *
     * @param newX The X in the new Position
     * @param newY The Y in the new Position
     * @return A new Position
     */
    fun move(newX: Int = x, newY: Int = y): Position = Position(newX, newY)

    /**
     * Creates a new position from a direction and number
     * The direction dictates where the position will move, and
     * the number dictates how far
     *
     * @param direction Where the position will change
     * @param number How much the position will move by
     * @return A new Position
     */
    fun move(direction: Direction, amount: Int): Position {
        return when (direction) {
            Direction.UP -> Position(this.x, this.y - amount)
            Direction.DOWN -> Position(this.x, this.y + amount)
            Direction.LEFT -> Position(this.x - amount, this.y)
            Direction.RIGHT -> Position(this.x + amount, this.y)
        }
    }
}