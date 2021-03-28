package deepanshu.example.com.moviez.core.base

import androidx.lifecycle.ViewModel
import deepanshu.example.com.moviez.core.di.component.DaggerViewModelInjector
import deepanshu.example.com.moviez.core.di.component.ViewModelInjector
import deepanshu.example.com.moviez.core.di.module.NetworkModule
import deepanshu.example.com.moviez.modules.movie.viewmodel.MovieCastViewModel
import deepanshu.example.com.moviez.modules.movie.viewmodel.MovieDetailViewModel
import deepanshu.example.com.moviez.modules.movie.viewmodel.MovieListingViewmodel
import deepanshu.example.com.moviez.modules.movie.viewmodel.MovieReviewViewModel

abstract class BaseViewModel: ViewModel() {

    private val injector: ViewModelInjector = DaggerViewModelInjector
        .builder()
        .networkModule(NetworkModule)
        .build()

    init {
        inject()
    }

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
            is MovieListingViewmodel -> injector.inject(this)
            is MovieDetailViewModel -> injector.inject(this)
            is MovieCastViewModel -> injector.inject(this)
            is MovieReviewViewModel -> injector.inject(this)
        }
    }
}