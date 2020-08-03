package board

import board.Direction.*

fun createSquareBoard(width: Int): SquareBoard {

    val cells = mutableListOf<Cell>()
    for (i in 1..width) {
        for (j in 1..width) {
            cells.add(Cell(i, j))
        }
    }

    return object : SquareBoard {
        override val width: Int
            get() = width

        override fun getCellOrNull(i: Int, j: Int): Cell? {
            return if (width < i || width < j) {
                null
            } else {
                cells.find {
                    it.i == i && it.j == j
                }
            }
        }

        override fun getCell(i: Int, j: Int): Cell {
            if (width < i || width < j) {
                throw IllegalArgumentException()
            } else {
                return cells.find {
                    it.i == i && it.j == j
                }!!
            }
        }

        override fun getAllCells(): Collection<Cell> {
            return cells
        }

        override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
            val list = mutableListOf<Cell>()
            jRange.forEach { column ->
                if (column <= width) {
                    list.add(
                            cells.find {
                                it.i == i && it.j == column
                            }!!
                    )
                }
            }
            return list
        }

        override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
            val list = mutableListOf<Cell>()
            iRange.forEach { row ->
                if (row <= width) {
                    list.add(
                            cells.find {
                                it.i == row && it.j == j
                            }!!
                    )
                }
            }
            return list
        }

        override fun Cell.getNeighbour(direction: Direction): Cell? {
            var row = this.i
            var column = this.j
            return when (direction) {
                UP -> {
                    row--
                    if (row > 0) {
                        cells.find {
                            it.i == row && it.j == column
                        }!!
                    } else {
                        null
                    }
                }
                LEFT -> {
                    column--
                    if (column > 0) {
                        cells.find {
                            it.i == row && it.j == column
                        }!!
                    } else {
                        null
                    }
                }
                DOWN -> {
                    row++
                    if (row <= width) {
                        cells.find {
                            it.i == row && it.j == column
                        }!!
                    } else {
                        null
                    }
                }
                RIGHT -> {
                    column++
                    if (column <= width) {
                        cells.find {
                            it.i == row && it.j == column
                        }!!
                    } else {
                        null
                    }
                }
            }
        }

    }
}

fun <T> createGameBoard(width: Int): GameBoard<T> {

    val cells = mutableListOf<Cell>()
    val cellsData = mutableMapOf<Cell, T?>()
    for (i in 1..width) {
        for (j in 1..width) {
            val cell = Cell(i, j)
            cells.add(cell)
            cellsData[cell] = null
        }
    }

    return object : GameBoard<T> {
        override val width: Int
            get() = width

        override fun getCellOrNull(i: Int, j: Int): Cell? {
            return if (width < i || width < j) {
                null
            } else {
                cells.find {
                    it.i == i && it.j == j
                }
            }
        }

        override fun getCell(i: Int, j: Int): Cell {
            if (width < i || width < j) {
                throw IllegalArgumentException()
            } else {
                return cells.find {
                    it.i == i && it.j == j
                }!!
            }
        }

        override fun getAllCells(): Collection<Cell> {
            return cells
        }

        override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
            val list = mutableListOf<Cell>()
            jRange.forEach { column ->
                if (column <= width) {
                    list.add(
                            cells.find {
                                it.i == i && it.j == column
                            }!!
                    )
                }
            }
            return list
        }

        override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
            val list = mutableListOf<Cell>()
            iRange.forEach { row ->
                if (row <= width) {
                    list.add(
                            cells.find {
                                it.i == row && it.j == j
                            }!!
                    )
                }
            }
            return list
        }

        override fun Cell.getNeighbour(direction: Direction): Cell? {
            var row = this.i
            var column = this.j
            return when (direction) {
                UP -> {
                    row--
                    if (row > 0) {
                        cells.find {
                            it.i == row && it.j == column
                        }
                    } else {
                        null
                    }
                }
                LEFT -> {
                    column--
                    if (column > 0) {
                        cells.find {
                            it.i == row && it.j == column
                        }
                    } else {
                        null
                    }
                }
                DOWN -> {
                    row++
                    if (row <= width) {
                        cells.find {
                            it.i == row && it.j == column
                        }!!
                    } else {
                        null
                    }
                }
                RIGHT -> {
                    column++
                    if (column <= width) {
                        cells.find {
                            it.i == row && it.j == column
                        }
                    } else {
                        null
                    }
                }
            }
        }

        override fun get(cell: Cell): T? {
            return cellsData[cell]
        }

        override fun set(cell: Cell, value: T?) {
            cellsData[cell] = value
        }

        override fun filter(predicate: (T?) -> Boolean): Collection<Cell> {
            val list = mutableListOf<Cell>()
            cellsData.forEach { (cell, t) ->
                if(predicate(t)) {
                    list.add(cell)
                }
            }
            return list
        }

        override fun find(predicate: (T?) -> Boolean): Cell? {
            cellsData.forEach { (cell, t) ->
                if(predicate(t)) {
                    return cell
                }
            }
            return null
        }

        override fun any(predicate: (T?) -> Boolean): Boolean {
            cells.forEach {
                if(predicate(cellsData[it])) {
                    return true
                }
            }
            return false
        }

        override fun all(predicate: (T?) -> Boolean): Boolean {
            cells.forEach {
                if(!predicate(cellsData[it])) {
                    return false
                }
            }
            return true
        }

    }
}


