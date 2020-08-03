package games.game2048

import board.Cell
import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game

/*
 * Your task is to implement the game 2048 https://en.wikipedia.org/wiki/2048_(video_game).
 * Implement the utility methods below.
 *
 * After implementing it you can try to play the game running 'PlayGame2048'.
 */
fun newGame2048(initializer: Game2048Initializer<Int> = RandomGame2048Initializer): Game =
        Game2048(initializer)

class Game2048(private val initializer: Game2048Initializer<Int>) : Game {
    private val board = createGameBoard<Int?>(4)

    override fun initialize() {
        repeat(2) {
            board.addNewValue(initializer)
        }
    }

    override fun canMove() = board.any { it == null }

    override fun hasWon() = board.any { it == 2048 }

    override fun processMove(direction: Direction) {
        if (board.moveValues(direction)) {
            board.addNewValue(initializer)
        }
    }

    override fun get(i: Int, j: Int): Int? = board.run { get(getCell(i, j)) }
}

/*
 * Add a new value produced by 'initializer' to a specified cell in a board.
 */
fun GameBoard<Int?>.addNewValue(initializer: Game2048Initializer<Int>) {
    val item = initializer.nextValue(this)
    if (item != null) {
        this[item.first] = item.second
    }
}

/*
 * Update the values stored in a board,
 * so that the values were "moved" in a specified rowOrColumn only.
 * Use the helper function 'moveAndMergeEqual' (in Game2048Helper.kt).
 * The values should be moved to the beginning of the row (or column),
 * in the same manner as in the function 'moveAndMergeEqual'.
 * Return 'true' if the values were moved and 'false' otherwise.
 */
fun GameBoard<Int?>.moveValuesInRowOrColumn(rowOrColumn: List<Cell>): Boolean {
    var flag = false
    val result = rowOrColumn
            .map { this[it] }
            .moveAndMergeEqual { it+it }
    rowOrColumn.withIndex().forEach {
        if(this[it.value] != result.getOrNull(it.index)) {
            flag = true
        }
        this[it.value] = result.getOrNull(it.index)
    }
    return flag
}

/*
 * Update the values stored in a board,
 * so that the values were "moved" to the specified direction
 * following the rules of the 2048 game.
 * Use the 'moveValuesInRowOrColumn' function above.
 * Return 'true' if the values were moved and 'false' otherwise.
 */
fun GameBoard<Int?>.moveValues(direction: Direction): Boolean {
    when (direction) {
        Direction.UP -> {
            val one = this.moveValuesInRowOrColumn(this.getColumn(1..4, 1))
            val two = this.moveValuesInRowOrColumn(this.getColumn(1..4, 2))
            val three = this.moveValuesInRowOrColumn(this.getColumn(1..4, 3))
            val four = this.moveValuesInRowOrColumn(this.getColumn(1..4, 4))
            return one || two || three || four
        }
        Direction.DOWN -> {
            val one = this.moveValuesInRowOrColumn(this.getColumn(4 downTo 1, 1))
            val two = this.moveValuesInRowOrColumn(this.getColumn(4 downTo 1, 2))
            val three = this.moveValuesInRowOrColumn(this.getColumn(4 downTo 1, 3))
            val four = this.moveValuesInRowOrColumn(this.getColumn(4 downTo 1, 4))
            return one || two || three || four
        }
        Direction.RIGHT -> {
            val one = this.moveValuesInRowOrColumn(this.getRow(1, 4 downTo 1))
            val two = this.moveValuesInRowOrColumn(this.getRow(2, 4 downTo 1))
            val three = this.moveValuesInRowOrColumn(this.getRow(3, 4 downTo 1))
            val four = this.moveValuesInRowOrColumn(this.getRow(4, 4 downTo 1))
            return one || two || three || four
        }
        Direction.LEFT -> {
            val one = this.moveValuesInRowOrColumn(this.getRow(1, 1..4))
            val two = this.moveValuesInRowOrColumn(this.getRow(2, 1..4))
            val three = this.moveValuesInRowOrColumn(this.getRow(3, 1..4))
            val four= this.moveValuesInRowOrColumn(this.getRow(4, 1..4))
            return one || two || three || four
        }
    }

}