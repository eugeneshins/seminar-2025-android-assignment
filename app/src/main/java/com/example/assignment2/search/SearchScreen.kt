package com.example.assignment2.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.assignment2.R
import com.example.assignment2.data.Movie

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel,
    // [수정] 1. MainActivity로부터 영화 ID(Long)를 받는 콜백 함수를 파라미터로 추가합니다.
    onMovieClick: (Long) -> Unit
) {
    val uiState by searchViewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(
            query = uiState.searchQuery,
            onQueryChange = searchViewModel::onSearchQueryChange,
            onSearch = {
                focusManager.clearFocus() // 검색 시 키보드 내리기
                searchViewModel.executeSearch(it)
            },
            onFocusChanged = searchViewModel::onFocusChanged
        )

        // 상태에 따라 다른 화면을 보여줌
        when {
            // 검색창에 포커스가 있고, 검색어가 비어있을 때
            uiState.isSearchFocused && uiState.searchQuery.isEmpty() -> {
                RecentSearchesList(
                    recentSearches = uiState.recentSearches,
                    onSearchTermClick = { term ->
                        searchViewModel.onSearchQueryChange(term)
                        focusManager.clearFocus()
                        searchViewModel.executeSearch(term)
                    },
                    onRemoveClick = searchViewModel::removeRecentSearch,
                    onClearAllClick = searchViewModel::clearAllRecentSearches
                )
            }
            // 검색 결과가 없다고 명시된 경우
            uiState.showEmptyResultMessage -> {
                EmptySearchResult()
            }
            // 검색 결과가 있는 경우
            uiState.searchResults.isNotEmpty() -> {
                // [수정] 2. 받아온 onMovieClick 콜백을 SearchResultsList에 그대로 전달합니다.
                SearchResultsList(movies = uiState.searchResults, onItemClick = onMovieClick)
            }
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onFocusChanged: (Boolean) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .onFocusChanged { onFocusChanged(it.isFocused) },
        placeholder = { Text("검색") },
        leadingIcon = {
            IconButton(onClick = { onSearch(query) }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
            }
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear search query")
                }
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = { onSearch(query) }
        ),
        shape = MaterialTheme.shapes.extraLarge
    )
}

@Composable
fun RecentSearchesList(
    recentSearches: List<String>,
    onSearchTermClick: (String) -> Unit,
    onRemoveClick: (String) -> Unit,
    onClearAllClick: () -> Unit
) {
    if (recentSearches.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("최근 검색어가 없습니다.")
        }
        return
    }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("최근 검색어", fontWeight = FontWeight.Bold)
            Text("전체 삭제", modifier = Modifier.clickable { onClearAllClick() }, color = Color.Gray, fontSize = 14.sp)
        }
        LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
            items(recentSearches) { term ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSearchTermClick(term) }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(term, modifier = Modifier.weight(1f))
                    IconButton(onClick = { onRemoveClick(term) }, modifier = Modifier.size(24.dp)) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "Remove recent search", tint = Color.Gray)
                    }
                }
                Divider()
            }
        }
    }
}

@Composable
fun SearchResultsList(
    movies: List<Movie>,
    // [수정] 3. Movie 객체 전체 대신 영화의 ID(Long)만 받도록 타입을 변경합니다.
    onItemClick: (Long) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(movies, key = { it.id }) { movie ->
            // [수정] 4. 클릭 시 Movie 객체 전체가 아닌 movie.id 값만 콜백으로 전달합니다.
            MovieListItem(movie = movie, onClick = { onItemClick(movie.id) })
            Divider()
        }
    }
}

@Composable
fun MovieListItem(movie: Movie, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
            contentDescription = movie.title,
            modifier = Modifier
                .width(80.dp)
                .aspectRatio(2 / 3f)
                .clip(MaterialTheme.shapes.medium),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.movie_icon),
            error = painterResource(id = R.drawable.movie_icon)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(movie.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(
                "개봉: ${movie.releaseDate.substring(0, 4)} | 장르: ${movie.getGenresString()}",
                fontSize = 12.sp,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                "평점: ${String.format("%.1f", movie.voteAverage)}",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun EmptySearchResult() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.movie_icon),
                contentDescription = "Empty Result",
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("검색 결과가 없습니다.")
        }
    }
}
