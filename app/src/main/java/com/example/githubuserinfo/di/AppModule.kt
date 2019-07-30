package com.example.githubuserinfo.di

import com.example.githubuserinfo.api.GithubService
import com.example.githubuserinfo.api.GithubService.Companion.GITHUB_BASE_URL
import com.example.githubuserinfo.repo.UserInfoRepository
import com.example.githubuserinfo.viewmodel.UserInfoViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single { generateOkHttpClient() }
    single { generateRetrofit(get()) }
    single { generateGithubService(get()) }
}

val repositoryModule = module {
    factory { UserInfoRepository() }
}

val viewModelModule = module {
    viewModel{ UserInfoViewModel() }
}

fun generateOkHttpClient(): OkHttpClient =
    OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor())
        .build()

fun generateRetrofit(okHttpClient: OkHttpClient): Retrofit =
    Retrofit.Builder()
        .baseUrl(GITHUB_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

fun generateGithubService(retrofit: Retrofit): GithubService =
        retrofit.create(GithubService::class.java)


val appModule = listOf (
    networkModule,
    repositoryModule,
    viewModelModule
)
