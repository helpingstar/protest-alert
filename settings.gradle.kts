pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
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

rootProject.name = "ProtestAlert"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")
include(":feature:schedule:impl")
include(":feature:settings:impl")
include(":core:data")
include(":core:designsystem")
include(":core:common")
include(":core:network")
include(":sync:work")
include(":core:database")
include(":core:model")
include(":core:datastore")
include(":core:datastore-proto")
include(":core:notifications")
include(":core:ui")
include(":core:domain")
include(":feature:schedule:api")
include(":feature:settings:api")
include(":core:navigation")
