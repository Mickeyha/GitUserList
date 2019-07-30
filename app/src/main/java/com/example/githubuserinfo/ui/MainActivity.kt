package com.example.githubuserinfo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil
import com.example.githubuserinfo.R
import com.example.githubuserinfo.databinding.ActivityMainBinding
import com.example.githubuserinfo.viewmodel.UserInfoViewModel

import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    val viewModel by viewModel<UserInfoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this@MainActivity, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.userInfoViewModel = viewModel


        setSupportActionBar(toolbar)
    }
}
