package com.ben.lincride.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.ben.lincride.R
import kotlin.math.roundToInt

@Composable
fun CarProgressBar(
    progress: Float,
    progressColor: Color,
    modifier: Modifier = Modifier
) {
    var barWidthPx = 0f
    val density = LocalDensity.current

    // Smooth animation for car movement
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 400),
        label = "car_progress_animation"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(12.dp)
            .onGloballyPositioned { coordinates ->
                barWidthPx = coordinates.size.width.toFloat()
            },
        contentAlignment = Alignment.CenterStart
    ) {
        // Track (no rounded corners)
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.LightGray.copy(alpha = 0.3f))
        )

        // Progress fill
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(animatedProgress)
                .background(progressColor)
        )

        // Car icon overlay
        Image(
            painter = painterResource(id = R.drawable.ic_car),
            contentDescription = "Car icon",
            modifier = Modifier
                .offset { 
                    val carWidthPx = with(density) { 24.dp.toPx() } // approximate car size
                    IntOffset(
                        x = ((barWidthPx - carWidthPx) * animatedProgress).roundToInt(),
                        y = 0
                    )
                }
                .size(24.dp)
        )
    }
}