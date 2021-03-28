package deepanshu.example.com.moviez.core.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import deepanshu.example.com.moviez.R

abstract class BasePaginationAdapter<U>(listener: OnItemClickListener<U>?) :
    BaseRecyclerviewAdapter<U>(listener) {

    protected var dataList = ArrayList<U>()

    private var isLoaderVisible: Boolean = false


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<U> {
        return when (viewType) {
            LOADING -> {
                val inflater = LayoutInflater.from(parent.context)
                val binding: ViewDataBinding = DataBindingUtil.inflate(inflater, R.layout.progress_layout, parent, false)
                LoadingViewHolder(binding)
            }
            else -> {
                super.onCreateViewHolder(parent, viewType)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (isLoaderVisible) {
            if (position == dataList.size - 1) return LOADING
        }
        return super.getItemViewType(position)
    }

    fun addItems(items: List<U>) {
        dataList.addAll(items)
        notifyDataSetChanged()
    }

    fun clear() {
        isLoaderVisible = false
        dataList.clear()
        notifyDataSetChanged()
    }


    abstract fun getEmptyItem(): U

    fun addFootLoader() {
        if (isLoaderVisible) {
            return
        }
        isLoaderVisible = true
        dataList.add(getEmptyItem())
        notifyItemInserted(dataList.size - 1)
    }

    fun removeFootLoader() {
        if (!isLoaderVisible) {
            return
        }
        isLoaderVisible = false
        val position: Int = dataList.size - 1
        val item: U = getItemForPosition(position)
        if (item != null) {
            dataList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<U>, position: Int) {
        when (getItemViewType(position)) {
            LOADING -> {}
            else -> super.onBindViewHolder(holder, position)
        }
    }

    companion object {
        const val LOADING: Int = 0
    }

    class LoadingViewHolder<U>(binding: ViewDataBinding) : BaseViewHolder<U>(binding)
}