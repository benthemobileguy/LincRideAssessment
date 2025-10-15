package com.ben.lincride.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.ben.lincride.R
import com.ben.lincride.core.designsystem.theme.LincRideTheme
import com.ben.lincride.core.designsystem.theme.LincRideColors

@Composable
fun GradientProgressBarWithCar(
    progress: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp), // Height to accommodate car above progress bar
        contentAlignment = Alignment.CenterStart
    ) {
        // Layer 1 (Bottom): Background track - 8px height, no radius
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .align(Alignment.CenterStart)
                .background(Color(0xFFE8ECF0))
        )
        
        // Layer 2 (Middle): Progress fill with gradient - 8px height, no radius
        val animatedProgress by animateFloatAsState(
            targetValue = progress.coerceIn(0f, 1f),
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            ),
            label = "progress_animation"
        )
        
        Box(
            modifier = Modifier
                .fillMaxWidth(animatedProgress)
                .height(8.dp)
                .align(Alignment.CenterStart)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF9EC0FF), // Blue/200 from Figma specs
                            Color(0xFF1F53B5)  // Blue/700 from Figma specs
                        )
                    )
                )
        )
        
        // Layer 3 (Top): Car icon stacked on top
        if (progress > 0f) {
            BoxWithConstraints(
                modifier = Modifier.fillMaxWidth()
            ) {
                val carXPosition = (maxWidth - 56.dp) * animatedProgress
                
                Image(
                    painter = painterResource(id = R.drawable.ic_car),
                    contentDescription = "Car",
                    modifier = Modifier
                        .size(56.dp)
                        .offset(x = carXPosition)
                        .align(Alignment.CenterStart)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GradientProgressBarWithCarPreview() {
    LincRideTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GradientProgressBarWithCar(
                progress = 0.3f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )
            GradientProgressBarWithCar(
                progress = 0.7f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )
        }
    }
}