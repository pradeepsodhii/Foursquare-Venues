package com.venue.foursquare.arch.dagger

import javax.inject.Inject
import dagger.android.HasAndroidInjector
import dagger.android.AndroidInjector
import android.app.Application

abstract class DomainDaggerApplication : Application(), HasAndroidInjector {

    protected abstract val component: AndroidInjector<out DomainDaggerApplication>

    protected abstract val daggerComponent: DomainComponent

    @Volatile
    private var injectNeeded = true

    @Inject
    fun setInjected() {
        injectNeeded = false
    }

    override fun onCreate() {
        super.onCreate()
        isInjectNeeded()
    }

    private fun isInjectNeeded() {
        if (injectNeeded) {
            synchronized(this) {
                if (injectNeeded) {
                    @Suppress("UNCHECKED_CAST")
                    (component as AndroidInjector<DomainDaggerApplication>).inject(this)
                    require(!injectNeeded) { "Application was not injected" }
                }
            }
        }
    }

    override fun androidInjector(): AndroidInjector<Any> {
        isInjectNeeded()
        return daggerComponent.androidInjector
    }
}
