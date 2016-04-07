package com.jetbrains.kotlin.minesweeper

import kotlin.browser.document

fun start() {
    val game = Game().apply {
        desiredRows = 15
        desiredColumns = 15
        restart()
    }

    MineField(document.body!!, game)
}
