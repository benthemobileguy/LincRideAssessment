package com.ben.lincride.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Standardized LincRide Design System Extensions
 * Promotes reusability and consistency across the app
 */

// MARK: - Typography Extensions
object AppTypography {
    
    // Use existing typography with consistent colors
    @Composable
    fun displayLarge(color: Color = LincRideColors.textPrimary): TextStyle = 
        MaterialTheme.typography.displayLarge.copy(color = color)
    
    @Composable
    fun displayMedium(color: Color = LincRideColors.textPrimary): TextStyle = 
        MaterialTheme.typography.displayMedium.copy(color = color)
    
    @Composable
    fun headlineLarge(color: Color = LincRideColors.textPrimary): TextStyle = 
        MaterialTheme.typography.headlineLarge.copy(color = color)
    
    @Composable
    fun headlineMedium(color: Color = LincRideColors.textPrimary): TextStyle = 
        MaterialTheme.typography.headlineMedium.copy(color = color)
    
    @Composable
    fun titleLarge(color: Color = LincRideColors.textPrimary): TextStyle = 
        MaterialTheme.typography.titleLarge.copy(color = color)
    
    @Composable
    fun titleMedium(color: Color = LincRideColors.textPrimary): TextStyle = 
        MaterialTheme.typography.titleMedium.copy(color = color)
    
    @Composable
    fun bodyLarge(color: Color = LincRideColors.textPrimary): TextStyle = 
        MaterialTheme.typography.bodyLarge.copy(color = color)
    
    @Composable
    fun bodyMedium(color: Color = LincRideColors.textPrimary): TextStyle = 
        MaterialTheme.typography.bodyMedium.copy(color = color)
    
    @Composable
    fun bodySmall(color: Color = LincRideColors.textSecondary): TextStyle = 
        MaterialTheme.typography.bodySmall.copy(color = color)
    
    @Composable
    fun labelLarge(color: Color = LincRideColors.textPrimary): TextStyle = 
        MaterialTheme.typography.labelLarge.copy(color = color)
    
    @Composable
    fun labelMedium(color: Color = LincRideColors.textSecondary): TextStyle = 
        MaterialTheme.typography.labelMedium.copy(color = color)
    
    @Composable
    fun labelSmall(color: Color = LincRideColors.textTertiary): TextStyle = 
        MaterialTheme.typography.labelSmall.copy(color = color)
}

// MARK: - Color Extensions
object LincRideColors {
    
    // Text Colors
    val textPrimary: Color = Color(0xFF383838)  // Main text color
    val textSecondary: Color = Color(0xFF757575) // Secondary text
    val textTertiary: Color = Color(0xFF9E9E9E)  // Tertiary text
    val textInverse: Color = Color.White         // White text
    val textDark: Color = Color(0xFF2A2A2A)      // Dark text (close button icon)
    
    // Surface Colors
    val surfaceBackground: Color = Color.White
    val surfaceElevated: Color = Color(0xFFF8F9FA)
    val surfaceBorder: Color = Color(0xFFE0E0E0)
    val surfaceInput: Color = Color(0xFFF8F8F8)
    
    // Brand Colors
    val brandGreen: Color = Color(0xFF29E892)      // Campaign/success
    val brandGreenDark: Color = Color(0xFF136B43)  // Trip ended gradient dark
    val brandGreenLight: Color = Color(0xFF25D183) // Trip ended gradient light
    val brandBlue: Color = Color(0xFF2C75FF)       // Primary action
    val brandGray: Color = Color(0xFFB0B0B0)       // Bar indicator
    
    // Progress Colors
    val progressGreen: Color = Color(0xFF2DFFA0)   // Progress bar and thumb
    val progressGreenHeading: Color = Color(0xFF60B527) // Heading to progress bar
    
    // Status Colors
    val statusSuccess: Color = Color(0xFF4CAF50)
    val statusError: Color = Color(0xFFF44336)
    val statusWarning: Color = Color(0xFFFF9800)
    val statusInfo: Color = Color(0xFF9EC0FF)
}

// MARK: - Spacing Extensions
object LincRideSpacing {
    val space1: Dp = 4.dp
    val space2: Dp = 8.dp
    val space3: Dp = 12.dp
    val space4: Dp = 16.dp
    val space5: Dp = 20.dp
    val space6: Dp = 24.dp
    val space8: Dp = 32.dp
    val space10: Dp = 40.dp
    val space12: Dp = 48.dp
}

// MARK: - Radius Extensions  
object LincRideRadius {
    val small: Dp = 8.dp
    val medium: Dp = 12.dp
    val large: Dp = 16.dp
    val extraLarge: Dp = 20.dp
    val modalTop: Dp = 24.dp
}

// MARK: - Component-Specific Extensions
object LincRideComponents {
    
    // Button Heights
    val buttonHeightSmall: Dp = 40.dp
    val buttonHeightMedium: Dp = 48.dp
    val buttonHeightLarge: Dp = 56.dp
    
    // Modal Heights (as fractions)
    val modalHeightSmall: Float = 0.6f
    val modalHeightMedium: Float = 0.7f
    val modalHeightLarge: Float = 0.8f
    
    // Bar Indicator Dimensions
    val barIndicatorWidth: Dp = 80.dp
    val barIndicatorHeight: Dp = 5.dp
    val barIndicatorRadius: Dp = 2.5.dp
}