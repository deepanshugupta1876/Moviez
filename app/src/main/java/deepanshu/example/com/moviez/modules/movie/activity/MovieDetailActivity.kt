package deepanshu.example.com.moviez.modules.movie.activity

import android.content.Context
import android.content.Intent
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import deepanshu.example.com.moviez.R
import deepanshu.example.com.moviez.core.base.BaseActivity
import deepanshu.example.com.moviez.core.base.BaseRecyclerviewAdapter
import deepanshu.example.com.moviez.core.network.ApiConstants
import deepanshu.example.com.moviez.core.network.Status
import deepanshu.example.com.moviez.databinding.ActivityMovieDetailBinding
import deepanshu.example.com.moviez.modules.movie.adapter.ProductionCompanyAdapter
import deepanshu.example.com.moviez.modules.movie.adapter.SimilarMoviesAdapter
import deepanshu.example.com.moviez.modules.movie.api.response.Movie
import deepanshu.example.com.moviez.modules.movie.api.response.ProductionCompany
import deepanshu.example.com.moviez.modules.movie.viewmodel.MovieDetailViewModel

class MovieDetailActivity : BaseActivity() {
    private lateinit var mBinding: ActivityMovieDetailBinding
    private lateinit var mViewModel: MovieDetailViewModel
    private var movieId:Int = 0

    override fun getBinding(): ViewDataBinding = mBinding

    override fun setUp() {
        setTitle("Movie Detail")
        initBinding()
        initViewModel()
        getExtraIntentData()
        addListener()
        makeMovieDetailApiCall()
    }

    private fun makeMovieDetailApiCall() {
        isInternetAvailable(object : OnInternetAvailableCallback{
            override fun onAvailable() {
                fetchMovieDetail()
            }

            override fun onNotAvailable() {
                showMessage(R.string.internet_not_availalble)
            }
        })
    }

    private fun getExtraIntentData() {
        movieId = intent.getIntExtra(KEY_MOVIE_ID,0)
    }

    private fun addListener() {
        mBinding.castBtn.setOnClickListener { navigateToCastDetail() }
        mBinding.reviewBtn.setOnClickListener { navigateToReviews() }
    }

    private fun navigateToReviews() {
        MovieReviewActivity.getInstance(this, movieId).also { startActivity(it) }
    }

    private fun navigateToCastDetail() {
        MovieCastActivity.getInstance(this,movieId).also { startActivity(it) }
    }

    private fun fetchMovieDetail() {
        val movieId = intent.getIntExtra(KEY_MOVIE_ID, 0)

        mViewModel.loadMovieDetailWithSimilarMoviesApi(getString(R.string.api_key), movieId)
            .observe(this, Observer { response ->
                when (response.status) {
                    Status.LOADING -> showLoading()
                    Status.SUCCESS -> {
                        hideLoading()
                        if (response.data == null) {
                            showMessage(R.string.data_corrupted)
                            return@Observer
                        }
                        mBinding.obj = response.data
                        val path = ApiConstants.IMAGE_BASE_URL + response.data.posterPath
                        Picasso.get().load(path)
                            .fit()
                            .placeholder(R.drawable.ic_placeholder)
                            .error(R.drawable.ic_placeholder)
                            .into(mBinding.banner)
                        setProductionCompanyRecyclerview(response.data.productionCompanies)
                        setSimilarMovieRecyclerview(response.data.similarMovieResponse.movies)
                    }
                    Status.ERROR -> {
                        hideLoading()
                        showMessage(response.message)
                    }
                }
            })

    }

    private fun setSimilarMovieRecyclerview(movies: List<Movie>) {
        mBinding.similarMovieList.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        mBinding.similarMovieList.adapter = SimilarMoviesAdapter(movies,
            object : BaseRecyclerviewAdapter.OnItemClickListener<Movie> {
                override fun onItemClick(item: Movie) {
                    navigateToMovieDetail(item)
                }
            })
    }

    private fun navigateToMovieDetail(item: Movie) {
        getInstance(this, item.id).also { startActivity(it) }
    }

    private fun setProductionCompanyRecyclerview(productionCompanies: List<ProductionCompany>) {
        mBinding.productinCompanyList.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        mBinding.productinCompanyList.adapter = ProductionCompanyAdapter(productionCompanies)
    }

    private fun initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(MovieDetailViewModel::class.java)
    }

    private fun initBinding() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail)
    }

    override fun getProgressBar(): ProgressBar = mBinding.progressBar

    companion object {
        private const val KEY_MOVIE_ID = "key_movie_id"

        fun getInstance(context: Context, movieId: Int): Intent =
            Intent(context, MovieDetailActivity::class.java).apply {
                putExtra(KEY_MOVIE_ID, movieId)
            }
    }
}