package com.ben.lincride.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ben.lincride.core.designsystem.theme.LincRideTheme

@Composable
fun BarIndicator(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFFB0B0B0)
) {
    Box(
        modifier = modifier
            .width(80.dp)
            .height(5.dp)
            .background(
                color = color,
                shape = RoundedCornerShape(2.5.dp)
            )
    )
}

@Preview(showBackground = true)
@Composable
fun BarIndicatorPreview() {
    LincRideTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BarIndicator()
        }
    }
}