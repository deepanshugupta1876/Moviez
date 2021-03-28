package deepanshu.example.com.moviez.modules.movie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import deepanshu.example.com.moviez.core.base.BaseViewModel
import deepanshu.example.com.moviez.core.network.ApiResponse
import deepanshu.example.com.moviez.modules.movie.api.request.PopularMovieRequest
import deepanshu.example.com.moviez.modules.movie.api.response.NowPlayingMoviesResponse
import deepanshu.example.com.moviez.modules.movie.api.response.PopularMoviesResponse
import deepanshu.example.com.moviez.modules.movie.api.services.MovieService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MovieListingViewmodel: BaseViewModel() {

    @Inject
    lateinit var movieService: MovieService

    private val  disposables = CompositeDisposable()


    fun loadPopularAndNowPlayingMoviesApi(apiKey:String, startPagePopular:Int): LiveData<ApiResponse<NowPlayingMoviesResponse?>> {
        val nowPlayingResponse = MutableLiveData<ApiResponse<NowPlayingMoviesResponse?>>()
        val popularMoviesRequest = PopularMovieRequest.Builder()
            .setPage(startPagePopular)
            .build()
        disposables.add(
            movieService.getNowPlayingMovies(apiKey)
                .subscribeOn(Schedulers.io())
                .zipWith(movieService.getPopularMovies(apiKey, popularMoviesRequest.page)
                    .subscribeOn(Schedulers.io()),
                    this::handlePopularMoviesResponse)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    nowPlayingResponse.value = ApiResponse.loading(null) }
                .subscribe({
                    result -> nowPlayingResponse.value = ApiResponse.success(result)
                }, {
                    error -> nowPlayingResponse.value = error.message?.let { ApiResponse.error(it,null) }
                }))
        return nowPlayingResponse
    }

    private fun handlePopularMoviesResponse(nowPlayingMovies:NowPlayingMoviesResponse, popularMovies: PopularMoviesResponse) : NowPlayingMoviesResponse {
        nowPlayingMovies.popularMoviesResponse = popularMovies
        return nowPlayingMovies
    }

    fun loadPopularMovieApi(apiKey:String, page:Int): LiveData<ApiResponse<PopularMoviesResponse?>> {
        val popularPlayingResponse = MutableLiveData<ApiResponse<PopularMoviesResponse?>>()
        val popularMoviesRequest = PopularMovieRequest.Builder()
            .setPage(page)
            .build()
        disposables.add(
            movieService.getPopularMovies(apiKey,popularMoviesRequest.page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    popularPlayingResponse.value = ApiResponse.loading(null) }
                .subscribe({
                        result -> popularPlayingResponse.value = ApiResponse.success(result)
                }, {
                        error -> popularPlayingResponse.value = error.message?.let { ApiResponse.error(it,null) }
                }))
        return popularPlayingResponse
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}