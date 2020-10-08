package com.venue.foursquare.inject.app

import com.venue.foursquare.app.AppController
import com.venue.foursquare.arch.dagger.DomainComponent
import com.venue.foursquare.inject.api.ApiSessionScope
import com.venue.foursquare.inject.api.module.ApiModule
import com.venue.foursquare.inject.module.ActivityListingModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@ApiSessionScope
@Component(modules = [
    AppModule::class,
    ActivityListingModule::class,
    AndroidSupportInjectionModule::class,
    ApiModule::class
])

interface AppComponent : AndroidInjector<AppController>, DomainComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun app(app: AppController): Builder
        fun build(): AppComponent
    }
}
