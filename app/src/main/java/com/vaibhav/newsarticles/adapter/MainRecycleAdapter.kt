package com.vaibhav.newsarticles.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.vaibhav.newsarticles.FullNewsActivity
import com.vaibhav.newsarticles.api.News
import com.vaibhav.newsarticles.databinding.NewsLayoutBinding

// RecycleViewAdapter for news articles
class MainRecycleAdapter(val context: Context) :RecyclerView.Adapter<MainRecycleAdapter.ViewHolder>() {

    var list = mutableListOf<News>()

    // A ViewHolder for holding the current data
    inner class ViewHolder(val binding : NewsLayoutBinding) : RecyclerView.ViewHolder(binding.root){

        init {
            // when the user click on read full then open the browser with news link
            binding.readFull.setOnClickListener {
                context.startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse(list[adapterPosition].url))
                )
            }

            // when the user click on title then open the browser with news link
            binding.title.setOnClickListener {
                context.startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse(list[adapterPosition].url))
                )
            }

            // when the user click on readMore button then open the new Activity
            binding.readMore.setOnClickListener {
                context.startActivity(
                    Intent(context,FullNewsActivity::class.java).apply {
                        putExtra("item",list[adapterPosition])
                    }
                )
            }
            // when the user click on card button then open the new Activity
            binding.root.setOnClickListener {
                context.startActivity(
                    Intent(context,FullNewsActivity::class.java).apply {
                        putExtra("item",list[adapterPosition])
                    }
                )
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainRecycleAdapter.ViewHolder {
       return ViewHolder(
           NewsLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
       )
    }

    // Bind the all the data of recycler view
    override fun onBindViewHolder(holder: MainRecycleAdapter.ViewHolder, position: Int) {
        with(holder.binding){

            // load the thumbnail of image
            Glide.with(context)
                .load(list[position].urlToImage)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(mImg)

            title.text=list[position].title
            description.text=list[position].description
            author.text=list[position].author
            val publish = list[position].publishedAt.split("T")
            date.text="${publish[0]}\n${publish[1].dropLast(1) }"
            source.text=list[position].source.name
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}