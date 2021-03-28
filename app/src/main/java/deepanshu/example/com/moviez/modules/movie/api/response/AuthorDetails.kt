package deepanshu.example.com.moviez.modules.movie.api.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

@Keep
data class AuthorDetails(
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("username")
    @Expose
    val username: String,
    @SerializedName("avatar_path")
    @Expose
    val avatarPath: String?,
    @SerializedName("rating")
    @Expose
    val rating: Any?
)