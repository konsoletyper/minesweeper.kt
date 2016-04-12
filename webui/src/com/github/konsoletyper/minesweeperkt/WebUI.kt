package com.github.konsoletyper.minesweeperkt

import kotlin.browser.document

fun main(args: Array<String>) {
    document.body!!.onload = { start() }
}

fun start() {
    val game = Game().apply {
        desiredRows = 15
        desiredColumns = 15
        restart()
    }

    MineField(document.body!!, game)
}
