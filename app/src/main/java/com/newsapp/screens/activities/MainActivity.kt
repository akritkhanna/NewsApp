package com.newsapp.screens.activities

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.newsapp.NewsViewModel
import com.newsapp.adapters.NewsRecyclerAdapter
import com.newsapp.databinding.ActivityMainBinding
import com.newsapp.models.ArticlesItem
import com.newsapp.services.FetchNewsService
import com.newsapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("NotifyDataSetChanged")
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: NewsRecyclerAdapter
    private val articles = mutableListOf<ArticlesItem>()


    private val viewModel by viewModels<NewsViewModel>()

    private val fetchNewsReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val shouldFetch = intent?.getBooleanExtra("shouldFetch", false)

            if (shouldFetch != null && shouldFetch){
                adapter.notifyDataSetChanged()
                articles.clear()
                viewModel.getTopHeadlines("in", 50)

            }

        }
    }





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        adapter = NewsRecyclerAdapter(articles)
        binding.articleRV.adapter = adapter


        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
              return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if(direction == ItemTouchHelper.LEFT){
                    articles[viewHolder.adapterPosition].isRead = true;
                    Toast.makeText(this@MainActivity, "Marked as read.", Toast.LENGTH_LONG).show()
                }else if (direction == ItemTouchHelper.RIGHT){
                    articles.removeAt(viewHolder.adapterPosition)
                    Toast.makeText(this@MainActivity, "News deleted.", Toast.LENGTH_LONG).show()

                }
                adapter.notifyDataSetChanged()

            }

        }

       ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.articleRV)

        viewModel.topHeadlines.observe(this, { response ->
            when (response) {
                is Resource.Loading -> {
                    binding.articlePB.visibility = View.VISIBLE

                }

                is Resource.Success -> {
                    binding.articlePB.visibility = View.GONE
                    response.data?.let { articles.addAll(it.articles) }
                    adapter.notifyDataSetChanged()
                    startService(Intent(this, FetchNewsService::class.java))


                }

                is Resource.Error -> {
                    binding.articlePB.visibility = View.GONE
                    Toast.makeText(this, response.message, Toast.LENGTH_LONG).show()
                }
            }
        })

    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(fetchNewsReceiver)
    }

    override fun onStop() {
        unregisterReceiver(fetchNewsReceiver)
        super.onStop()
    }

    override fun onDestroy() {
        stopService(Intent(this, FetchNewsService::class.java))
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(fetchNewsReceiver, IntentFilter(packageName+"fetch_news"))
    }
}