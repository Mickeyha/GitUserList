package com.example.githubuserinfo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.githubuserinfo.R

class UserDetailDialogFragment : DialogFragment() {

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

    private var userLogin: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.run {
            userLogin = this.getString(KEY_USER_INFO_LOGIN, null)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_user_detail, null, false)
    }

    override fun onStart() {
        super.onStart()

        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
        }
        dialog.window!!.setBackgroundDrawableResource(R.color.colorUserDetailBackground)
    }
}