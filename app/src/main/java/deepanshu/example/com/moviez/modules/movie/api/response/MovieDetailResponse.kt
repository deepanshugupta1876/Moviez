package deepanshu.example.com.moviez.modules.movie.api.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

@Keep
data class MovieDetailResponse(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("original_language")
    @Expose
    val originalLanguage: String,
    @SerializedName("popularity")
    @Expose
    val popularity: Double,
    @SerializedName("poster_path")
    @Expose
    val posterPath: Any?,
    @SerializedName("production_companies")
    @Expose
    val productionCompanies: List<ProductionCompany>,
    @SerializedName("release_date")
    @Expose
    val releaseDate: String,
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("title")
    @Expose
    val title: String,
    @SerializedName("overview")
    @Expose
    val overview: String,
    @SerializedName("vote_average")
    @Expose
    val voteAverage: Double,
) {
    var similarMovieResponse:SimilarMovieResponse = SimilarMovieResponse()
}