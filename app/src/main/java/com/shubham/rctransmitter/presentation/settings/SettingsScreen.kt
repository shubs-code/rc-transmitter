package com.shubham.rctransmitter.presentation.settings

import androidx.compose.foundation.background
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

    var ipInput by remember { mutableStateOf(udpIp) }
    var portInput by remember { mutableStateOf(udpPort) }
    var modeInput by remember { mutableStateOf(commMode) }

    LaunchedEffect(udpIp, udpPort, commMode) {
        ipInput = udpIp
        portInput = udpPort
        modeInput = commMode
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

            Spacer(modifier = Modifier.size(24.dp))

            Text(
                text = "Settings",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 32.dp)
            )

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
                        cursorColor = Color.White,
                        focusedLabelColor = Color(0xFF4CAF50),
                        unfocusedLabelColor = Color.LightGray
                    ),
                    placeholder = {
                        Text("192.168.1.100", color = Color.Gray)
                    }
                )

                Spacer(modifier = Modifier.size(24.dp))

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
                        cursorColor = Color.White,
                        focusedLabelColor = Color(0xFF4CAF50),
                        unfocusedLabelColor = Color.LightGray
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
                Text(
                    text = "Connect USB device and tap Save\nBaud rate: 115200",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    viewModel.saveSettings(ipInput, portInput, modeInput)
                    onBackClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text("Save", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.size(16.dp))
        }
    }
}