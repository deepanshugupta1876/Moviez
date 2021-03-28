package deepanshu.example.com.moviez.modules.movie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import deepanshu.example.com.moviez.core.base.BaseViewModel
import deepanshu.example.com.moviez.core.network.ApiResponse
import deepanshu.example.com.moviez.modules.movie.api.response.MovieCastResponse
import deepanshu.example.com.moviez.modules.movie.api.response.MovieDetailResponse
import deepanshu.example.com.moviez.modules.movie.api.response.SimilarMovieResponse
import deepanshu.example.com.moviez.modules.movie.api.services.MovieService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MovieCastViewModel : BaseViewModel() {

    @Inject
    lateinit var movieService: MovieService

    private val disposables = CompositeDisposable()


    fun loadMovieCastApi(
        apiKey: String,
        movieId: Int
    ): LiveData<ApiResponse<MovieCastResponse?>> {
        val movieCastResponse = MutableLiveData<ApiResponse<MovieCastResponse?>>()

        disposables.add(
            movieService.getMovieCasts(movieId, apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    movieCastResponse.value = ApiResponse.loading(null)
                }
                .subscribe({ result ->
                    movieCastResponse.value = ApiResponse.success(result)
                }, { error ->
                    movieCastResponse.value = error.message?.let { ApiResponse.error(it, null) }
                })
        )
        return movieCastResponse
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}