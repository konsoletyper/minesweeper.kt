package com.jetbrains.kotlin.minesweeper

import com.jetbrains.kotlin.commons.Grid
import com.jetbrains.kotlin.commons.MutableGrid

class Game() {
    var desiredRows = 20
    var desiredColumns = 20

    var state = GameState.IN_PROGRESS
        get
        private set

    var cellsLeft = 0

    lateinit var cells: Grid<Cell>
        get
        private set

    init {
        restart()
    }

    fun restart() {
        cells = createGrid()
        state = GameState.IN_PROGRESS
        putMines()
    }

    private fun createGrid() = MutableGrid(desiredRows, desiredColumns) { row, col -> Cell(this, row, col) }

    private fun putMines() {
        val minesToPut = cells.cellCount / 7
        for (i in 1..minesToPut) {
            while (true) {
                val row = random(cells.rows)
                val column = random(cells.columns)
                if (!cells[row, column].hasMine) {
                    cells[row, column].hasMine = true
                    break
                }
            }
        }

        cellsLeft = cells.cellCount - minesToPut
    }

    // TODO: make inner after the following bug gets fixed: https://youtrack.jetbrains.com/issue/KT-11823
    class Cell internal constructor(private var game: Game, val row: Int, val column: Int) {
        var opened: Boolean = false
            get
            private set

        var hasMine: Boolean = false
            get
            internal set

        val minesAround: Int by lazy { cellsAround.count { it.hasMine } }

        fun open() {
            if (opened || game.state != GameState.IN_PROGRESS) return

            if (hasMine) {
                opened = true
                game.state = GameState.FAILED
                return
            }

            openAround()

            if (game.cellsLeft == 0) {
                game.state = GameState.WON
                return
            }
        }

        private fun openAround() {
            if (opened) return

            opened = true
            --game.cellsLeft

            if (minesAround == 0) {
                cellsAround.forEach { it.openAround() }
            }
        }

        val cellsAround: Sequence<Cell>
            get() = allCoordinatesAround().filter { it in game.cells }.map { game.cells[it] }

        private fun allCoordinatesAround() = sequenceOf(Pair(row - 1, column - 1), Pair(row - 1, column),
                Pair(row - 1, column + 1), Pair(row, column + 1), Pair(row + 1, column + 1), Pair(row + 1, column),
                Pair(row + 1, column - 1), Pair(row, column - 1))
    }

    companion object {
        private fun random(range: IntRange): Int = range.start +
                (Math.random() * (range.endInclusive - range.start + 1)).toInt()
    }
}