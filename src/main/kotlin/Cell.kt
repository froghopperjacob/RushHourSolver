/**
 * A cell that references a board, has a position, and
 * can hold a car.
 *
 * @param board The parent board
 * @param position The position of the board
 *
 * @author Jacob Selbo
 * @version 1.0
 * @since 1/12/21
 */
data class Cell(val board: Board, val position: Position) {
    var car: Car? = null

    /**
     * The hashcode of the board
     *
     * @return a unique id for this object
     */
    override fun hashCode(): Int {
        var result = position.hashCode()
        result = 31 * result + (car?.hashCode() ?: 0)
        return result
    }
}