package com.newsapp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.newsapp.databinding.ArticleListLayoutBinding
import com.newsapp.models.ArticlesItem
import com.newsapp.util.Tools

class NewsRecyclerAdapter(private val articles: List<ArticlesItem>) :
    RecyclerView.Adapter<NewsRecyclerAdapter.NewsViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NewsViewHolder(
        ArticleListLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.binding.titleTV.text = articles[position].title;
        holder.binding.descriptionTV.text = articles[position].description;
        holder.binding.sourceTV.text = "Source : " + articles[position].source?.name ?: "-";

        holder.binding.publishedAtTV.text =
            articles[position].publishedAt?.let { Tools.utcToLocal(it, "dd MMM, yyyy") }

        articles[position].urlToImage?.let {
            Glide.with(holder.itemView.context).load(it).into(holder.binding.articleIV)
        }
        Log.d("TAG", "onBindViewHolder: shit"+articles[position].isRead)
        if(articles[position].isRead){
            holder.binding.readStatusTV.text = "Status : Read"
        }else{
            holder.binding.readStatusTV.visibility = View.GONE
        }
    }

    override fun getItemCount() = articles.size

    inner class NewsViewHolder(val binding: ArticleListLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)
}