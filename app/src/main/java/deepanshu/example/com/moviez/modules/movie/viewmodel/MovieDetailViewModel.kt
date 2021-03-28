package deepanshu.example.com.moviez.modules.movie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import deepanshu.example.com.moviez.core.base.BaseViewModel
import deepanshu.example.com.moviez.core.network.ApiResponse
import deepanshu.example.com.moviez.modules.movie.api.response.MovieDetailResponse
import deepanshu.example.com.moviez.modules.movie.api.response.SimilarMovieResponse
import deepanshu.example.com.moviez.modules.movie.api.services.MovieService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MovieDetailViewModel : BaseViewModel() {

    @Inject
    lateinit var movieService: MovieService

    private val disposables = CompositeDisposable()


    fun loadMovieDetailWithSimilarMoviesApi(
        apiKey: String,
        movieId: Int
    ): LiveData<ApiResponse<MovieDetailResponse?>> {
        val movieDetailResponse = MutableLiveData<ApiResponse<MovieDetailResponse?>>()

        disposables.add(
            movieService.getMovieDetail(movieId, apiKey)
                .subscribeOn(Schedulers.io())
                .zipWith(
                    movieService.getSimilarMovies(movieId, apiKey)
                        .subscribeOn(Schedulers.io()),
                    this::handlePopularMoviesResponse
                )
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    movieDetailResponse.value = ApiResponse.loading(null)
                }
                .subscribe({ result ->
                    movieDetailResponse.value = ApiResponse.success(result)
                }, { error ->
                    movieDetailResponse.value = error.message?.let { ApiResponse.error(it, null) }
                })
        )
        return movieDetailResponse
    }

    private fun handlePopularMoviesResponse(
        movieDetailResponse: MovieDetailResponse,
        similarMovieResponse: SimilarMovieResponse
    ): MovieDetailResponse {
        movieDetailResponse.similarMovieResponse = similarMovieResponse
        return movieDetailResponse
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}