package deepanshu.example.com.moviez.modules.movie.api.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

@Keep
data class Dates(
    @SerializedName("maximum")
    @Expose
    val maximum: String,
    @SerializedName("minimum")
    @Expose
    val minimum: String
)