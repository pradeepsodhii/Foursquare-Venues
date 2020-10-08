package com.venue.foursquare.arch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class ProviderAdaptedFactory<VM : ViewModel>
@Inject constructor(private val modelProvider: Provider<VM>) :
  ViewModelProvider.Factory {
  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel> create(modelClass: Class<T>): T = modelProvider.get() as T
}