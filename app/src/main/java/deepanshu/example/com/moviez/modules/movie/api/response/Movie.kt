package deepanshu.example.com.moviez.modules.movie.api.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

@Keep
data class Movie(
    @SerializedName("poster_path")
    @Expose
    val posterPath: String = "",
    @SerializedName("release_date")
    @Expose
    val releaseDate: String = "",
    @SerializedName("id")
    @Expose
    val id: Int = -1,
    @SerializedName("original_language")
    @Expose
    val originalLanguage: String = "",
    @SerializedName("title")
    @Expose
    val title: String = "",
    @SerializedName("vote_average")
    @Expose
    val voteAverage: Double = 0.0
)