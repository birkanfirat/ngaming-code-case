plugins {
    alias(libs.plugins.ngamingcase.android.library)
}

android {
    namespace = "com.ngaming.ngamingcase.core.ui"
    buildFeatures.viewBinding = true
}

dependencies {
    api(project(":core:common"))

    api(libs.androidx.core.ktx)
    api(libs.androidx.appcompat)
    api(libs.androidx.fragment.ktx)
    api(libs.androidx.lifecycle.runtime.ktx)
    api(libs.material)
    api(libs.glide.core)
}
