package com.example.assignment2.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.assignment2.data.Movie
import com.example.assignment2.data.MovieRepositoryImpl

@Composable
fun MovieDetailScreen(
    movieId: Long,
    onNavigateBack: () -> Unit,
    // 실제 앱에서는 ViewModel을 사용해야 하지만, 여기서는 Repository에서 직접 데이터를 가져옵니다.
    movieRepository: com.example.assignment2.data.MovieRepository = MovieRepositoryImpl()
) {
    // 전달받은 movieId로 영화 정보를 찾습니다.
    val movie = movieRepository.getMovieById(movieId)

    if (movie == null) {
        // 영화 정보가 없을 경우 처리
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("영화를 찾을 수 없습니다.")
            // 뒤로가기 버튼
            IconButton(onClick = onNavigateBack, modifier = Modifier.align(Alignment.TopStart)) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        }
        return
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
    ) {
        // --- 상단 이미지 영역 ---
        Box(contentAlignment = Alignment.TopStart) {
            // 배경 이미지 (Backdrop)
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w780${movie.backdropPath}",
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .matchParentSize() // 부모(배경 이미지)와 같은 크기로 설정
                    .background(Color.Black.copy(alpha = 0.4f)) // 40% 투명도
            )
            // 뒤로가기 버튼
            IconButton(onClick = onNavigateBack, modifier = Modifier.padding(8.dp)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "뒤로가기",
                    tint = Color.White
                )
            }

            // 포스터, 제목, 평점 영역
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                // 포스터 이미지
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
                    contentDescription = movie.title,
                    modifier = Modifier
                        .width(100.dp)
                        .height(150.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.align(Alignment.Bottom)
                ) {
                    // 제목
                    Text(
                        text = movie.title,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        // 제목이 길면 자동으로 줄바꿈 되도록 width 고정
                        modifier = Modifier.widthIn(max = 200.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    // 평점
                    RatingBar(rating = movie.voteAverage / 2, modifier = Modifier.height(20.dp))
                    Text(
                        text = String.format("%.1f", movie.voteAverage),
                        color = Color.White,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        // --- 하단 정보 영역 ---
        Column(modifier = Modifier.padding(16.dp)) {
            // 장르
            Row {
                movie.genreIds.take(2).forEach { genreId ->
                    val genreName = com.example.assignment2.data.genreMap[genreId]
                    if (genreName != null) {
                        Chip(label = genreName)
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 줄거리 (Summary)
            Text("Summary", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(movie.overview, fontSize = 14.sp, lineHeight = 20.sp)

            Spacer(modifier = Modifier.height(24.dp))

            // 인기도 (Popularity)
            Text("Popularity", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(movie.popularity.toString(), fontSize = 14.sp)
        }
    }
}

@Composable
private fun Chip(label: String) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(Color.LightGray.copy(alpha = 0.5f))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(label, fontSize = 12.sp)
    }
}

@Composable
fun RatingBar(
    rating: Double,
    modifier: Modifier = Modifier,
    starCount: Int = 5,
    starColor: Color = Color.Yellow,
) {
    Row(modifier = modifier) {
        for (i in 1..starCount) {
            val starIcon = if (i <= rating) Icons.Filled.Star else Icons.Default.Star
            val starTint = if (i <= rating) starColor else Color.Gray
            Icon(imageVector = starIcon, contentDescription = null, tint = starTint)
        }
    }
}
    