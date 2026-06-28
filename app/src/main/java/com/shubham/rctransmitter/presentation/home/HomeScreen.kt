package com.shubham.rctransmitter.presentation.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun HomeScreen(
    onSettingsClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val isReady by viewModel.isReady.collectAsState()

    val leftStickX by viewModel.leftStickX.collectAsState()
    val leftStickY by viewModel.leftStickY.collectAsState()

    val rightStickX by viewModel.rightStickX.collectAsState()
    val rightStickY by viewModel.rightStickY.collectAsState()
    val telemetryLines by viewModel.telemetryLines.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1a1f2e))
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.navigationBars.asPaddingValues())
                .padding(32.dp, 8.dp, 32.dp, 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // LEFT JOYSTICK
            RotaryControl(
                modifier = Modifier.weight(1f),
                stickX = leftStickX,
                stickY = leftStickY,
                onPositionChange = { x, y -> viewModel.updateLeftStick(x, y) },
                onRelease = { viewModel.releaseLeftStick() } // Handled dynamically by VM
            )

            Column(
                modifier = Modifier
                    .weight(0.8f)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

//                Box(
//                    modifier = Modifier
//                        .size(60.dp)
//                        .clip(RoundedCornerShape(12.dp))
//                        .background(
//                            if (isReady) Color(0xFFF44336)
//                            else Color(0xFF4CAF50)
//                        )
//                        .clickable { viewModel.toggleReady() },
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        text = if (isReady) "STOP" else "READY",
//                        fontSize = 12.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Color.Black
//                    )
//                }

                IconButton(onClick = onSettingsClick) {
                    Icon(
                        Icons.Outlined.Settings,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
            }

            // RIGHT JOYSTICK
            RotaryControl(
                modifier = Modifier.weight(1f),
                stickX = rightStickX,
                stickY = rightStickY,
                onPositionChange = { x, y -> viewModel.updateRightStick(x, y) },
                onRelease = { viewModel.releaseRightStick() } // Handled dynamically by VM
            )
        }

        // Telemetry Display
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(60.dp)
                .padding(start = 32.dp,
                    end = 76.dp,
                    top = 8.dp,
                    bottom = 8.dp)
                .background(Color(0xFF0f1419), shape = RoundedCornerShape(8.dp))
                .border(1.dp, Color(0xFF4CAF50), RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                telemetryLines.lastOrNull()?.let { line ->
                    Text(
                        text = line,
                        fontSize = 11.sp,
                        color = Color(0xFF4CAF50),
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1
                    )

                }
            }
        }
    }


}

@Composable
fun RotaryControl(
    modifier: Modifier = Modifier,
    stickX: Float = 0f,
    stickY: Float = 0f,
    onPositionChange: (Float, Float) -> Unit, // Simplified types to non-null
    onRelease: () -> Unit
) {
    val controlSize = 200.dp
    val knobSize = 50.dp
    val maxDistance = 75f

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .size(controlSize)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF2d3142))
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        updateJoystick(offset, size, onPositionChange)
                    },
                    onDrag = { change, _ ->
                        change.consume()
                        updateJoystick(change.position, size, onPositionChange)
                    },
                    onDragEnd = { onRelease() },      // Clear & decoupled
                    onDragCancel = { onRelease() }   // Clear & decoupled
                )
            },
        contentAlignment = Alignment.Center
    ) {
        // OUTER BORDER (Square)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    3.dp,
                    Color(0xFF4d5562),
                    RoundedCornerShape(20.dp)
                )
        )

        // CENTER CROSS LINES
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(2.dp)
                .background(Color(0xFF4d5562))
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(Color(0xFF4d5562))
        )

        // KNOB
        Box(
            modifier = Modifier
                .size(knobSize)
                .offset(
                    x = (stickX * maxDistance).dp,
                    y = (stickY * maxDistance).dp
                )
                .background(Color.White, RoundedCornerShape(14.dp))
        )
    }
}

private fun updateJoystick(
    touch: Offset,
    size: IntSize,
    onPositionChange: (Float, Float) -> Unit
) {
    val minSize = minOf(size.width, size.height).toFloat()
    val maxDistance = minSize / 2.5f
    val centerX = size.width / 2f
    val centerY = size.height / 2f

    val dx = (touch.x - centerX).coerceIn(-maxDistance, maxDistance)
    val dy = (touch.y - centerY).coerceIn(-maxDistance, maxDistance)

    val normalizedX = (dx / maxDistance).coerceIn(-1f, 1f)
    val normalizedY = (dy / maxDistance).coerceIn(-1f, 1f)
    onPositionChange(normalizedX, normalizedY)
}


