package com.amb.fakestackoverflow.di

import android.app.Application
import com.amb.fakestackoverflow.view.MainActivity
import com.amb.fakestackoverflow.view.QuestionDetailsActivity
import dagger.Component

@Component(modules = [StackOverFlowModule::class])
interface ApplicationComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(mainActivity: QuestionDetailsActivity)
}


class MyApplication : Application() {
    val appComponent: ApplicationComponent =
        DaggerApplicationComponent.create()
}