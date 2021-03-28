package deepanshu.example.com.moviez.modules.movie.api.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

@Keep
data class Cast(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("known_for_department")
    @Expose
    val knownForDepartment: String,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("original_name")
    @Expose
    val originalName: String,
    @SerializedName("popularity")
    @Expose
    val popularity: Double,
    @SerializedName("profile_path")
    @Expose
    val profilePath: String?,
    @SerializedName("character")
    @Expose
    val character: String
)