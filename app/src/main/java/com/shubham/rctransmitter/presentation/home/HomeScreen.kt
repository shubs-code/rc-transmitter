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
import kotlin.math.roundToInt

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1a1f2e))
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp, 8.dp, 32.dp, 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // LEFT JOYSTICK
            RotaryControl(
                modifier = Modifier.weight(1f),
                stickX = leftStickX,
                stickY = leftStickY,
                isLeftThrottle = true,
                onPositionChange = { x, y ->
                    viewModel.updateLeftStick(x, y)
                }
            )

            Column(
                modifier = Modifier
                    .weight(0.8f)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isReady) Color(0xFFF44336)
                            else Color(0xFF4CAF50)
                        )
                        .clickable { viewModel.toggleReady() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isReady) "STOP" else "READY",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

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
                isLeftThrottle = false,
                onPositionChange = { x, y ->
                    viewModel.updateRightStick(x, y)
                }
            )
        }
    }
}

@Composable
fun RotaryControl(
    modifier: Modifier = Modifier,
    stickX: Float = 0f,
    stickY: Float = 0f,
    isLeftThrottle: Boolean = false,
    onPositionChange: (Float, Float) -> Unit
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
                        updateJoystick(
                            touch = offset,
                            size = size,
                            isLeftThrottle = isLeftThrottle,
                            onPositionChange = onPositionChange
                        )
                    },
                    onDrag = { change, _ ->
                        change.consume()
                        updateJoystick(
                            touch = change.position,
                            size = size,
                            isLeftThrottle = isLeftThrottle,
                            onPositionChange = onPositionChange
                        )
                    },
                    onDragEnd = {
                        if (isLeftThrottle) {
                            onPositionChange(0f, stickY)
                        } else {
                            onPositionChange(0f, 0f)
                        }
                    },
                    onDragCancel = {
                        if (isLeftThrottle) {
                            onPositionChange(0f, stickY)
                        } else {
                            onPositionChange(0f, 0f)
                        }
                    }
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
    isLeftThrottle: Boolean,
    onPositionChange: (Float, Float) -> Unit
) {
    val minSize = minOf(size.width, size.height).toFloat()
    val maxDistance = minSize / 2.5f

    val centerX = size.width / 2f
    val centerY = size.height / 2f

    var dx = touch.x - centerX
    var dy = touch.y - centerY

    // SQUARE RANGE (instead of circular)
    dx = dx.coerceIn(-maxDistance, maxDistance)
    dy = dy.coerceIn(-maxDistance, maxDistance)

    val normalizedX = (dx / maxDistance).coerceIn(-1f, 1f)
    val normalizedY = (dy / maxDistance).coerceIn(-1f, 1f)

    onPositionChange(normalizedX, normalizedY)

    val xPercent = (normalizedX * 100).roundToInt()

    if (isLeftThrottle) {
        val throttlePercent =
            (((1f - normalizedY) / 2f) * 100f).roundToInt()

        Log.d(
            "JOYSTICK",
            "LEFT -> Horizontal=$xPercent%, Vertical=$throttlePercent%"
        )
    } else {
        val yPercent = (-normalizedY * 100).roundToInt()

        Log.d(
            "JOYSTICK",
            "RIGHT -> Horizontal=$xPercent%, Vertical=$yPercent%"
        )
    }
}