package games.gameOfFifteen

import board.Cell
import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'.
 */
fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game =
        GameOfFifteen(initializer)

class GameOfFifteen(private val initializer: GameOfFifteenInitializer) : Game {
    private val board = createGameBoard<Int?>(4)
    override fun initialize() {
        val values = initializer.initialPermutation
        var count = 0
        board.getAllCells().forEach {
            if (count < 15)
                board[it] = values[count++]
        }
    }

    override fun canMove(): Boolean {
        return true
    }

    override fun hasWon(): Boolean {
        return board
                .getAllCells()
                .asSequence()
                .zipWithNext { a, b ->
                    if (board[b] == null)
                        true
                    else
                        board[a] ?: 0 <= board[b] ?: 0
                }.all {
                    it
                }
    }

    override fun processMove(direction: Direction) {
        with(board) {
            val emptyCell = getAllCells().find {
                get(it) == null
            }
            when (direction) {
                Direction.UP -> {
                    val cell = emptyCell?.getNeighbour(Direction.DOWN)
                    cell?.let {
                        swapCellValue(this, cell, emptyCell)
                    }
                }
                Direction.DOWN -> {
                    val cell = emptyCell?.getNeighbour(Direction.UP)
                    cell?.let {
                        swapCellValue(this, cell, emptyCell)
                    }
                }
                Direction.RIGHT -> {
                    val cell = emptyCell?.getNeighbour(Direction.LEFT)
                    cell?.let {
                        swapCellValue(this, cell, emptyCell)
                    }
                }
                Direction.LEFT -> {
                    val cell = emptyCell?.getNeighbour(Direction.RIGHT)
                    cell?.let {
                        swapCellValue(this, cell, emptyCell)
                    }
                }
            }
        }
    }

    override fun get(i: Int, j: Int): Int? =
            board.run { get(getCell(i, j)) }

    private fun swapCellValue(board: GameBoard<Int?>, a: Cell, b: Cell) {
        with(board) {
            val temp = get(a)
            set(a, get(b))
            set(b, temp)
        }
    }
}