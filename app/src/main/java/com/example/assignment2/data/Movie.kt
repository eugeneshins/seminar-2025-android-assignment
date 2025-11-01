package com.example.assignment2.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val adult: Boolean,
    @SerialName("backdrop_path")
    val backdropPath: String?, // null일 수 있으므로 Nullable(?) 타입으로 변경
    @SerialName("genre_ids")
    val genreIds: List<Int>,
    val id: Long,
    @SerialName("original_language")
    val originalLanguage: String,

    // [추가] 1. 이 필드가 누락되어 있었습니다.
    @SerialName("original_title")
    val original_title: String,

    val overview: String,
    val popularity: Double,
    @SerialName("poster_path")
    val posterPath: String?, // null일 수 있으므로 Nullable(?) 타입으로 변경
    @SerialName("release_date")
    val releaseDate: String,
    val title: String,
    val video: Boolean,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount: Int
) {
    // [추가] 2. getGenresString 함수를 클래스 안으로 이동시켜 관리합니다.
    fun getGenresString(): String {
        return genreIds
            .mapNotNull { id -> genreMap[id] }
            .joinToString(separator = ", ")
    }
}

// 장르 ID와 이름을 매핑하는 Map (전역 변수)
val genreMap = mapOf<Int, String>(
    28 to "Action", 12 to "Adventure", 16 to "Animation", 35 to "Comedy",
    80 to "Crime", 99 to "Documentary", 18 to "Drama", 10751 to "Family",
    14 to "Fantasy", 36 to "History", 27 to "Horror", 10402 to "Music",
    9648 to "Mystery", 10749 to "Romance", 878 to "Science Fiction",
    10770 to "TV Movie", 53 to "Thriller", 10752 to "War", 37 to "Western"
)
