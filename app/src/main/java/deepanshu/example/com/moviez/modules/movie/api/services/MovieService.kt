package deepanshu.example.com.moviez.modules.movie.api.services

import deepanshu.example.com.moviez.modules.movie.api.response.*
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

    @GET("now_playing")
    fun getNowPlayingMovies(
        @Query("api_key") apiKey:String
    ): Single<NowPlayingMoviesResponse>

    @GET("popular")
    fun getPopularMovies(
        @Query("api_key") apiKey:String,
        @Query("page") page:Int
    ): Single<PopularMoviesResponse>

    @GET("{movie_id}")
    fun getMovieDetail(
        @Path(value = "movie_id", encoded = false) movieId:Int,
        @Query("api_key") apiKey:String
    ): Single<MovieDetailResponse>

    @GET("{movie_id}/similar")
    fun getSimilarMovies(
        @Path(value = "movie_id", encoded = false) movieId:Int,
        @Query("api_key") apiKey:String
    ): Single<SimilarMovieResponse>

    @GET("{movie_id}/credits")
    fun getMovieCasts(
        @Path(value = "movie_id", encoded = false) movieId:Int,
        @Query("api_key") apiKey:String
    ): Single<MovieCastResponse>

    @GET("{movie_id}/reviews")
    fun getMovieReviews(
        @Path(value = "movie_id", encoded = false) movieId:Int,
        @Query("api_key") apiKey:String
    ): Single<MovieReviewResponse>
}