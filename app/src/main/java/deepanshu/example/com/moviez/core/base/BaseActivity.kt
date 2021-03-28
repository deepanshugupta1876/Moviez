package deepanshu.example.com.moviez.core.base

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar

abstract class BaseActivity : AppCompatActivity() {

    abstract fun getBinding(): ViewDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUp()
    }

    abstract fun setUp()

    fun showMessage(@StringRes errorMessage: Int) {
        showMessage(getString(errorMessage))
    }

    fun showMessage(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    fun showSnackBar(@StringRes errorMessage: Int) {
        showSnackBar(getString(errorMessage))
    }

    fun showSnackBar(errorMessage: String) {
        Snackbar.make(getBinding().root, errorMessage, Snackbar.LENGTH_SHORT).show()
    }

    abstract fun getProgressBar(): ProgressBar?

    fun showLoading() {
        if (getProgressBar() != null) {
            getProgressBar()!!.visibility = View.VISIBLE
        }
    }

    fun hideLoading() {
        if (getProgressBar() != null) {
            getProgressBar()!!.visibility = View.GONE
        }
    }

    fun isInternetAvailable(listener: OnInternetAvailableCallback?) {
        val connection =
            ConnectionStateMonitor(
                applicationContext
            )
        connection.observe(this, { isInternetOn ->
            if (listener != null) {
                when (isInternetOn) {
                    true -> {
                        listener.onAvailable()
                        connection.callInActive()
                    }
                    false -> listener.onNotAvailable()
                }
            }
        })
    }

    interface OnInternetAvailableCallback {
        fun onAvailable()

        fun onNotAvailable()
    }
}