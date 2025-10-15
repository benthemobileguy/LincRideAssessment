package com.ben.lincride.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import com.ben.lincride.R
import com.ben.lincride.core.designsystem.theme.LincGreen
import com.ben.lincride.core.designsystem.theme.IconPrimary
import com.ben.lincride.core.designsystem.theme.IconSecondary

/**
 * Bottom navigation bar with proper Material Design icons
 */
@Composable
fun BottomNavigationBar(
    selectedTab: Int = 0,
    onTabSelected: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = Color.White,
        contentColor = IconPrimary,
        tonalElevation = 0.dp // Remove shadow elevation
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_home),
                    contentDescription = "Home",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    text = "Home",
                    fontSize = 12.sp,
                    fontWeight = if (selectedTab == 0) FontWeight.SemiBold else FontWeight.Normal
                )
            },
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = IconPrimary,
                selectedTextColor = IconPrimary,
                unselectedIconColor = IconSecondary,
                unselectedTextColor = IconSecondary,
                indicatorColor = Color.Transparent // Remove shadow/oval overlay
            )
        )
        
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_history),
                    contentDescription = "History",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    text = "History",
                    fontSize = 12.sp,
                    fontWeight = if (selectedTab == 1) FontWeight.SemiBold else FontWeight.Normal
                )
            },
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = IconPrimary,
                selectedTextColor = IconPrimary,
                unselectedIconColor = IconSecondary,
                unselectedTextColor = IconSecondary,
                indicatorColor = Color.Transparent // Remove shadow/oval overlay
            )
        )
        
        NavigationBarItem(
            icon = {
                // Circular avatar for profile
                Image(
                    painter = painterResource(id = R.drawable.avatar_profile),
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFB6C1), CircleShape),
                    contentScale = ContentScale.Crop
                )
            },
            label = {
                Text(
                    text = "Profile",
                    fontSize = 12.sp,
                    fontWeight = if (selectedTab == 2) FontWeight.SemiBold else FontWeight.Normal
                )
            },
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = IconPrimary,
                selectedTextColor = IconPrimary,
                unselectedIconColor = IconSecondary,
                unselectedTextColor = IconSecondary,
                indicatorColor = Color.Transparent // Remove shadow/oval overlay
            )
        )
    }
}