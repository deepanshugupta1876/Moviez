package deepanshu.example.com.moviez.modules.movie.adapter

import com.squareup.picasso.Picasso
import deepanshu.example.com.moviez.R
import deepanshu.example.com.moviez.core.base.BaseRecyclerviewAdapter
import deepanshu.example.com.moviez.core.network.ApiConstants
import deepanshu.example.com.moviez.databinding.ItemMovieReviewBinding
import deepanshu.example.com.moviez.modules.movie.api.response.Review

class MovieReviewAdapter(private val reviews:List<Review>):BaseRecyclerviewAdapter<Review>(null) {

    override fun getItemForPosition(position: Int): Review = reviews[position]

    override fun getLayoutIdForPosition(position: Int): Int = R.layout.item_movie_review

    override fun getItemCount(): Int = reviews.size

    override fun onBindViewHolder(holder: BaseViewHolder<Review>, position: Int) {
        super.onBindViewHolder(holder, position)
        val binding:ItemMovieReviewBinding = holder.getBinding() as ItemMovieReviewBinding
        val path = ApiConstants.IMAGE_BASE_URL + getItemForPosition(position).authorDetails.avatarPath
        Picasso.get().load(path)
            .fit()
            .placeholder(R.drawable.ic_placeholder)
            .error(R.drawable.ic_placeholder)
            .into(binding.profileImage)
    }
}