package com.ben.lincride

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ben.lincride.core.designsystem.theme.LincRideTheme
import com.ben.lincride.core.designsystem.theme.LincGreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.ben.lincride.presentation.viewmodel.RideViewModel
import com.ben.lincride.ui.screens.MainMapScreen
import com.ben.lincride.ui.screens.HomeScreen
import com.ben.lincride.ui.screens.EventSimulationScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LincRideTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LincRideApp()
                }
            }
        }
    }
}

/**
 * Main app entry point - Single screen with overlay bottom sheets
 * Implements correct Event flow: Screen 3.2.1 with bottom sheet overlays
 */
@Composable
fun LincRideApp() {
    // MainMapScreen is now self-contained.
    // It gets its own ViewModel and handles all events internally.
    MainMapScreen()
}

@Composable
fun MainScreen() {
    var currentEvent by remember { mutableStateOf("App Load") }
    var showProgress by remember { mutableStateOf(false) }
    
    // Auto-advance through events for demo
    LaunchedEffect(currentEvent) {
        when (currentEvent) {
            "App Load" -> {
                delay(2000)
                currentEvent = "Offer Ride Available"
            }
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Title
        Text(
            text = "🚗 LincRide",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = LincGreen
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Senior Android Assessment",
            fontSize = 16.sp,
            color = Color.Gray
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        // Current Event Display
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = LincGreen.copy(alpha = 0.1f))
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Current Event:",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = currentEvent,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = LincGreen
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Progress Indicators
        Text(
            text = "✅ Architecture Complete",
            fontSize = 16.sp,
            color = LincGreen
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "✅ Compose Integration Working",
            fontSize = 16.sp,
            color = LincGreen
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "🚧 Implementing Figma Screens...",
            fontSize = 16.sp,
            color = Color.Red
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Action Button
        Button(
            onClick = { 
                showProgress = true
                currentEvent = "Starting Ride Simulation..."
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = LincGreen),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = if (showProgress) "Simulation Starting..." else "Start Event Simulation",
                modifier = Modifier.padding(vertical = 8.dp),
                fontSize = 16.sp
            )
        }
        
        if (showProgress) {
            Spacer(modifier = Modifier.height(16.dp))
            
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = LincGreen
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    LincRideTheme {
        MainScreen()
    }
}
