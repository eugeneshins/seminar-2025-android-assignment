package com.example.seminar_assignment_2025.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.seminar_assignment_2025.ui.Direction
import kotlin.random.Random

class GameViewmodel : ViewModel() {
    var board by mutableStateOf(Array(4) { IntArray(4) })
        private set
    var score by mutableStateOf(0)
        private set
    private val boardHistory = mutableListOf<Array<IntArray>>()
    private val scoreHistory = mutableListOf<Int>()
    
    init {
        resetGame()
    }
    fun resetGame() {
        board = Array(4) { IntArray(4) }
        score = 0
        addRandomTile()
        addRandomTile()
    }

    private fun addRandomTile() {
        val emptyList = mutableListOf<Int>()
        board.forEachIndexed { i, row ->
            row.forEachIndexed { j, value ->
                if (value == 0) emptyList.add(i*4+j)
            }
        }
        val pick = emptyList.random()
        board[pick/4][pick%4] = if (Random.nextDouble() > 0.2) 2 else 4
    }

    fun move(direction: Direction) {
        boardHistory.add(deepCopy(board))
        scoreHistory.add(score)
        val newBoard = when (direction) {
            Direction.LEFT  -> moveLeft(board)
            Direction.RIGHT -> moveRight(board)
            Direction.UP    -> moveUp(board)
            Direction.DOWN  -> moveDown(board)
        }

        if (!board.contentDeepEquals(newBoard)) {
            board = newBoard
            addRandomTile()
        }
    }
    private fun deepCopy(arr: Array<IntArray>): Array<IntArray> =
        Array(4) { i -> arr[i].clone() }

    private fun mergeRowLeft(row: IntArray): IntArray {
        val result = IntArray(4)
        var lastValue = 0
        var pos = 0

        for (num in row) {
            if (num == 0) continue
            if (lastValue == 0) {
                lastValue = num
            } else if (lastValue == num) {
                result[pos] = lastValue * 2
                score += result[pos]
                pos++
                lastValue = 0
            } else {
                result[pos] = lastValue
                pos++
                lastValue = num
            }
        }
        if (lastValue != 0) {
            result[pos] = lastValue
        }
        return result
    }

    private fun moveLeft(board: Array<IntArray>): Array<IntArray> =
        Array(4) { i -> mergeRowLeft(board[i]) }

    private fun moveRight(board: Array<IntArray>): Array<IntArray> =
        Array(4) { i -> mergeRowLeft(board[i].reversedArray()).reversedArray() }

    private fun moveUp(board: Array<IntArray>): Array<IntArray> {
        val transposed = transpose(board)
        val moved = Array(4) { i -> mergeRowLeft(transposed[i]) }
        return transpose(moved)
    }

    private fun moveDown(board: Array<IntArray>): Array<IntArray> {
        val transposed = transpose(board)
        val moved = Array(4) { i -> mergeRowLeft(transposed[i].reversedArray()).reversedArray() }
        return transpose(moved)
    }

    private fun transpose(matrix: Array<IntArray>): Array<IntArray> =
        Array(4) { i -> IntArray(4) { j -> matrix[j][i] } }

    fun undo() {
        if (boardHistory.isNotEmpty() && scoreHistory.isNotEmpty()) {
            board = boardHistory.removeLast()
            score = scoreHistory.removeLast()
        }
    }
}