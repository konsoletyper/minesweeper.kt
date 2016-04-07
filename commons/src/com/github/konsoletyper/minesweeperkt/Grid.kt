package com.github.konsoletyper.minesweeperkt

interface Grid<T> {
    operator fun get(row: Int, column: Int): T

    operator fun get(coordinates: Pair<Int, Int>): T {
        val (row, column) = coordinates
        return this[row, column]
    }

    operator fun contains(coordinates: Pair<Int, Int>): Boolean {
        val (row, column) = coordinates
        return row in rows && column in columns
    }

    val rowCount: Int

    val columnCount: Int

    val rows: IntRange
        get() = 0..rowCount - 1

    val columns: IntRange
        get() = 0..columnCount - 1

    val cells: Sequence<Pair<Int, Int>>
        get() = rows.asSequence().map { row -> columns.asSequence().map { column -> Pair(row, column) } }.flatten()

    val cellData: Sequence<T>
        get() = cells.map {
            val (row, column) = it
            this[row, column]
        }

    val cellCount: Int
        get() = rowCount * columnCount

    val dimensions: Pair<Int, Int>
        get() = Pair(rowCount, columnCount)
}
