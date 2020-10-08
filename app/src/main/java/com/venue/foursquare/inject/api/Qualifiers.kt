package com.venue.foursquare.inject.api

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.ANNOTATION_CLASS
import kotlin.reflect.KClass

@Target(ANNOTATION_CLASS)
annotation class RetrofitInstanceQualifier

@Qualifier
@Retention(RUNTIME)
@RetrofitInstanceQualifier
annotation class Apis

@Qualifier
@Retention(RUNTIME)
@RetrofitInstanceQualifier
annotation class ApiMedia

@Qualifier
@Retention(RUNTIME)
annotation class NetworkInterceptor

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Reload(val type: KClass<out Any>)
