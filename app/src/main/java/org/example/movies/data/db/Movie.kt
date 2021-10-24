package org.example.movies.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull

@Entity
data class Movie(
    @PrimaryKey
    val id: Long,

    @ColumnInfo(name = "imdb_id")
    @SerializedName("imdb_id")
    val imdbId: String?,

    @NotNull
    @ColumnInfo(name = "original_title")
    @SerializedName("original_title")
    val originalTitle: String,

    @NotNull
    @ColumnInfo(name = "backdrop_path")
    @SerializedName("backdrop_path")
    val backdropPath: String,

    @NotNull
    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    val posterPath: String

    ) {
    @Ignore
    var favorite = false
}