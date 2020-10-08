package com.venue.foursquare.inject.api.module

import com.venue.foursquare.inject.api.NetworkInterceptor
import dagger.Module
import dagger.multibindings.Multibinds
import okhttp3.Interceptor

@Module
abstract class InterceptorsModule {
  @Multibinds
  abstract fun interceptors(): Set<Interceptor>

  @Multibinds
  @NetworkInterceptor
  abstract fun netInterceptors(): Set<Interceptor>
}
