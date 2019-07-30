package com.example.githubuserinfo.repo

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.example.githubuserinfo.datasource.UserInfoDataSourceFactory
import com.example.githubuserinfo.model.UserInfo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import io.reactivex.Observable

class UserInfoRepository : KoinComponent {
    companion object {
        const val PAGE_SIZE = 20
        const val PREFETCH_DISTANCE = 4
    }

    fun getUsers(): Observable<PagedList<UserInfo>> {
        val dataSource = UserInfoDataSourceFactory()
        val pagedLIstConfig = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setPrefetchDistance(PREFETCH_DISTANCE)
            .build()

        return RxPagedListBuilder(dataSource, pagedLIstConfig)
            .setFetchScheduler(Schedulers.io())
            .setNotifyScheduler(AndroidSchedulers.mainThread())
            .buildObservable()
    }
}