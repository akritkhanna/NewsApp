package com.newsapp.screens.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.newsapp.NewsViewModel
import com.newsapp.adapters.NewsRecyclerAdapter
import com.newsapp.databinding.ActivityMainBinding
import com.newsapp.models.ArticlesItem
import com.newsapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: NewsRecyclerAdapter
    private val articles = mutableListOf<ArticlesItem>()

    private val viewModel by viewModels<NewsViewModel>()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        adapter = NewsRecyclerAdapter(articles)
        binding.articleRV.adapter = adapter


        //val viewModel: NewsViewModel = ViewModelProvider(this).get(NewsViewModel::class.java)

        viewModel.topHeadlines.observe(this, { response ->
            when (response) {
                is Resource.Loading -> {
                    binding.articlePB.visibility = View.VISIBLE

                }

                is Resource.Success -> {
                    binding.articlePB.visibility = View.GONE
                    response.data?.let { articles.addAll(it.articles) }
                    adapter.notifyDataSetChanged()

                }

                is Resource.Error -> {
                    binding.articlePB.visibility = View.GONE
                    Toast.makeText(this, response.message, Toast.LENGTH_LONG).show()
                }
            }
        })

    }
}