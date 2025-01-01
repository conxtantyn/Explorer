pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Explorer"

include(":core:common")
include(":core:ui")

include(":discovery:domain")
include(":discovery:data")
include(":discovery:local")
include(":discovery:ui")

include(":android")
