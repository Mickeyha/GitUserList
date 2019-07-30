package com.example.githubuserinfo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.githubuserinfo.R
import com.example.githubuserinfo.databinding.FragmentUserDetailBinding
import com.example.githubuserinfo.viewmodel.UserDetailViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserDetailDialogFragment : DialogFragment() {

    lateinit var binding: FragmentUserDetailBinding
    private val userDetail by viewModel<UserDetailViewModel>()
    private var userLogin: String? = null

    companion object {
        const val KEY_USER_INFO_LOGIN = "key_user_info_login"

        fun createInstance(login: String):  UserDetailDialogFragment {
            val userDetailDialogFragment = UserDetailDialogFragment()
            val bundle = Bundle()

            bundle.putString(KEY_USER_INFO_LOGIN, login)
            userDetailDialogFragment.arguments = bundle
            return userDetailDialogFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.run {
            userLogin = this.getString(KEY_USER_INFO_LOGIN, null)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_user_detail, null, false)
        binding.lifecycleOwner = this
        binding.detailViewModel = userDetail
        binding.imageClose.setOnClickListener { dialog.dismiss() }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        initDialogLayout()

        startGetUserDetail()

        observeIsLoading()
        observeUserDetail()
        observeErrorMessage()
    }

    private fun observeErrorMessage() {
        userDetail.errorMessage.observe(this, Observer<String> {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun observeUserDetail() {
        userDetail.userDetail.observe(this, Observer {
            // avatar
            Glide.with(requireContext())
                .load(it.avatarUrl)
                .placeholder(R.color.colorUserAvatarPlaceHolderColor)
                .into(binding.imageUserPic)

            binding.viewUserDetailInfo.setData(login = it.login, isSiteAdmin = it.isSiteAdmin)
            binding.viewUserDetailLoc.setData(it.location)
            binding.viewUserDetailBlog.setData(it.blogUrl)
        })
    }

    private fun observeIsLoading() {
        userDetail.isLoading.observe(this, Observer {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })
    }

    private fun startGetUserDetail() {
        userLogin?.run {
            userDetail.getUserDetail(this)
        }
    }

    private fun initDialogLayout() {
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
        }
        dialog.window!!.setBackgroundDrawableResource(R.color.colorUserDetailBackground)
    }
}