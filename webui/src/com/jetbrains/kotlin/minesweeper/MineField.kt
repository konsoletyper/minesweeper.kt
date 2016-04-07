package com.jetbrains.kotlin.minesweeper

import com.jetbrains.kotlin.commons.MutableGrid
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import kotlin.browser.document
import kotlin.dom.onClick
import kotlin.dom.removeFromParent

class MineField(private val parent: HTMLElement, val game: Game) {
    private val container = document.createElement("div") as HTMLDivElement
    private var cellElements = createGrid(0, 0)

    init {
        with(container.style) {
            border = "1px solid $BORDER_COLOR"
            position = "relative"
        }
        recreateCellElements()
        update()

        parent.appendChild(container)
    }

    fun update() {
        if (game.cells.dimensions != cellElements.dimensions) {
            recreateCellElements()
        }

        for ((row, column) in cellElements.cells) {
            val element = cellElements[row, column]
            val cell = game.cells[row, column]
            element.wrapper.style.backgroundColor = if (cell.opened) OPENED_CELL_COLOR else CELL_COLOR
            element.content.textContent = when {
                game.state != GameState.IN_PROGRESS && cell.hasMine -> "@"
                cell.opened -> if (cell.minesAround > 0) cell.minesAround.toString() else ""
                else -> ""
            }

            element.content.style.color = if (cell.opened && cell.hasMine) "red" else "black"
        }
    }

    private fun recreateCellElements() {
        cellElements.cellData.forEach { it.wrapper.removeFromParent() }
        cellElements = createGrid(game.cells.rowCount, game.cells.columnCount)

        with(container.style) {
            width = "${CELL_SIZE * cellElements.columnCount}px"
            height = "${CELL_SIZE * cellElements.columnCount}px"
        }
    }

    private fun createGrid(rows: Int, columns: Int) = MutableGrid(rows, columns) { row, column ->
        val wrapper = (document.createElement("div") as HTMLDivElement).apply {
            container.appendChild(this)

            with(style) {
                left = "${column * CELL_SIZE}px"
                top = "${row * CELL_SIZE}px"
                width = "${CELL_SIZE}px"
                height = "${CELL_SIZE}px"
                position = "absolute"
                if (row < rows - 1) {
                    borderBottom = "1px solid $CELL_BORDER_COLOR"
                }
                if (column < columns - 1) {
                    borderRight = "1px solid $CELL_BORDER_COLOR"
                }
                display = "table-cell"
                verticalAlign = "middle"
                boxSizing = "border-box"
            }
            onClick {
                game.cells[row, column].open()
                update()
            }
        }

        val content = (document.createElement("div") as HTMLDivElement).apply {
            with(style) {
                position = "absolute"
                top = "50%"
                height = "${CELL_SIZE - 3}px"
                lineHeight = height
                marginTop = "-${(CELL_SIZE - 3) / 2}px"
                left = "0"
                right = "0"
                textAlign = "center"
            }
            wrapper.appendChild(this)
        }

        CellElements(wrapper, content)
    }

    class CellElements(val wrapper: HTMLDivElement, val content: HTMLDivElement)

    companion object {
        private val BORDER_COLOR = "rgb(150,150,150)"

        private val CELL_SIZE = 40
        private val CELL_BORDER_COLOR = "rgb(200,200,200)"
        private val CELL_COLOR = "white"
        private val OPENED_CELL_COLOR = "rgb(240,240,240)"
    }
}
