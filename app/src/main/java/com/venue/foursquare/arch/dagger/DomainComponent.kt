package com.venue.foursquare.arch.dagger

import dagger.android.DispatchingAndroidInjector

interface DomainComponent {
    val androidInjector: DispatchingAndroidInjector<Any>
}
