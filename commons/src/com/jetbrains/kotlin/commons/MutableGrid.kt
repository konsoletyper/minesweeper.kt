package com.jetbrains.kotlin.commons

class MutableGrid<T>(
    override val rowCount: Int,
    override  val columnCount: Int,
    defaultValue: (row: Int, column: Int) -> T
) : Grid<T> {
    private val data = Array<Any?>(rowCount * columnCount, { defaultValue(it / rowCount, it % rowCount) })

    @Suppress("UNCHECKED_CAST")
    override operator fun get(row: Int, column: Int): T = data[row * columnCount + column] as T

    operator fun set(row: Int, column: Int, value: T) {
        data[row * columnCount + column] = value
    }
}