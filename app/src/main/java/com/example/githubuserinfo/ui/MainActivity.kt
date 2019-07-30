package com.example.githubuserinfo.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuserinfo.R
import com.example.githubuserinfo.databinding.ActivityMainBinding
import com.example.githubuserinfo.model.UserInfo
import com.example.githubuserinfo.viewmodel.UserInfoViewModel

import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModel<UserInfoViewModel>()
    private val userInfoListAdapter = UserInfoListAdapter()
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =
            DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.userInfoViewModel = viewModel


        initAdapter()

        // get users
        viewModel.getUsers()
        viewModel.userInfoList.observe(this, Observer<PagedList<UserInfo>> {
            userInfoListAdapter.submitList(it)
        })

        handleProgressBar()
        handleErrorMessage()
        userInfoListAdapter.onItemClick.observe(this, Observer {
            UserDetailDialogFragment.createInstance(it)
                .show(supportFragmentManager, UserDetailDialogFragment::class.java.name)
        })

        setSupportActionBar(toolbar)
    }

    private fun initAdapter() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
            adapter = userInfoListAdapter
        }
    }

    // show/hide progress bar
    private fun handleProgressBar() {
        viewModel.isLoading.observe(this, Observer {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })
    }

    // show error toast
    private fun handleErrorMessage() {
        viewModel.errorMessage.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }
}
