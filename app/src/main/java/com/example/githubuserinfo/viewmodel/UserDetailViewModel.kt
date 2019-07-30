package com.example.githubuserinfo.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.githubuserinfo.api.GithubService
import com.example.githubuserinfo.model.UserDetailInfo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber
import java.util.concurrent.TimeUnit

class UserDetailViewModel : BaseViewModel(), KoinComponent {

    private val service: GithubService by inject()
    val userDetail = MutableLiveData<UserDetailInfo>()
    val isLoading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()

    fun getUserDetail(login: String) {
        compositeDisposable +=
            service.getUserDetail(login)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(15, TimeUnit.SECONDS)
                .doOnSubscribe {
                    isLoading.value = true
                }
                .doOnError{
                    Timber.e("onError, $it")
                }
                .doOnEvent{ _,_ ->
                    isLoading.value = false
                }
                .subscribe({
                    Timber.d("result = $it")
                    userDetail.value = it
                }, {
                    Timber.e("getUserDetail failed, $it")
                    errorMessage.postValue(it.toString())
                })
    }
}