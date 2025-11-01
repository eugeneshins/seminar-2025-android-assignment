package com.example.assignment2.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.assignment2.data.Movie
import com.example.assignment2.data.MovieRepository
import com.example.assignment2.data.MovieRepositoryImpl
import com.example.assignment2.data.RecentSearchesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// UI의 모든 상태를 담는 데이터 클래스
data class SearchUiState(
    val searchQuery: String = "",
    val searchResults: List<Movie> = emptyList(),
    val recentSearches: List<String> = emptyList(),
    val isSearchFocused: Boolean = false,
    val showEmptyResultMessage: Boolean = false
)

class SearchViewModel(
    application: Application, // Context를 얻기 위해 Application을 받음
    private val movieRepository: MovieRepository
) : AndroidViewModel(application) {

    // 최근 검색어를 관리하는 매니저 초기화
    private val recentSearchesManager = RecentSearchesManager(application)

    // ViewModel 내부에서만 관리될 상태 값들
    private val _searchQuery = MutableStateFlow("")
    private val _searchResults = MutableStateFlow<List<Movie>>(emptyList())
    private val _isSearchFocused = MutableStateFlow(false)
    private val _showEmptyResultMessage = MutableStateFlow(false)

    // 여러 내부 상태 값과 DataStore의 Flow를 조합하여 최종 UI 상태(StateFlow)를 만듦
    val uiState: StateFlow<SearchUiState> = combine(
        _searchQuery,
        _searchResults,
        recentSearchesManager.recentSearches, // DataStore의 Flow를 직접 사용
        _isSearchFocused,
        _showEmptyResultMessage
    ) { query, results, recent, focused, showEmpty ->
        SearchUiState(
            searchQuery = query,
            searchResults = results,
            recentSearches = recent,
            isSearchFocused = focused,
            showEmptyResultMessage = showEmpty
        )
    }.stateIn(
        // ViewModel이 활성화되어 있는 동안만 Flow를 활성 상태로 유지
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = SearchUiState()
    )

    // 검색어가 변경될 때 호출
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        // 검색어를 입력하기 시작하면 '결과 없음' 메시지를 숨김
        if (query.isNotEmpty()) {
            _showEmptyResultMessage.value = false
        }
    }

    // 검색 실행 시 호출
    fun executeSearch(query: String) {
        val trimmedQuery = query.trim()
        if (trimmedQuery.isBlank()) return

        viewModelScope.launch {
            val results = movieRepository.searchByTitle(trimmedQuery)
            _searchResults.value = results
            // 검색 결과가 비어있는지 여부를 상태에 반영
            _showEmptyResultMessage.value = results.isEmpty()

            // 검색에 성공하면 최근 검색어 목록에 추가
            recentSearchesManager.addSearchTerm(trimmedQuery)
        }
    }

    // 검색창 포커스 상태 변경 시 호출
    fun onFocusChanged(isFocused: Boolean) {
        _isSearchFocused.value = isFocused
    }

    // 최근 검색어 개별 삭제
    fun removeRecentSearch(term: String) {
        viewModelScope.launch {
            recentSearchesManager.removeSearchTerm(term)
        }
    }

    // 최근 검색어 전체 삭제
    fun clearAllRecentSearches() {
        viewModelScope.launch {
            recentSearchesManager.clearAllSearchTerms()
        }
    }

    // ViewModel을 생성하기 위한 Factory (Application 인스턴스를 ViewModel에 전달하기 위함)
    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
                return SearchViewModel(
                    application = application,
                    movieRepository = MovieRepositoryImpl()
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
