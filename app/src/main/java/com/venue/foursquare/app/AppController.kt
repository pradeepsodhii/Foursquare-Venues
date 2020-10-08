package com.venue.foursquare.app

import android.os.StrictMode
import androidx.appcompat.app.AppCompatDelegate
import com.venue.foursquare.BuildConfig
import com.venue.foursquare.arch.dagger.DomainComponent
import com.venue.foursquare.arch.dagger.DomainDaggerApplication
import com.venue.foursquare.inject.app.AppComponent
import com.venue.foursquare.inject.app.DaggerAppComponent
import timber.log.Timber

class AppController : DomainDaggerApplication() {

    init {
        instance = this
    }

    companion object {
        private var instance: AppController? = null

        fun getInstance(): AppController {
            return instance as AppController
        }
    }


    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectActivityLeaks()
                    .penaltyLog()
                    .build()
            )
        }
    }

      override val daggerComponent: DomainComponent
        get() {
            return component
        }

    override val component: AppComponent by lazy {
        (DaggerAppComponent.builder() as AppComponent.Builder)
            .app(this)
            .build()
    }
}