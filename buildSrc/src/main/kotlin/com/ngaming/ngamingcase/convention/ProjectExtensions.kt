package com.ngaming.ngamingcase.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun VersionCatalog.int(alias: String): Int =
    findVersion(alias).orElseThrow { IllegalStateException("Missing '$alias' version in the catalog") }
        .requiredVersion.toInt()

internal val JAVA_VERSION = JavaVersion.VERSION_17

/** Bütün Android modüllerinin paylaştığı ayarlar. */
internal fun Project.configureAndroid(extension: CommonExtension) {
    extension.compileSdk = libs.int("compileSdk")
    extension.defaultConfig.minSdk = libs.int("minSdk")

    extension.compileOptions.sourceCompatibility = JAVA_VERSION
    extension.compileOptions.targetCompatibility = JAVA_VERSION

    extension.testOptions.unitTests.isReturnDefaultValues = true

    extensions.configure<KotlinAndroidProjectExtension> {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
    }
}
