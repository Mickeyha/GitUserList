package com.example.githubuserinfo.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserinfo.R
import com.example.githubuserinfo.model.UserInfo
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_user_info.view.*

class UserInfoListAdapter : PagedListAdapter<UserInfo, UserInfoViewHolder>(UserDiff) {

    var onItemClick = MutableLiveData<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserInfoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(
            R.layout.item_user_info,
            parent,
            false
        )

        return UserInfoViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserInfoViewHolder, position: Int) {
        getItem(position)?.let { userInfo ->
            // user avatar
            Glide.with(holder.itemView.context)
                .load(userInfo.avatarUrl)
                .placeholder(R.color.colorAvatarPlaceHolder)
                .into(holder.avatarImageView)

            // user login
            userInfo.login?.run {
                holder.loginTextView.text = userInfo.login

                // onItemClick listener
                holder.itemView.setOnClickListener {
                    onItemClick.value = userInfo.login
                }
            }

            // user badge
            if (userInfo.isSiteAdmin) {
                holder.badgeTextView.visibility = View.VISIBLE
            } else {
                holder.badgeTextView.visibility = View.GONE
            }
        }
    }

}


class UserInfoViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    val avatarImageView: CircleImageView = itemView.image_user
    val loginTextView: TextView = itemView.text_login
    val badgeTextView: TextView = itemView.text_badge
}

object UserDiff: DiffUtil.ItemCallback<UserInfo> () {
    override fun areItemsTheSame(oldItem: UserInfo, newItem: UserInfo): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: UserInfo, newItem: UserInfo): Boolean = oldItem == newItem
}