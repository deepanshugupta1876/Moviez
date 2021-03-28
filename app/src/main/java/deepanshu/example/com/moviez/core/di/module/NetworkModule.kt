package deepanshu.example.com.moviez.core.di.module

import dagger.Module
import dagger.Provides
import dagger.Reusable
import deepanshu.example.com.moviez.core.network.ApiConstants
import deepanshu.example.com.moviez.core.network.RetrofitClient
import deepanshu.example.com.moviez.modules.movie.api.services.MovieService
import retrofit2.Retrofit

/**
 * Module which provides all required dependencies about network
 */
@Module
// Safe here as we are dealing with a Dagger 2 module
object NetworkModule {
    /**
     * Provides the Post service implementation.
     * @param retrofit the Retrofit object used to instantiate the service
     * @return the Post service implementation.
     */
    @Provides
    @Reusable
    @JvmStatic
    internal fun provideMovieApi(retrofit: Retrofit): MovieService {
        return retrofit.create(MovieService::class.java)
    }

    /**
     * Provides the Retrofit object.
     * @return the Retrofit object
     */
    @Provides
    @Reusable
    @JvmStatic
    internal fun provideRetrofitInterface(): Retrofit {
        return RetrofitClient.getRetrofitInstance(ApiConstants.BASE_URL)
    }

}