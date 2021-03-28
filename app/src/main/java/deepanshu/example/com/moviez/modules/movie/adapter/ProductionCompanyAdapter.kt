package deepanshu.example.com.moviez.modules.movie.adapter

import com.squareup.picasso.Picasso
import deepanshu.example.com.moviez.R
import deepanshu.example.com.moviez.core.base.BaseRecyclerviewAdapter
import deepanshu.example.com.moviez.core.network.ApiConstants
import deepanshu.example.com.moviez.databinding.ItemProductionCompanyBinding
import deepanshu.example.com.moviez.modules.movie.api.response.ProductionCompany

class ProductionCompanyAdapter(private val productionCompanies:List<ProductionCompany>):BaseRecyclerviewAdapter<ProductionCompany>(null) {

    override fun getItemForPosition(position: Int): ProductionCompany = productionCompanies[position]

    override fun getLayoutIdForPosition(position: Int): Int = R.layout.item_production_company

    override fun getItemCount(): Int = productionCompanies.size

    override fun onBindViewHolder(holder: BaseViewHolder<ProductionCompany>, position: Int) {
        super.onBindViewHolder(holder, position)
        val binding: ItemProductionCompanyBinding = holder.getBinding() as ItemProductionCompanyBinding
        val path = ApiConstants.IMAGE_BASE_URL + getItemForPosition(position).logoPath
        Picasso.get().load(path)
            .placeholder(R.drawable.ic_placeholder)
            .error(R.drawable.ic_placeholder)
            .into(binding.logo)
    }
}