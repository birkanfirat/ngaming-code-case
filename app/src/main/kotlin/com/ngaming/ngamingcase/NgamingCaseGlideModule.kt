package com.ngaming.ngamingcase

import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

/** Glide'ın kendi kodunu üretmesi için gerekli. Açılışta manifest taramasını atlatıyor. */
@GlideModule
class NgamingCaseGlideModule : AppGlideModule()
