package deepanshu.example.com.moviez.modules.movie.adapter

import com.squareup.picasso.Picasso
import deepanshu.example.com.moviez.R
import deepanshu.example.com.moviez.core.base.BasePaginationAdapter
import deepanshu.example.com.moviez.core.network.ApiConstants
import deepanshu.example.com.moviez.databinding.ItemPopularMovieBinding
import deepanshu.example.com.moviez.modules.movie.api.response.Movie

class PopularMoviesRecyclerviewAdapter(listener: OnItemClickListener<Movie>) :
    BasePaginationAdapter<Movie>(listener) {

    override fun getEmptyItem(): Movie = Movie()

    override fun getItemForPosition(position: Int): Movie = dataList[position]

    override fun getLayoutIdForPosition(position: Int): Int = R.layout.item_popular_movie

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: BaseViewHolder<Movie>, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder.getBinding() is ItemPopularMovieBinding) {
            val binding: ItemPopularMovieBinding = holder.getBinding() as ItemPopularMovieBinding
            val path = ApiConstants.IMAGE_BASE_URL + getItemForPosition(position).posterPath
            Picasso.get().load(path)
                .fit()
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .into(binding.banner)
        }
    }
}