package com.mecofarid.summy.common.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider

inline fun <reified VM : ViewModel> Fragment.activityViewModelBuilder(
  noinline viewModelInitializer: () -> VM
): Lazy<VM> {
  return ViewModelLazy(
    viewModelClass = VM::class,
    storeProducer = { requireActivity().viewModelStore },
    factoryProducer(viewModelInitializer)
  )
}

fun <VM> factoryProducer(
  viewModelInitializer: () -> VM
): () -> ViewModelProvider.Factory = {
  object : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
      @Suppress("UNCHECKED_CAST")
      return viewModelInitializer.invoke() as T
    }
  }
}