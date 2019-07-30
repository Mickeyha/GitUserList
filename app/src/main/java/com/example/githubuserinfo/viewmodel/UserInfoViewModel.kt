package com.example.githubuserinfo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.example.githubuserinfo.model.UserInfo
import com.example.githubuserinfo.repo.UserInfoRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber
import java.util.concurrent.TimeUnit

class UserInfoViewModel: BaseViewModel(), KoinComponent {
    private val repo: UserInfoRepository by inject()

    val isLoading = MutableLiveData<Boolean>()
    val userInfoList = MutableLiveData<PagedList<UserInfo>>()
    val errorMessage = MutableLiveData<String>()

    fun getUsers() {
        compositeDisposable +=
                repo.getUsers()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .timeout(15, TimeUnit.SECONDS)
                    .doOnSubscribe { isLoading.value = true }
                    .doOnError {  }
                    .doOnNext { isLoading.value = false }
                    .subscribe({
                        userInfoList.value = it
                    },{
                        Timber.e("getUsers failed. $it")
                        errorMessage.postValue(it.toString())
                    })
    }
}