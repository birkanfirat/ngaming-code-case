pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ngaming-case"

include(":app")
include(":core:common")
include(":core:network")
include(":core:ui")
include(":feature:posts:domain")
include(":feature:posts:data")
include(":feature:posts:ui")
