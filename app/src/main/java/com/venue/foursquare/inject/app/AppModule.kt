package com.venue.foursquare.inject.app

import android.app.Application
import android.content.Context
import com.venue.foursquare.app.AppController
import com.venue.foursquare.arch.dagger.DomainDaggerApplication
import dagger.Binds
import dagger.Module

@Module(
    includes = [
        AppModule.TypeBindings::class
    ]
)
class AppModule {
    @Module
    abstract class TypeBindings {
        @Binds
        abstract fun app2Context(impl: AppController): Context

        @Binds
        abstract fun app2app(impl: AppController): Application

        @Binds
        abstract fun app2DomainApp(impl: AppController): DomainDaggerApplication
    }

}