package com.example.githubuserinfo.model

import com.google.gson.annotations.SerializedName

data class UserDetailInfo (
    @field:SerializedName("id") var id: Long,
    @field:SerializedName("login") var login: String? = null,
    @field:SerializedName("avatar_url") var avatarUrl: String? = null,
    @field:SerializedName("site_admin") var isSiteAdmin: Boolean = false,
    @field:SerializedName("bio") var bio: String = "",
    @field:SerializedName("name") var name: String = "",
    @field:SerializedName("location") var location: String = "",
    @field:SerializedName("blogUrl") var blogUrl: String = ""
)