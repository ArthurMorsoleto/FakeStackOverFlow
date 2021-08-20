package com.amb.fakestackoverflow.di

import android.app.Application
import com.amb.fakestackoverflow.view.MainActivity
import dagger.Component

@Component(modules = [StackOverFlowModule::class])
interface ApplicationComponent {
    fun inject(mainActivity: MainActivity)
}


class MyApplication : Application() {
    val appComponent: ApplicationComponent =
        DaggerApplicationComponent.create()
}