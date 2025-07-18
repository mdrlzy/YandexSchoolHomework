includeBuild("build-logic")

pluginManagement {
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

rootProject.name = "Budgetwise"
include(":app")
include(":core:domain")
include(":core:network")
include(":core:ui")
include(":core:di")
include(":feature:account:api")
include(":feature:account:impl")
include(":feature:categories:api")
include(":feature:categories:impl")
include(":feature:transactions:api")
include(":feature:transactions:impl")
include(":feature:settings")
include(":core:db")
