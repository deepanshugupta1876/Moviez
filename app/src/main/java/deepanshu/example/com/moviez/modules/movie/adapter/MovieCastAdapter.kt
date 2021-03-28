package deepanshu.example.com.moviez.modules.movie.adapter

import com.squareup.picasso.Picasso
import deepanshu.example.com.moviez.R
import deepanshu.example.com.moviez.core.base.BaseRecyclerviewAdapter
import deepanshu.example.com.moviez.core.network.ApiConstants
import deepanshu.example.com.moviez.databinding.ItemMovieCastBinding
import deepanshu.example.com.moviez.modules.movie.api.response.Cast

class MovieCastAdapter(private val movieCasts:List<Cast>): BaseRecyclerviewAdapter<Cast>(null) {

    override fun getItemForPosition(position: Int): Cast = movieCasts[position]

    override fun getLayoutIdForPosition(position: Int): Int = R.layout.item_movie_cast

    override fun getItemCount(): Int = movieCasts.size

    override fun onBindViewHolder(holder: BaseViewHolder<Cast>, position: Int) {
        super.onBindViewHolder(holder, position)
        val binding: ItemMovieCastBinding = holder.getBinding() as ItemMovieCastBinding
        val path= ApiConstants.IMAGE_BASE_URL + getItemForPosition(position).profilePath
        Picasso.get().load(path)
            .fit()
            .placeholder(R.drawable.ic_placeholder)
            .error(R.drawable.ic_placeholder)
            .into(binding.profileIamge)
    }
}