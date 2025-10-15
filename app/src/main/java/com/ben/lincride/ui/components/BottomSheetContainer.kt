package com.ben.lincride.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.ui.graphics.Brush
import kotlinx.coroutines.delay
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

/**
 * Reusable bottom sheet container that matches LincRide design patterns
 * Supports slide-up animations, backdrop, and dismissal
 */
@Composable
fun BottomSheetContainer(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    heightFraction: Float = 0.6f, // What fraction of screen height
    enableBackdropDismiss: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    val density = LocalDensity.current
    
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(300)),
        exit = fadeOut(animationSpec = tween(300))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(10f)
        ) {
            // Backdrop overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        if (enableBackdropDismiss) {
                            onDismiss()
                        }
                    }
            )
            
            // Bottom sheet content
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(
                    initialOffsetY = { with(density) { 400.dp.roundToPx() } },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                ),
                exit = slideOutVertically(
                    targetOffsetY = { with(density) { 400.dp.roundToPx() } },
                    animationSpec = tween(300)
                ),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Card(
                    modifier = modifier
                        .fillMaxWidth()
                        .fillMaxHeight(heightFraction)
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Standardized bar indicator with its own padding
                        BarIndicator(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 8.dp, bottom = 8.dp)
                        )
                        
                        // Content - no universal padding
                        content()
                    }
                }
            }
        }
    }
}

/**
 * Overlay container for full-screen overlays like Trip Ended
 */
@Composable
fun OverlayContainer(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    backgroundColor: Color = Color.White,
    content: @Composable BoxScope.() -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(500)) + scaleIn(
            initialScale = 0.95f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        ),
        exit = fadeOut(animationSpec = tween(300)) + scaleOut(
            targetScale = 0.95f,
            animationSpec = tween(300)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .zIndex(20f)
        ) {
            content()
        }
    }
}

/**
 * Production-quality animated progress bar for car movement tracking
 * Features smooth transitions, gradient colors, and material motion curves
 */
@Composable
fun CarProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Gray.copy(alpha = 0.3f),
    progressColor: Color = Color.Blue
) {
    // Smooth progress animation with spring physics
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = spring(
            dampingRatio = 0.7f,
            stiffness = 300f
        ),
        label = "progress_animation"
    )
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(8.dp) // Slightly thicker for better visibility
            .background(backgroundColor, RoundedCornerShape(4.dp))
    ) {
        // Animated progress with gradient
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(animatedProgress)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            progressColor.copy(alpha = 0.8f),
                            progressColor,
                            progressColor
                        )
                    ),
                    shape = RoundedCornerShape(4.dp)
                )
        )
        
        // Subtle shimmer effect for active progress
        if (animatedProgress > 0f && animatedProgress < 1f) {
            var shimmerOffset by remember { mutableStateOf(0f) }
            
            LaunchedEffect(animatedProgress) {
                while (animatedProgress > 0f && animatedProgress < 1f) {
                    shimmerOffset = if (shimmerOffset >= 1f) 0f else shimmerOffset + 0.02f
                    delay(50)
                }
            }
            
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(20.dp)
                    .offset(x = (shimmerOffset * 200).dp.coerceAtMost(180.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.White.copy(alpha = 0.3f),
                                Color.Transparent
                            )
                        ),
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        }
    }
}