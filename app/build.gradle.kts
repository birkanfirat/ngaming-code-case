plugins {
    alias(libs.plugins.ngamingcase.android.application)
    alias(libs.plugins.ngamingcase.hilt)
}

android {
    namespace = "com.ngaming.ngamingcase"

    defaultConfig {
        applicationId = "com.ngaming.ngamingcase"
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:network"))

    implementation(libs.androidx.activity.ktx)
    ksp(libs.glide.ksp)
    implementation(project(":feature:posts:ui"))
    implementation(project(":feature:posts:data"))

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
}
