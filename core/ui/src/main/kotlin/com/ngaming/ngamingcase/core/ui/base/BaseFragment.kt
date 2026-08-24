package com.ngaming.ngamingcase.core.ui.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/** Fragment'ın binding'ini view ömrü boyunca tutuyor, view yok olunca bırakıyor. Ekranı kurma işi onViewReady içinde yapılıyor. */
abstract class BaseFragment<VB : ViewBinding>(
    @LayoutRes contentLayoutId: Int,
    private val bind: (View) -> VB,
) : Fragment(contentLayoutId) {

    private var viewBinding: VB? = null

    protected val binding: VB
        get() = checkNotNull(viewBinding) { "${javaClass.simpleName} view henüz üretilmedi" }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = bind(view)
        onViewReady(savedInstanceState)
    }

    override fun onDestroyView() {
        viewBinding = null
        super.onDestroyView()
    }

    protected abstract fun onViewReady(savedInstanceState: Bundle?)
}
