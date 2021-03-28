package deepanshu.example.com.moviez.modules.movie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import deepanshu.example.com.moviez.core.base.BaseViewModel
import deepanshu.example.com.moviez.core.network.ApiResponse
import deepanshu.example.com.moviez.modules.movie.api.response.MovieDetailResponse
import deepanshu.example.com.moviez.modules.movie.api.response.MovieReviewResponse
import deepanshu.example.com.moviez.modules.movie.api.response.SimilarMovieResponse
import deepanshu.example.com.moviez.modules.movie.api.services.MovieService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MovieReviewViewModel : BaseViewModel() {

    @Inject
    lateinit var movieService: MovieService

    private val disposables = CompositeDisposable()

    fun loadMMovieReviewsApi(
        apiKey: String,
        movieId: Int
    ): LiveData<ApiResponse<MovieReviewResponse?>> {
        val movieReviewResponse = MutableLiveData<ApiResponse<MovieReviewResponse?>>()

        disposables.add(
            movieService.getMovieReviews(movieId, apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    movieReviewResponse.value = ApiResponse.loading(null)
                }
                .subscribe({ result ->
                    movieReviewResponse.value = ApiResponse.success(result)
                }, { error ->
                    movieReviewResponse.value = error.message?.let { ApiResponse.error(it, null) }
                })
        )
        return movieReviewResponse
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}