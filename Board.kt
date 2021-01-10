package main

import java.lang.StringBuilder

class Cell(val position: Position) {
    var car: Car? = null
}

class Board(val size: Int = 6) {
    var cells: Array<Array<Cell>> = Array(size) { y -> Array(size) { x -> Cell(Position(x, y)) } }
    var cars: Array<Car> = arrayOf()
        get() {
            if (field.isNotEmpty()) {
                return field
            }

            val returnCars: ArrayList<Car> = arrayListOf()

            for (row in cells) {
                for (cell in row) {
                    cell.car?.let {
                        if (!returnCars.contains(it)) {
                            returnCars.add(cell.car!!)
                        }
                    }
                }
            }

            field = returnCars.toTypedArray()

            return returnCars.toTypedArray()
        }
    val canMoveCarsFreely: Boolean
        get() {
            var ret = true

            for (car in cars) {
                if (car.forceMove)
                    ret = false
            }

            return ret
        }

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

    fun getCarWithName(name: String): Car? {
        for (car in cars)
            if (car.name == name)
                return car

        return null
    }

    fun differenceCanMove(other: Board, ignore: Car): Pair<Boolean, Array<Car>> {
        val thisMovements = arrayListOf<Pair<Int, Int>>()
        val otherMovements = arrayListOf<Pair<Int, Int>>()
        val allCars = arrayListOf<Car>()

        for (car in cars) {
            if (car.name != ignore.name) {
                thisMovements.add(car.canMove())
                otherMovements.add(other.getCarWithName(car.name)!!.canMove())
                allCars.add(car)
            }
        }

        var opens = false
        val moveCars = arrayListOf<Car>()

        for (index in thisMovements.indices) {
            val thisMovement = thisMovements[index]
            val otherMovement = otherMovements[index]

            if (thisMovement.first < otherMovement.first ||
                thisMovement.second < otherMovement.second) {
                opens = true
                moveCars.add(allCars[index])
            }
        }

        return Pair(!opens, moveCars.toTypedArray())
    }

    fun setCarOnBoard(car: Car) {
        car.board = this
    }

    fun fillWithCars(cars: Array<Car>) {
        for (car in cars)
            setCarOnBoard(car)
    }

    fun clone(): Board {
        val newBoard = Board()

        for (car in cars) {
            newBoard.setCarOnBoard(car.clone())
        }

        return newBoard
    }

    fun clearForceMove() {
        for (car in cars)
            car.forceMove = false
    }

    fun clearMovementLastTurn() {
        for (car in cars)
            car.movedLastTurn = false
    }

    override fun toString(): String {
        val string = StringBuilder()

        for (row in cells) {
            string.append("[ ")

            for (cell in row) {
                val name = if (cell.car == null)
                    "+"
                else cell.car!!.name

                string.append("$name ")
            }

            string.append("]\n")
        }

        return string.toString()
    }

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
                    if (otherCell.car != cell.car) {
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

    override fun hashCode(): Int {
        return cells.contentDeepHashCode()
    }
}