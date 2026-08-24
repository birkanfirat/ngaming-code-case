plugins {
    alias(libs.plugins.ngamingcase.android.library)
    alias(libs.plugins.ngamingcase.hilt)
}

android {
    namespace = "com.ngaming.ngamingcase.core.network"

    buildFeatures.buildConfig = true

    defaultConfig {
        buildConfigField("String", "BASE_URL", "\"https://jsonplaceholder.typicode.com/\"")
    }
}

dependencies {
    api(project(":core:common"))

    api(platform(libs.okhttp.bom))
    api(libs.retrofit.core)
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit.serialization)
    implementation(libs.kotlinx.serialization.json)
}
