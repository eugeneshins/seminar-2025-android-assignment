package com.example.assignment2

import android.app.Application
import android.os.Bundle
import com.example.assignment2.GameScreen
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import android.view.LayoutInflater
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.assignment2.ui.theme.Assignment2Theme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.assignment2.detail.MovieDetailScreen
import com.example.assignment2.search.SearchScreen
import com.example.assignment2.search.SearchViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// 1. 네비게이션 아이템 데이터 클래스 정의
data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Assignment2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    // NavController 생성: 화면 이동, 백스택 관리를 모두 처리하는 컨트롤러
    val navController = rememberNavController()

    // 2. 네비게이션 아이템 리스트 생성
    val navItems = listOf(
        BottomNavItem("Home", Icons.Default.Home, "home"),
        BottomNavItem("Search", Icons.Default.Search, "search"),
        BottomNavItem("App", Icons.Default.ShoppingCart, "app"),
        BottomNavItem("Game", Icons.Default.PlayArrow, "game"),
        BottomNavItem("Profile", Icons.Default.AccountCircle, "profile")
    )

    Scaffold(
        bottomBar = {
            // 현재 화면의 경로(route)를 실시간으로 확인
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            // 현재 경로가 상세 화면("detail/...")이 아닐 때만 하단 네비게이션 바를 보여줌
            if (currentRoute?.startsWith("detail/") != true) {
                NavigationBar {
                    navItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.route,
                            onClick = {
                                // 아이콘 클릭 시 해당 경로로 이동
                                navController.navigate(item.route) {
                                    // 백스택 최상단(startDestination)까지 pop하여 중복 스택 방지
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    // 같은 화면이 여러 개 쌓이는 것을 방지
                                    launchSingleTop = true
                                    // 이전 상태 복원
                                    restoreState = true
                                }
                            },
                            icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        // NavHost: 내비게이션 경로에 따라 화면을 교체해주는 컨테이너
        NavHost(
            navController = navController,
            startDestination = "search", // 앱 시작 시 보여줄 첫 화면
            modifier = Modifier.padding(innerPadding)
        ) {
            // "search" 경로를 요청받았을 때 SearchScreen을 보여줌
            composable("search") {
                val factory = SearchViewModel.Factory(LocalContext.current.applicationContext as Application)
                SearchScreen(
                    searchViewModel = viewModel(factory = factory),
                    // 검색 결과 클릭 시 실행될 콜백: movieId를 받아 상세 화면으로 이동
                    onMovieClick = { movieId ->
                        navController.navigate("detail/$movieId")
                    }
                )
            }

            // "detail/{movieId}" 경로를 요청받았을 때 MovieDetailScreen을 보여줌
            composable(
                route = "detail/{movieId}", // URL에서 movieId 부분을 변수로 사용
                arguments = listOf(navArgument("movieId") { type = NavType.LongType }) // movieId는 Long 타입
            ) { backStackEntry ->
                // 전달받은 movieId를 추출
                val movieId = backStackEntry.arguments?.getLong("movieId")
                if (movieId != null) {
                    MovieDetailScreen(
                        movieId = movieId,
                        // 상세 화면의 뒤로가기 버튼(<) 클릭 시 실행될 콜백
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
            }

            // --- 나머지 하단 탭 화면들 ---
            composable("home") { HomeScreen() }
            composable("app") { AppScreen() }
            composable("game") { GameScreen() }
            composable("profile") { ProfileScreen() }
        }
    }
}

// --- 각 탭에 해당하는 화면들 (이하 코드는 변경 없음) ---

@Composable
fun HomeScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Home")
    }
}

@Composable
fun AppScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("App")
    }
}

@Composable
fun ProfileScreen() {
    AndroidView(modifier = Modifier.fillMaxSize(),
        factory = { context ->
            // LayoutInflater를 사용한 표준적인 inflate 방식
            LayoutInflater.from(context).inflate(R.layout.profile_activity, null)
        },
        update = { /* 뷰 업데이트 로직 */ }
    )
}
