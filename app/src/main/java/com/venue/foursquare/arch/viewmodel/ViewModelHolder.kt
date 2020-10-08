package com.venue.foursquare.arch.viewmodel

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner

interface ViewModelHolder<VM : ViewModel> {
  @get:MainThread
  val viewModel: VM

  companion object {
    inline fun <reified VM : ViewModel> moduleMethod(
      storeOwner: ViewModelStoreOwner,
      adaptedFactory: ProviderAdaptedFactory<VM>
    ) = object : ViewModelHolder<VM> {
      @get:MainThread
      override val viewModel: VM by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(storeOwner, adaptedFactory)
          .get<VM>(VM::class.java)
          .also {}
      }
    }

    inline fun <reified VM : ViewModel> of(crossinline getFunc: () -> VM) =
      object : ViewModelHolder<VM> {
        @get:MainThread
        override val viewModel: VM
          get() = getFunc()
      }
  }
}
