package deepanshu.example.com.moviez.modules.movie.api.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

@Keep
data class PopularMoviesResponse(
    @SerializedName("page")
    @Expose
    val page: Int = 0,
    @SerializedName("results")
    @Expose
    val movies: List<Movie> = emptyList(),
    @SerializedName("total_results")
    @Expose
    val totalResults: Int = 0,
    @SerializedName("total_pages")
    @Expose
    val totalPages: Int = 0
)