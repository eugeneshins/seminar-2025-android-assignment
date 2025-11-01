package com.example.seminar_assignment_2025

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.seminar_assignment_2025.ui.GameScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavGenerate()
        }
    }
}

enum class NavItem(val label: String, val icon: ImageVector) {
    Home("Home", Icons.Filled.Home),
    Search("Search", Icons.Filled.Search),
    App("App", Icons.Filled.ShoppingCart),
    Game("Game", Icons.Filled.PlayArrow),
    Profile("Profile", Icons.Filled.Person)
}
@Composable
fun NavGenerate() {
    // 현재 선택된 탭 상태
    var selectedMenu by remember { mutableStateOf(NavItem.Home) }
    Scaffold(
        bottomBar = {
            NavigationBar {
                NavItem.values().forEach { item ->
                    NavigationBarItem(
                        selected = (selectedMenu == item),
                        onClick = { selectedMenu = item },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        // 메인 콘텐츠 영역
        when (selectedMenu) {
            NavItem.Home -> HomeScreen(Modifier.padding(innerPadding))
            NavItem.Search -> SearchScreen(Modifier.padding(innerPadding))
            NavItem.App -> AppScreen(Modifier.padding(innerPadding))
            NavItem.Game -> GameScreen(Modifier.padding(innerPadding))
            NavItem.Profile -> ProfileScreen(Modifier.padding(innerPadding))
        }
    }
}
@Composable fun HomeScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "Home",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
@Composable fun SearchScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "Search",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable fun AppScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "App",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable fun ProfileScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "Profile",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
    }
}