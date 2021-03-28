package deepanshu.example.com.moviez.core.di.component

import dagger.Component
import deepanshu.example.com.moviez.core.di.module.NetworkModule
import deepanshu.example.com.moviez.modules.movie.viewmodel.MovieCastViewModel
import deepanshu.example.com.moviez.modules.movie.viewmodel.MovieDetailViewModel
import deepanshu.example.com.moviez.modules.movie.viewmodel.MovieListingViewmodel
import deepanshu.example.com.moviez.modules.movie.viewmodel.MovieReviewViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface ViewModelInjector {

    fun inject(movieListingViewmodel: MovieListingViewmodel)

    fun inject(movieDetailViewModel: MovieDetailViewModel)

    fun inject(movieCastViewModel: MovieCastViewModel)

    fun inject(movieReviewViewModel: MovieReviewViewModel)

    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector

        fun networkModule(networkModule: NetworkModule): Builder
    }
}