package com.ben.lincride

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ben.lincride.core.designsystem.theme.LincRideTheme
import com.ben.lincride.core.designsystem.theme.LincGreen
import com.ben.lincride.ui.screens.MainMapScreen
import com.ben.lincride.ui.screens.EventSimulationScreen

@Composable
fun AppContent() {
    var currentScreen by remember { mutableStateOf("simulation") }
    
    when (currentScreen) {
        "simulation" -> {
            Column {
                // Navigation bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { currentScreen = "simulation" },
                        colors = if (currentScreen == "simulation") {
                            ButtonDefaults.buttonColors(containerColor = LincGreen)
                        } else {
                            ButtonDefaults.buttonColors(containerColor = Color.Gray)
                        }
                    ) {
                        Text("Event Demo")
                    }
                    
                    Button(
                        onClick = { currentScreen = "map" },
                        colors = if (currentScreen == "map") {
                            ButtonDefaults.buttonColors(containerColor = LincGreen)
                        } else {
                            ButtonDefaults.buttonColors(containerColor = Color.Gray)
                        }
                    ) {
                        Text("Main Map")
                    }
                }
                
                // Content
                EventSimulationScreen()
            }
        }
        "map" -> {
            Column {
                // Navigation bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { currentScreen = "simulation" },
                        colors = if (currentScreen == "simulation") {
                            ButtonDefaults.buttonColors(containerColor = LincGreen)
                        } else {
                            ButtonDefaults.buttonColors(containerColor = Color.Gray)
                        }
                    ) {
                        Text("Event Demo")
                    }
                    
                    Button(
                        onClick = { currentScreen = "map" },
                        colors = if (currentScreen == "map") {
                            ButtonDefaults.buttonColors(containerColor = LincGreen)
                        } else {
                            ButtonDefaults.buttonColors(containerColor = Color.Gray)
                        }
                    ) {
                        Text("Main Map")
                    }
                }
                
                // Content
                MainMapScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppContentPreview() {
    LincRideTheme {
        AppContent()
    }
}