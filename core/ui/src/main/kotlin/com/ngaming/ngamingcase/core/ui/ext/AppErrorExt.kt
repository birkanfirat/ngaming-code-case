package com.ngaming.ngamingcase.core.ui.ext

import android.content.Context
import com.ngaming.ngamingcase.core.common.AppError
import com.ngaming.ngamingcase.core.ui.R

/** Hata tipini kullanıcının göreceği metne çeviriyor. Metinler strings.xml'den geldiği için Türkçe/İngilizce hazır. */
fun AppError.toMessage(context: Context): String = when (this) {
    AppError.Network -> context.getString(R.string.error_network)
    is AppError.Server -> context.getString(R.string.error_server, code)
    is AppError.Validation -> when (field) {
        AppError.Validation.Field.TITLE -> context.getString(R.string.error_title_required)
        AppError.Validation.Field.BODY -> context.getString(R.string.error_body_required)
    }
    AppError.Unknown -> context.getString(R.string.error_unknown)
}
