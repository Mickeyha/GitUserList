package com.example.githubuserinfo.datasource

import android.annotation.SuppressLint
import android.webkit.URLUtil
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.example.githubuserinfo.api.GithubService
import com.example.githubuserinfo.model.UserInfo
import com.example.githubuserinfo.utils.ResponseUtil
import com.example.githubuserinfo.utils.ResponseUtil.Companion.HEADER_KEY_LINK
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

class UserInfoDataSource : PageKeyedDataSource<String, UserInfo>(), KoinComponent {

    companion object {
        const val SINCE_INITIAL = 0
        const val SIZE_PER_PAGE = 20
    }

    private val service: GithubService by inject()


    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<String, UserInfo>) {

        service.getUsers(SINCE_INITIAL, SIZE_PER_PAGE)
            .enqueue(object : Callback<List<UserInfo>> {
                override fun onResponse(call: Call<List<UserInfo>>, response: Response<List<UserInfo>>) {
                   if (response.isSuccessful) {
                       response.body()?.run {
                           val nextPageUrl =
                               ResponseUtil.INSTANCE.parseNextPageUrl(response.headers().get(HEADER_KEY_LINK).toString())

                           val userInfoList: List<UserInfo> = response.body()!!
                           callback.onResult(userInfoList, null, nextPageUrl)
                       }
                   }
                }

                override fun onFailure(call: Call<List<UserInfo>>, t: Throwable) {
                    t.run {
                        when(t) {
                            is SocketTimeoutException -> {
                                Timber.e("SocketTimeoutException, getUsers failed. $t")
                            }
                            is IOException -> {
                                Timber.e("IOException, getUsers failed. $t")
                            }
                            else -> {
                                Timber.e("getUsers failed. $t")
                            }
                        }
                    }
                }

            })
    }

    @SuppressLint("CheckResult")
    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, UserInfo>) {
        val url = params.key
        if (URLUtil.isValidUrl(url)) {
            service.getUsers(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(15, TimeUnit.SECONDS)
                .retry(2)
                .subscribe( { response ->
                    if (response.isSuccessful) {
                        response.body()?.run {
                            val nextUrl =
                                ResponseUtil.INSTANCE.parseNextPageUrl(response.headers().get(HEADER_KEY_LINK).toString())

                            val userInfoList: List<UserInfo> = response.body()!!
                            callback.onResult(userInfoList, nextUrl)
                        }
                    }
                }, {
                    Timber.e("loadAfter failed. $it")
                })
        }
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, UserInfo>) {
        // Nothing to do here
    }

}

class UserInfoDataSourceFactory : DataSource.Factory<String, UserInfo>() {
    override fun create(): DataSource<String, UserInfo> = UserInfoDataSource()
}