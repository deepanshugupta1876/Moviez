package deepanshu.example.com.moviez.core.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import deepanshu.example.com.moviez.BR

abstract class BaseRecyclerviewAdapter<U>(private val itemClickListener: OnItemClickListener<U>?) :
    RecyclerView.Adapter<BaseRecyclerviewAdapter.BaseViewHolder<U>>() {

    interface OnItemClickListener<U> {
        fun onItemClick(item: U)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<U> {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding =
            DataBindingUtil.inflate(layoutInflater, viewType, parent, false)
        return BaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<U>, position: Int) {
        val item: U = getItemForPosition(position) ?: return
        holder.bind(item)
        holder.itemView.setOnClickListener { itemClickListener?.onItemClick(item) }
    }

    override fun getItemViewType(position: Int): Int {
        return getLayoutIdForPosition(position)
    }

    protected abstract fun getItemForPosition(position: Int): U

    @LayoutRes
    protected abstract fun getLayoutIdForPosition(position: Int): Int

    open class BaseViewHolder<U>(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: U) {
            binding.setVariable(BR.obj, item)
        }

        fun getBinding() : ViewDataBinding {
            return binding
        }
    }
}