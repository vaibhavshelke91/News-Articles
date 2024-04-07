package com.vaibhav.newsarticles

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.vaibhav.newsarticles.api.News
import com.vaibhav.newsarticles.databinding.ActivityFullNewsBinding

class FullNewsActivity : AppCompatActivity() {
    private val binding by lazy { ActivityFullNewsBinding.inflate(layoutInflater) }


    // get the News Class from intent using lazy implementation
    val item by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("item", News::class.java)
        } else {
            intent.getSerializableExtra("item") as News
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.mToolbar.setNavigationOnClickListener {
            finish()
        }
        // loaded the image of news
        Glide.with(this)
            .load(item?.urlToImage)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.img)

        binding.title.text=item?.title

        val publish = item?.publishedAt?.split("T")?.get(0)
        binding.dateAndName.text="${item?.author}  $publish"
        binding.source.text="${item?.source?.name}"
        binding.description.text=item?.description

        // when the user clicked on full button then open the new browser window with news link
        binding.full.setOnClickListener {
            startActivity(
                Intent(Intent.ACTION_VIEW,
                    Uri.parse(item?.url))
            )
        }
    }
}