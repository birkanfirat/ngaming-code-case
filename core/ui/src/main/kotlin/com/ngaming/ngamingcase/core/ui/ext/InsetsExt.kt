package com.ngaming.ngamingcase.core.ui.ext

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

/** Status bar ve gesture bar'ın kapladığı alanı padding olarak ekliyor. Ekran kenardan kenara çizildiği için gerekli. */
fun View.applySystemBarInsets(top: Boolean = false, bottom: Boolean = false) {
    val declaredTop = paddingTop
    val declaredBottom = paddingBottom

    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        view.updatePadding(
            top = if (top) declaredTop + bars.top else declaredTop,
            bottom = if (bottom) declaredBottom + bars.bottom else declaredBottom,
        )
        insets
    }
}
