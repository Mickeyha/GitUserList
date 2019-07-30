package com.example.githubuserinfo.model

import com.google.gson.annotations.SerializedName

data class UserInfo (
    @field:SerializedName("id") var id: Long,
    @field:SerializedName("login") var login: String? = null,
    @field:SerializedName("avatar_url") var avatarUrl: String? = null,
    @field:SerializedName("site_admin") var isSiteAdmin: Boolean = false
)