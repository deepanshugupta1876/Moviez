package deepanshu.example.com.moviez.modules.movie.activity

import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import deepanshu.example.com.moviez.R
import deepanshu.example.com.moviez.core.base.BaseActivity
import deepanshu.example.com.moviez.core.base.BaseRecyclerviewAdapter
import deepanshu.example.com.moviez.core.base.callbacks.PaginationListener
import deepanshu.example.com.moviez.core.network.Status
import deepanshu.example.com.moviez.databinding.ActivityMovieListBinding
import deepanshu.example.com.moviez.modules.movie.adapter.NowPlayingRecyclerviewAdapter
import deepanshu.example.com.moviez.modules.movie.adapter.PopularMoviesRecyclerviewAdapter
import deepanshu.example.com.moviez.modules.movie.api.response.Movie
import deepanshu.example.com.moviez.modules.movie.api.response.PopularMoviesResponse
import deepanshu.example.com.moviez.modules.movie.viewmodel.MovieListingViewmodel

class MovieListActivity : BaseActivity(), BaseRecyclerviewAdapter.OnItemClickListener<Movie> {
    private lateinit var mBinding: ActivityMovieListBinding
    private lateinit var mViewModel: MovieListingViewmodel
    private lateinit var mPopularAdapter: PopularMoviesRecyclerviewAdapter

    private var currentPagePopular: Int = POPULAR_PAGE_START
    private var isLastPagePopular: Boolean = false
    private var isFootLoaderVisiblePopular: Boolean = false
    private var totalPagePopular: Int = POPULAR_PAGE_START

    override fun getBinding(): ViewDataBinding = mBinding

    override fun setUp() {
        setBinding()
        setViewModel()
        makeFetchMoviesApiCall()
    }

    private fun makeFetchMoviesApiCall() {
        isInternetAvailable(object : OnInternetAvailableCallback{
            override fun onAvailable() {
                fetchMovies()
            }

            override fun onNotAvailable() {
                showMessage(getString(R.string.internet_not_availalble))
            }
        })
    }

    private fun fetchMovies() {
        mViewModel.loadPopularAndNowPlayingMoviesApi(getString(R.string.api_key),POPULAR_PAGE_START).observe(this, { response ->
            when (response.status) {
                Status.LOADING -> showLoading()
                Status.SUCCESS -> {
                    hideLoading()
                    if (response.data == null) {
                        showMessage(R.string.data_corrupted)
                        return@observe
                    }
                    setPopularRecyclerview(response.data.popularMoviesResponse)
                    setNowPlayingPager(response.data.movies)
                    return@observe
                }
                Status.ERROR -> {
                    hideLoading()
                    showMessage(response.message)
                    return@observe
                }
            }

        })
    }

    private fun makeFetchPopularMoviesApiCall() {
        isInternetAvailable(object : OnInternetAvailableCallback{
            override fun onAvailable() {
                fetchPopularMovies()
            }

            override fun onNotAvailable() {
                showMessage(R.string.internet_not_availalble)
            }
        })
    }

    private fun fetchPopularMovies() {
        mViewModel.loadPopularMovieApi(getString(R.string.api_key),currentPagePopular).observe(this, { response ->
            when (response.status) {
                Status.LOADING ->{}
                Status.SUCCESS -> {
                    if (response.data == null) {
                        showMessage(getString(R.string.data_corrupted))
                        return@observe
                    }
                    updatePopularMovies(response.data)
                    return@observe
                }
                Status.ERROR -> {
                    showMessage(response.message)
                    return@observe
                }
            }
        })
    }

    private fun isFirstPage(): Boolean {
        return currentPagePopular == POPULAR_PAGE_START
    }

    private fun removeLoadingFooter() {
        if (!isFirstPage()) {
            mPopularAdapter.removeFootLoader()
            isFootLoaderVisiblePopular = false
        }
    }

    private fun updatePopularMovies(data: PopularMoviesResponse) {
        totalPagePopular = data.totalPages
        currentPagePopular = data.page
        removeLoadingFooter()
        mPopularAdapter.addItems(data.movies)
        mPopularAdapter.addFootLoader()
    }

    private fun setPopularRecyclerview(data: PopularMoviesResponse) {
        totalPagePopular = data.totalPages
        val layoutManager = LinearLayoutManager(this)
        mBinding.popularMovies.layoutManager = layoutManager
        mPopularAdapter = PopularMoviesRecyclerviewAdapter(this)
        mPopularAdapter.addItems(data.movies)
        mBinding.popularMovies.adapter = mPopularAdapter
        mBinding.popularMovies.addOnScrollListener(object : PaginationListener(layoutManager) {
            override fun isLastPage(): Boolean = isLastPagePopular

            override fun loadMoreItems() {
                if (currentPagePopular < totalPagePopular) {
                    isFootLoaderVisiblePopular = true
                    currentPagePopular++
                    loadNextPage()
                } else {
                    isLastPagePopular = true
                }
            }

            override fun isLoading(): Boolean = isFootLoaderVisiblePopular

            override fun getItemPerPage(): Int = data.page

        })
    }

    private fun loadNextPage() {
        makeFetchPopularMoviesApiCall()
    }


    private fun setNowPlayingPager(nowPlayingMovies: List<Movie>) {
        mBinding.nowPlayingViewPager.adapter = NowPlayingRecyclerviewAdapter(nowPlayingMovies,this )
        mBinding.indicatorDot.setViewPager(mBinding.nowPlayingViewPager)
    }

    override fun getProgressBar(): ProgressBar = mBinding.progressBar

    private fun setBinding() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_list)
    }

    private fun setViewModel() {
        mViewModel = ViewModelProviders.of(this).get(MovieListingViewmodel::class.java)
    }

    companion object {
        const val POPULAR_PAGE_START: Int = 1
    }

    override fun onItemClick(item: Movie) {
        navigateToMovieDetailScreen(item.id)
    }

    private fun navigateToMovieDetailScreen(id: Int) {
        MovieDetailActivity.getInstance(this,id).also { startActivity(it) }
    }

}