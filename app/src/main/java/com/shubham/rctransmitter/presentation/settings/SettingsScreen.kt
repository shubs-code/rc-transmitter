package com.shubham.rctransmitter.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val udpIp by viewModel.udpIp.collectAsState()
    val udpPort by viewModel.udpPort.collectAsState()
    val commMode by viewModel.commMode.collectAsState()

    val leftMinX by viewModel.leftMinX.collectAsState()
    val leftMaxX by viewModel.leftMaxX.collectAsState()
    val leftMinY by viewModel.leftMinY.collectAsState()
    val leftMaxY by viewModel.leftMaxY.collectAsState()

    val rightMinX by viewModel.rightMinX.collectAsState()
    val rightMaxX by viewModel.rightMaxX.collectAsState()
    val rightMinY by viewModel.rightMinY.collectAsState()
    val rightMaxY by viewModel.rightMaxY.collectAsState()

    // Assuming snapback is a Boolean and percent is an Int
    val leftXSnapBack by viewModel.leftXSnapBack.collectAsState()
    val leftXSnapPercent by viewModel.leftXSnapPercent.collectAsState()

    val leftYSnapBack by viewModel.leftYSnapBack.collectAsState()
    val leftYSnapPercent by viewModel.leftYSnapPercent.collectAsState()

    val rightXSnapBack by viewModel.rightXSnapBack.collectAsState()
    val rightXSnapPercent by viewModel.rightXSnapPercent.collectAsState()

    val rightYSnapBack by viewModel.rightYSnapBack.collectAsState()
    val rightYSnapPercent by viewModel.rightYSnapPercent.collectAsState()

    var ipInput by remember { mutableStateOf(udpIp) }
    var portInput by remember { mutableStateOf(udpPort) }
    var modeInput by remember { mutableStateOf(commMode) }

    var leftMinXInput by remember { mutableStateOf(leftMinX.toString()) }
    var leftMaxXInput by remember { mutableStateOf(leftMaxX.toString()) }
    var leftMinYInput by remember { mutableStateOf(leftMinY.toString()) }
    var leftMaxYInput by remember { mutableStateOf(leftMaxY.toString()) }

    var rightMinXInput by remember { mutableStateOf(rightMinX.toString()) }
    var rightMaxXInput by remember { mutableStateOf(rightMaxX.toString()) }
    var rightMinYInput by remember { mutableStateOf(rightMinY.toString()) }
    var rightMaxYInput by remember { mutableStateOf(rightMaxY.toString()) }

    var leftXSnapBackInput by remember { mutableStateOf(leftXSnapBack) }
    var leftXSnapPercentInput by remember { mutableStateOf(leftXSnapPercent.toString()) }

    var leftYSnapBackInput by remember { mutableStateOf(leftYSnapBack) }
    var leftYSnapPercentInput by remember { mutableStateOf(leftYSnapPercent.toString()) }

    var rightXSnapBackInput by remember { mutableStateOf(rightXSnapBack) }
    var rightXSnapPercentInput by remember { mutableStateOf(rightXSnapPercent.toString()) }

    var rightYSnapBackInput by remember { mutableStateOf(rightYSnapBack) }
    var rightYSnapPercentInput by remember { mutableStateOf(rightYSnapPercent.toString()) }

    LaunchedEffect(
        udpIp, udpPort, commMode,
        leftMinX, leftMaxX, leftMinY, leftMaxY,
        rightMinX, rightMaxX, rightMinY, rightMaxY,
        leftXSnapBack, leftXSnapPercent,
        leftYSnapBack, leftYSnapPercent,
        rightXSnapBack, rightXSnapPercent,
        rightYSnapBack, rightYSnapPercent
    ) {
        ipInput = udpIp
        portInput = udpPort
        modeInput = commMode
        leftMinXInput = leftMinX.toString()
        leftMaxXInput = leftMaxX.toString()
        leftMinYInput = leftMinY.toString()
        leftMaxYInput = leftMaxY.toString()
        rightMinXInput = rightMinX.toString()
        rightMaxXInput = rightMaxX.toString()
        rightMinYInput = rightMinY.toString()
        rightMaxYInput = rightMaxY.toString()

        leftXSnapBackInput = leftXSnapBack
        leftXSnapPercentInput = leftXSnapPercent.toString()
        leftYSnapBackInput = leftYSnapBack
        leftYSnapPercentInput = leftYSnapPercent.toString()

        rightXSnapBackInput = rightXSnapBack
        rightXSnapPercentInput = rightXSnapPercent.toString()
        rightYSnapBackInput = rightYSnapBack
        rightYSnapPercentInput = rightYSnapPercent.toString()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1a1f2e))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.Start)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.size(16.dp))

            Text(
                text = "Settings",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Communication Mode
            Text(
                text = "Communication Mode",
                fontSize = 16.sp,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = modeInput == "UDP",
                    onClick = { modeInput = "UDP" },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color(0xFF4CAF50),
                        unselectedColor = Color.Gray
                    )
                )
                Text("UDP", color = Color.White, modifier = Modifier.padding(start = 8.dp))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = modeInput == "USB",
                    onClick = { modeInput = "USB" },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color(0xFF4CAF50),
                        unselectedColor = Color.Gray
                    )
                )
                Text("USB Serial", color = Color.White, modifier = Modifier.padding(start = 8.dp))
            }

            if (modeInput == "UDP") {
                Text(
                    text = "UDP IP Address",
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = ipInput,
                    onValueChange = { ipInput = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFF4CAF50),
                        unfocusedBorderColor = Color.Gray,
                        focusedContainerColor = Color(0xFF2D3142),
                        unfocusedContainerColor = Color(0xFF2D3142),
                        cursorColor = Color.White
                    ),
                    placeholder = {
                        Text("192.168.1.100", color = Color.Gray)
                    }
                )

                Spacer(modifier = Modifier.size(16.dp))

                Text(
                    text = "UDP Port",
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = portInput,
                    onValueChange = { portInput = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFF4CAF50),
                        unfocusedBorderColor = Color.Gray,
                        focusedContainerColor = Color(0xFF2D3142),
                        unfocusedContainerColor = Color(0xFF2D3142),
                        cursorColor = Color.White
                    ),
                    placeholder = {
                        Text("5000", color = Color.Gray)
                    }
                )
            } else {
                Text(
                    text = "USB Serial Configuration",
                    fontSize = 14.sp,
                    color = Color(0xFFBBBBBB),
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            Spacer(modifier = Modifier.size(24.dp))

            // ================= LEFT JOYSTICK CONFIGURATION =================
            Text(
                text = "Left Joystick Settings",
                fontSize = 16.sp,
                color = Color(0xFF4CAF50),
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Left X Limits
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Min X", fontSize = 12.sp, color = Color.Gray)
                    OutlinedTextField(
                        value = leftMinXInput,
                        onValueChange = { leftMinXInput = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF4CAF50), unfocusedBorderColor = Color.Gray,
                            focusedContainerColor = Color(0xFF2D3142), unfocusedContainerColor = Color(0xFF2D3142),
                            cursorColor = Color.White
                        )
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("Max X", fontSize = 12.sp, color = Color.Gray)
                    OutlinedTextField(
                        value = leftMaxXInput,
                        onValueChange = { leftMaxXInput = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF4CAF50), unfocusedBorderColor = Color.Gray,
                            focusedContainerColor = Color(0xFF2D3142), unfocusedContainerColor = Color(0xFF2D3142),
                            cursorColor = Color.White
                        )
                    )
                }
            }

            // Left X Snap
            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = leftXSnapBackInput,
                        onCheckedChange = { leftXSnapBackInput = it },
                        colors = CheckboxDefaults.colors(checkedColor = Color(0xFF4CAF50), uncheckedColor = Color.Gray, checkmarkColor = Color.White)
                    )
                    Text("Snap Back X", fontSize = 14.sp, color = Color.White)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("X Snap %", fontSize = 12.sp, color = Color.Gray)
                    OutlinedTextField(
                        value = leftXSnapPercentInput,
                        onValueChange = { leftXSnapPercentInput = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF4CAF50), unfocusedBorderColor = Color.Gray,
                            focusedContainerColor = Color(0xFF2D3142), unfocusedContainerColor = Color(0xFF2D3142),
                            cursorColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.size(16.dp))

            // Left Y Limits
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Min Y", fontSize = 12.sp, color = Color.Gray)
                    OutlinedTextField(
                        value = leftMinYInput,
                        onValueChange = { leftMinYInput = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF4CAF50), unfocusedBorderColor = Color.Gray,
                            focusedContainerColor = Color(0xFF2D3142), unfocusedContainerColor = Color(0xFF2D3142),
                            cursorColor = Color.White
                        )
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("Max Y", fontSize = 12.sp, color = Color.Gray)
                    OutlinedTextField(
                        value = leftMaxYInput,
                        onValueChange = { leftMaxYInput = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF4CAF50), unfocusedBorderColor = Color.Gray,
                            focusedContainerColor = Color(0xFF2D3142), unfocusedContainerColor = Color(0xFF2D3142),
                            cursorColor = Color.White
                        )
                    )
                }
            }

            // Left Y Snap
            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = leftYSnapBackInput,
                        onCheckedChange = { leftYSnapBackInput = it },
                        colors = CheckboxDefaults.colors(checkedColor = Color(0xFF4CAF50), uncheckedColor = Color.Gray, checkmarkColor = Color.White)
                    )
                    Text("Snap Back Y", fontSize = 14.sp, color = Color.White)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("Y Snap %", fontSize = 12.sp, color = Color.Gray)
                    OutlinedTextField(
                        value = leftYSnapPercentInput,
                        onValueChange = { leftYSnapPercentInput = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF4CAF50), unfocusedBorderColor = Color.Gray,
                            focusedContainerColor = Color(0xFF2D3142), unfocusedContainerColor = Color(0xFF2D3142),
                            cursorColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.size(24.dp))

            // ================= RIGHT JOYSTICK CONFIGURATION =================
            Text(
                text = "Right Joystick Settings",
                fontSize = 16.sp,
                color = Color(0xFF4CAF50),
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Right X Limits
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Min X", fontSize = 12.sp, color = Color.Gray)
                    OutlinedTextField(
                        value = rightMinXInput,
                        onValueChange = { rightMinXInput = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF4CAF50), unfocusedBorderColor = Color.Gray,
                            focusedContainerColor = Color(0xFF2D3142), unfocusedContainerColor = Color(0xFF2D3142),
                            cursorColor = Color.White
                        )
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("Max X", fontSize = 12.sp, color = Color.Gray)
                    OutlinedTextField(
                        value = rightMaxXInput,
                        onValueChange = { rightMaxXInput = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF4CAF50), unfocusedBorderColor = Color.Gray,
                            focusedContainerColor = Color(0xFF2D3142), unfocusedContainerColor = Color(0xFF2D3142),
                            cursorColor = Color.White
                        )
                    )
                }
            }

            // Right X Snap
            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = rightXSnapBackInput,
                        onCheckedChange = { rightXSnapBackInput = it },
                        colors = CheckboxDefaults.colors(checkedColor = Color(0xFF4CAF50), uncheckedColor = Color.Gray, checkmarkColor = Color.White)
                    )
                    Text("Snap Back X", fontSize = 14.sp, color = Color.White)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("X Snap %", fontSize = 12.sp, color = Color.Gray)
                    OutlinedTextField(
                        value = rightXSnapPercentInput,
                        onValueChange = { rightXSnapPercentInput = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF4CAF50), unfocusedBorderColor = Color.Gray,
                            focusedContainerColor = Color(0xFF2D3142), unfocusedContainerColor = Color(0xFF2D3142),
                            cursorColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.size(16.dp))

            // Right Y Limits
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Min Y", fontSize = 12.sp, color = Color.Gray)
                    OutlinedTextField(
                        value = rightMinYInput,
                        onValueChange = { rightMinYInput = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF4CAF50), unfocusedBorderColor = Color.Gray,
                            focusedContainerColor = Color(0xFF2D3142), unfocusedContainerColor = Color(0xFF2D3142),
                            cursorColor = Color.White
                        )
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("Max Y", fontSize = 12.sp, color = Color.Gray)
                    OutlinedTextField(
                        value = rightMaxYInput,
                        onValueChange = { rightMaxYInput = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF4CAF50), unfocusedBorderColor = Color.Gray,
                            focusedContainerColor = Color(0xFF2D3142), unfocusedContainerColor = Color(0xFF2D3142),
                            cursorColor = Color.White
                        )
                    )
                }
            }

            // Right Y Snap
            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = rightYSnapBackInput,
                        onCheckedChange = { rightYSnapBackInput = it },
                        colors = CheckboxDefaults.colors(checkedColor = Color(0xFF4CAF50), uncheckedColor = Color.Gray, checkmarkColor = Color.White)
                    )
                    Text("Snap Back Y", fontSize = 14.sp, color = Color.White)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("Y Snap %", fontSize = 12.sp, color = Color.Gray)
                    OutlinedTextField(
                        value = rightYSnapPercentInput,
                        onValueChange = { rightYSnapPercentInput = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF4CAF50), unfocusedBorderColor = Color.Gray,
                            focusedContainerColor = Color(0xFF2D3142), unfocusedContainerColor = Color(0xFF2D3142),
                            cursorColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    viewModel.saveSettings(
                        ipInput,
                        portInput,
                        modeInput,
                        leftMinXInput.toIntOrNull() ?: -100,
                        leftMaxXInput.toIntOrNull() ?: 100,
                        leftMinYInput.toIntOrNull() ?: -100,
                        leftMaxYInput.toIntOrNull() ?: 100,
                        rightMinXInput.toIntOrNull() ?: -100,
                        rightMaxXInput.toIntOrNull() ?: 100,
                        rightMinYInput.toIntOrNull() ?: -100,
                        rightMaxYInput.toIntOrNull() ?: 100,
                        leftXSnapBackInput,
                        leftXSnapPercentInput.toIntOrNull() ?: 100,
                        leftYSnapBackInput,
                        leftYSnapPercentInput.toIntOrNull() ?: 100,
                        rightXSnapBackInput,
                        rightXSnapPercentInput.toIntOrNull() ?: 100,
                        rightYSnapBackInput,
                        rightYSnapPercentInput.toIntOrNull() ?: 100
                    )
                    onBackClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text("Save", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.size(16.dp))
        }
    }
}