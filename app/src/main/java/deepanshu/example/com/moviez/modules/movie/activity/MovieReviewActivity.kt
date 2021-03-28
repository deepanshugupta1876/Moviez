package deepanshu.example.com.moviez.modules.movie.activity

import android.content.Context
import android.content.Intent
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import deepanshu.example.com.moviez.R
import deepanshu.example.com.moviez.core.base.BaseActivity
import deepanshu.example.com.moviez.core.network.Status
import deepanshu.example.com.moviez.databinding.ActivityMovieReviewBinding
import deepanshu.example.com.moviez.modules.movie.adapter.MovieReviewAdapter
import deepanshu.example.com.moviez.modules.movie.api.response.Review
import deepanshu.example.com.moviez.modules.movie.viewmodel.MovieReviewViewModel

class MovieReviewActivity : BaseActivity() {
    private lateinit var mBinding: ActivityMovieReviewBinding
    private lateinit var mViewModel: MovieReviewViewModel

    override fun getBinding(): ViewDataBinding = mBinding

    override fun setUp() {
        setTitle("Reviews")
        initBinding()
        initViewModel()
        makeFetchMovieReviewApiCall()
    }

    private fun makeFetchMovieReviewApiCall() {
        isInternetAvailable(object : OnInternetAvailableCallback{
            override fun onAvailable() {
                fetchMovieReview()
            }

            override fun onNotAvailable() {
                showMessage(R.string.internet_not_availalble)
            }
        })
    }

    private fun fetchMovieReview() {
        val movieId = intent.getIntExtra(KEY_MOVIE_ID,0)
        mViewModel.loadMMovieReviewsApi(getString(R.string.api_key),movieId).observe(this, { response ->
            when(response.status) {
                Status.LOADING -> showLoading()
                Status.SUCCESS -> {
                    hideLoading()
                    if (response.data == null) {
                        showMessage(R.string.data_corrupted)
                        return@observe
                    }

                    setReviewRecyclerview(response.data.reviews)
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

    private fun setReviewRecyclerview(reviews: List<Review>) {
        mBinding.reviewList.layoutManager = LinearLayoutManager(this)
        val adapter = MovieReviewAdapter(reviews)
        mBinding.reviewList.adapter = adapter
    }

    private fun initBinding() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_review)
    }

    private fun initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(MovieReviewViewModel::class.java)
    }

    override fun getProgressBar(): ProgressBar = mBinding.progressBar

    companion object {
        private const val KEY_MOVIE_ID = "key_movie_id"

        fun getInstance(context: Context, movieId: Int): Intent =
            Intent(context, MovieReviewActivity::class.java).apply {
                putExtra(KEY_MOVIE_ID, movieId)
            }
    }
}