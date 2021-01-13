import java.lang.StringBuilder

/**
 * A board that holds cars and cells.
 *
 * @param name The name of the car
 * @param size Width and Height of the board
 *
 * @author Jacob Selbo
 * @version 1.0
 * @since 1/12/21
 */
class Board(val name: String, val size: Int) {
    val cells: Array<Array<Cell>> = Array(size)
        { y ->
            Array(size) { x ->
                Cell(this, Position(x, y)) } }
    /**
     * A list of all cars in the board
     */
    var cars: Array<Car> = arrayOf()
        get() {
            if (field.isNotEmpty()) // Check if we've already indexed this
                return field

            val boardCars: ArrayList<Car> = arrayListOf()

            for (row in cells) {
                for (cell in row) {
                    cell.car?.let {
                        if (!boardCars.contains(it))
                            boardCars += it
                    }
                }
            }

            field = boardCars.toTypedArray()
            return field
        }

    /**
     * Gets all cells that contains the given car.
     *
     * @param car Checking car
     *
     * @return Array of all the cells containing the car
     */
    fun getCellsWithCar(car: Car): Array<Cell> {
        val carCells: ArrayList<Cell> = arrayListOf()

        for (row in cells) {
            for (cell in row) {
                if (cell.car == car) {
                    carCells.add(cell)
                }
            }
        }

        return carCells.toTypedArray()
    }

    /**
     * Gets the first car with the given name.
     * If no car could be found, returns null
     *
     * @param name The name of the car
     *
     * @return The first car with the name, if not null
     */
    fun getCarWithName(name: String): Car? {
        for (car in cars)
            if (car.name == name)
                return car

        return null
    }

    /**
     * Places a car onto the board
     * The car sets the cells it encapsulates
     *
     * @param car The car to place
     */
    fun placeCarOnBoard(car: Car) {
        car.board = this
    }

    /**
     * Places a list of cars onto the board
     *
     * @param cars A list of cars to place
     */
    fun placeCarsOnBoard(cars: Array<Car>) {
        for (car in cars)
            placeCarOnBoard(car)
    }

    /**
     * Clones the board, and the cars associated with
     *
     * @return A new, duplicate board
     */
    fun clone(): Board {
        return Board(name, size).also {
            for (car in cars) {
                it.placeCarOnBoard(car.clone())
            }
        }
    }

    /**
     * @return A readable format of the board
     */
    override fun toString(): String {
        val string = StringBuilder()

        for (row in cells) {
            string.append("[ ")

            for (cell in row) {
                val name = if (cell.car == null)
                    "-"
                else cell.car!!.name

                string.append("$name ")
            }

            string.append("]\n")
        }

        return string.toString()
    }

    /**
     * Checks if this board is equal to another T.
     * If the T is a board, then it checks if the cells cars names are the same.
     *
     * @return A boolean equating if this is equal to another T
     */
    override fun equals(other: Any?): Boolean {
        if (other !is Board) {
            return false
        }

        val otherBoard: Board = other
        var equals = true

        for (rowIndex in cells.indices) {
            val row = cells[rowIndex]

            for (cellIndex in row.indices) {
                val cell = row[cellIndex]
                val otherCell = otherBoard.cells[rowIndex][cellIndex]

                if (otherCell.car == null || cell.car == null) {
                    if (otherCell.car != cell.car) { // Checking that they are both null. Since we know one is null, this is just a shorthand
                        equals = false
                    }
                } else {
                    if (cell.car!!.name != otherCell.car!!.name) {
                        equals = false
                    }
                }
            }
        }

        return equals
    }

    /**
     * The hashcode of the board
     *
     * @return a unique id for this object
     */
    override fun hashCode(): Int {
        var result = size
        result = 31 * result + cells.contentDeepHashCode()
        return result
    }

    companion object {
        /**
         * Create a board from a string
         *
         * @param name The name of the board
         * @param size Size of the board
         * @param from Rows of strings forming the board in a string form.
         *
         * @return A new board
         */
        fun from(name: String, size: Int, from: Array<Array<String>>): Board {
            val newBoard = Board(name, size)
            val cars: ArrayList<Car> = arrayListOf()
            val cellsWithCars: HashMap<String, ArrayList<Position>> = hashMapOf()

            for (rowIndex in from.indices) {
                val row = from[rowIndex]

                for (cellIndex in row.indices) {
                    val cell = row[cellIndex]

                    if (cell != "-") {
                        if (!cellsWithCars.containsKey(cell)) {
                            cellsWithCars[cell] = arrayListOf(Position(cellIndex, rowIndex))
                        } else {
                            cellsWithCars[cell]!!.add(Position(cellIndex, rowIndex))
                        }
                    }
                }
            }

            for ((carName, cells) in cellsWithCars) {
                var facing = Facing.HORIZONTAL

                if (cells[0].x == cells[1].x)
                    facing = Facing.VERTICAL

                cars.add(Car(carName, cells.size, facing, cells[0]))
            }

            newBoard.placeCarsOnBoard(cars.toTypedArray())

            return newBoard
        }
    }
}