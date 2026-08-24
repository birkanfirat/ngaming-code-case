plugins {
    alias(libs.plugins.ngamingcase.android.library)
    alias(libs.plugins.ngamingcase.hilt)
}

android {
    namespace = "com.ngaming.ngamingcase.posts.ui"
    buildFeatures.viewBinding = true
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":feature:posts:domain"))

    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.kotlinx.coroutines.android)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
}
