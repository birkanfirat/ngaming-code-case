plugins {
    alias(libs.plugins.ngamingcase.android.library)
    alias(libs.plugins.ngamingcase.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.ngaming.ngamingcase.posts.data"
}

dependencies {
    implementation(project(":core:network"))
    implementation(project(":feature:posts:domain"))
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit.core)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
}
