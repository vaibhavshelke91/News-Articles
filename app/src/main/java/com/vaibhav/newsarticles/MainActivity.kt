package com.vaibhav.newsarticles

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.vaibhav.newsarticles.adapter.MainRecycleAdapter
import com.vaibhav.newsarticles.api.News
import com.vaibhav.newsarticles.api.NewsResult
import com.vaibhav.newsarticles.databinding.ActivityMainBinding
import com.vaibhav.newsarticles.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }
    private val adapter by lazy { MainRecycleAdapter(this) }


    // Post Notification Permission for Android 13 and Above
    private val notificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                Snackbar.make(
                    binding.mRecycleView,
                    "Notification Permission Granted !",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                Snackbar.make(
                    binding.mRecycleView,
                    "Notification Permission Denied !",
                    Snackbar.LENGTH_SHORT
                ).show()

            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Check if the permission granted or not to show Firebase Notifications.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if (!isPostNotificationPermissionGranted()){
                notificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }


        binding.mRecycleView.layoutManager = LinearLayoutManager(this)
        binding.mRecycleView.adapter = adapter

        // Sort Button to sort new article old-to-new and new-to-old
        binding.sort.setOnClickListener {
            adapter.list.reverse()
            adapter.notifyDataSetChanged()
            Snackbar.make(binding.mRecycleView, "Sorted By Date Wise", Snackbar.LENGTH_SHORT).show()
        }


        // observe the data from viewmodels
        viewModel.data.observe(this) {
            binding.mSwipeRefresh.isRefreshing = false
            when (it) {
                // Current result is loading state
                is NewsResult.Loading -> {
                    binding.mProgress.visibility = View.VISIBLE
                }
                // Success State
                is NewsResult.Success -> {
                    binding.mProgress.visibility = View.GONE
                    adapter.list = it.data as MutableList<News>
                    adapter.notifyDataSetChanged()
                }

                // Error State
                is NewsResult.Error -> {
                    Snackbar.make(binding.mRecycleView, "${it.message}", Snackbar.LENGTH_LONG)
                        .show()
                    binding.mProgress.visibility = View.GONE
                }
            }
        }
        // load all news articles from api
        viewModel.loadNews()

        // swipe to refresh
        binding.mSwipeRefresh.setOnRefreshListener {
            viewModel.loadNews()
        }

    }

    // Check the post notification permission is granted or not
    // This function only work for Android 13 and above
    private fun isPostNotificationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }


}