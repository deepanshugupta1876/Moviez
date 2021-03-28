package deepanshu.example.com.moviez.modules.movie.api.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

@Keep
data class MovieCastResponse(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("cast")
    @Expose
    val cast: List<Cast>,
)