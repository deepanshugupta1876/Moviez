package deepanshu.example.com.moviez.modules.movie.adapter

import com.squareup.picasso.Picasso
import deepanshu.example.com.moviez.R
import deepanshu.example.com.moviez.core.base.BaseRecyclerviewAdapter
import deepanshu.example.com.moviez.core.network.ApiConstants
import deepanshu.example.com.moviez.databinding.ItemNowPlayingBinding
import deepanshu.example.com.moviez.modules.movie.api.response.Movie

class NowPlayingRecyclerviewAdapter(private val movies:List<Movie>, listener: OnItemClickListener<Movie>): BaseRecyclerviewAdapter<Movie>(listener) {

    override fun getItemForPosition(position: Int): Movie = movies[position]

    override fun getLayoutIdForPosition(position: Int): Int = R.layout.item_now_playing

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: BaseViewHolder<Movie>, position: Int) {
        super.onBindViewHolder(holder, position)
        val binding: ItemNowPlayingBinding = holder.getBinding() as ItemNowPlayingBinding
        val path = ApiConstants.IMAGE_BASE_URL + getItemForPosition(position).posterPath
         Picasso.get().load(path)
             .fit()
             .placeholder(R.drawable.ic_placeholder)
             .error(R.drawable.ic_placeholder)
             .into(binding.banner)
    }
}