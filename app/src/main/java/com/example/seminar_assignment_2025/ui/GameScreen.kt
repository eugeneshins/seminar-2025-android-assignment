package com.example.seminar_assignment_2025.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seminar_assignment_2025.game.GameViewmodel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlin.math.abs

enum class Direction { LEFT, RIGHT, UP, DOWN }
@Composable
fun GameScreen(modifier: Modifier = Modifier, viewModel: GameViewmodel = viewModel()) {
    var swipeDirection by remember { mutableStateOf<Direction?>(null) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .pointerInput(Unit) {
            detectDragGestures (onDragStart = { swipeDirection = null },
                onDragEnd = {
                    swipeDirection?.let { dir ->
                        viewModel.move(dir)
                    }
                },
                onDrag = { change, dragAmount ->
                    if (swipeDirection == null) {
                        val (dx, dy) = dragAmount
                        if (abs(dx) > 20 || abs(dy) > 20) {
                            swipeDirection = if (abs(dx) > abs(dy)) {
                                if (dx > 0) Direction.RIGHT else Direction.LEFT
                            } else {
                                if (dy > 0) Direction.DOWN else Direction.UP
                            }
                        }
                    }
                }
            )
        },
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("2048", fontSize = 32.sp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Score: ${viewModel.score}", fontSize = 20.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {viewModel.undo()}) { Text("Undo") }
                Button(onClick = {viewModel.resetGame()}) { Text("Reset") }
            }
        }

        Column {
            repeat(4) { i ->
                Row {
                    repeat(4) { j ->
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .padding(4.dp)
                                .background(getColor(viewModel.board[i][j])),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("${if(viewModel.board[i][j] != 0) viewModel.board[i][j] else ""}",
                                fontSize = 20.sp,
                                color = if(viewModel.board[i][j] >4) Color.White else Color.Black)
                        }
                    }
                }
            }
        }
    }
}

fun getColor(value: Int): Color {
    return when (value) {
        0    -> Color(0xFFcdc1b4)
        2    -> Color(0xFFeee4da)
        4    -> Color(0xFFede0c8)
        8    -> Color(0xFFf2b179)
        16   -> Color(0xFFf59563)
        32   -> Color(0xFFf67c5f)
        64   -> Color(0xFFf65e3b)
        128  -> Color(0xFFedcf72)
        256  -> Color(0xFFedcc61)
        512  -> Color(0xFFedc850)
        1024 -> Color(0xFFedc53f)
        2048 -> Color(0xFFedc22e)
        else -> Color.Black
    }
}