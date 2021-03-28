package deepanshu.example.com.moviez.modules.movie.activity

import android.content.Context
import android.content.Intent
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import deepanshu.example.com.moviez.R
import deepanshu.example.com.moviez.core.base.BaseActivity
import deepanshu.example.com.moviez.core.network.Status
import deepanshu.example.com.moviez.databinding.ActivityMovieCastBinding
import deepanshu.example.com.moviez.modules.movie.adapter.MovieCastAdapter
import deepanshu.example.com.moviez.modules.movie.api.response.Cast
import deepanshu.example.com.moviez.modules.movie.viewmodel.MovieCastViewModel

class MovieCastActivity : BaseActivity() {
    private lateinit var mBinding:ActivityMovieCastBinding
    private lateinit var mViewModel: MovieCastViewModel

    override fun getBinding(): ViewDataBinding = mBinding

    override fun setUp() {
        setTitle("Casts")
        initBinding()
        initViewModel()
        makeMovieCastApiCall()
    }

    private fun makeMovieCastApiCall() {
        isInternetAvailable(object : OnInternetAvailableCallback{
            override fun onAvailable() {
                fetchMovieCast()
            }

            override fun onNotAvailable() {
                showMessage(R.string.internet_not_availalble)
            }
        })
    }

    private fun fetchMovieCast() {
        val movieId = intent.getIntExtra(KEY_MOVIE_ID,0)
        mViewModel.loadMovieCastApi(getString(R.string.api_key),movieId).observe(this, { response ->
            when(response.status) {
                Status.LOADING -> showLoading()
                Status.SUCCESS -> {
                    hideLoading()
                    if (response.data == null) {
                        showMessage(R.string.data_corrupted)
                        return@observe
                    }

                    setCastRecyclerview(response.data.cast)
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

    private fun setCastRecyclerview(casts: List<Cast>) {
        mBinding.castList.layoutManager = GridLayoutManager(this,2)
        val adapter = MovieCastAdapter(casts)
        mBinding.castList.adapter = adapter
    }

    private fun initBinding() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_cast)
    }

    private fun initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(MovieCastViewModel::class.java)
    }

    override fun getProgressBar(): ProgressBar = mBinding.progressBar

    companion object {
        private const val KEY_MOVIE_ID = "key_movie_id"

        fun getInstance(context: Context, movieId: Int): Intent =
            Intent(context, MovieCastActivity::class.java).apply {
                putExtra(KEY_MOVIE_ID, movieId)
            }
    }
}