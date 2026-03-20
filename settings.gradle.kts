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

rootProject.name = "android-scaffold-verify"
include(":app")
include(":feature:feature-user")
include(":feature:roadtest")
include(":feature:feature-map")
include(":feature:feature-ble")
include(":feature:feature-setting")
include(":core:model")
include(":infra:network-impl")
include(":testing:fake-network")
include(":feature:feature-location")
include(":core:ui")
include(":core:network")
include(":core:database")
include(":core:location")
include(":core:ble")
include(":core:map")
include(":core:common")
include(":infra:database-impl")
include(":infra:location-impl")
include(":infra:ble-impl")
include(":infra:map-baidu")
include(":infra:permission")
include(":infra:log")
include(":testing:fake-database")
include(":testing:fake-location")
include(":benchmarks")
