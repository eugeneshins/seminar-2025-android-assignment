package com.example.assignment2.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

// Context 확장 함수를 통해 앱 전역에서 동일한 DataStore 인스턴스를 사용하도록 설정
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "search_history")

class RecentSearchesManager(context: Context) {

    private val dataStore = context.dataStore

    // DataStore에 데이터를 저장하기 위한 Key
    private companion object {
        val RECENT_SEARCHES_KEY = stringPreferencesKey("recent_searches")
    }

    // 최근 검색어 목록을 Flow 형태로 가져옴 (데이터가 변경될 때마다 UI가 자동으로 업데이트됨)
    val recentSearches: Flow<List<String>> = dataStore.data.map { preferences ->
        val jsonString = preferences[RECENT_SEARCHES_KEY] ?: "[]"
        Json.decodeFromString<List<String>>(jsonString)
    }

    // 새로운 검색어를 목록에 추가
    suspend fun addSearchTerm(term: String) {
        dataStore.edit { preferences ->
            val currentSearchesJson = preferences[RECENT_SEARCHES_KEY] ?: "[]"
            val currentSearches = Json.decodeFromString<MutableList<String>>(currentSearchesJson)

            // 이미 있는 검색어는 삭제 후 맨 위로 올림
            currentSearches.remove(term)
            currentSearches.add(0, term) // 맨 앞에 추가

            // 최대 10개까지만 저장 (예시)
            val updatedSearches = currentSearches.take(10)

            preferences[RECENT_SEARCHES_KEY] = Json.encodeToString(updatedSearches)
        }
    }

    // 특정 검색어 삭제
    suspend fun removeSearchTerm(term: String) {
        dataStore.edit { preferences ->
            val currentSearchesJson = preferences[RECENT_SEARCHES_KEY] ?: "[]"
            val currentSearches = Json.decodeFromString<MutableList<String>>(currentSearchesJson)
            currentSearches.remove(term)
            preferences[RECENT_SEARCHES_KEY] = Json.encodeToString(currentSearches)
        }
    }

    // 모든 검색어 삭제
    suspend fun clearAllSearchTerms() {
        dataStore.edit { preferences ->
            preferences[RECENT_SEARCHES_KEY] = "[]"
        }
    }
}
    