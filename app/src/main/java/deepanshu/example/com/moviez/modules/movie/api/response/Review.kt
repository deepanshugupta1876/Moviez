package deepanshu.example.com.moviez.modules.movie.api.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

@Keep
data class Review(
    @SerializedName("author")
    @Expose
    val author: String,
    @SerializedName("author_details")
    @Expose
    val authorDetails: AuthorDetails,
    @SerializedName("content")
    @Expose
    val content: String,
    @SerializedName("created_at")
    @Expose
    val createdAt: String,
    @SerializedName("id")
    @Expose
    val id: String
)