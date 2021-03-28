package deepanshu.example.com.moviez.modules.movie.api.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

@Keep
data class NowPlayingMoviesResponse(
    @SerializedName("dates")
    @Expose
    val dates: Dates,
    @SerializedName("page")
    @Expose
    val page: Int,
    @SerializedName("results")
    @Expose
    val movies: List<Movie>,
    @SerializedName("total_pages")
    @Expose
    val totalPages: Int,
    @SerializedName("total_results")
    @Expose
    val totalResults: Int
) {
    var popularMoviesResponse: PopularMoviesResponse = PopularMoviesResponse()
}