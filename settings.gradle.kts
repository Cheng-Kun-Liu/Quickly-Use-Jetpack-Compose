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

rootProject.name = "quickly"

include(":app")

include(":res")
include(":core-ui")

include(":core-logic:common")
include(":core-logic:analytics")
include(":core-logic:authenticate")
include(":core-logic:notification")
include(":core-logic:model")
include(":core-logic:network:http")
include(":core-logic:database")
include(":core-logic:repository")
include(":core-logic:location")
include(":core-logic:language")

include(":core-launcher")

include(":feature:main")
include(":feature:integrations")
include(":feature:chat")
include(":feature:samples")
include(":feature:video")
include(":feature:webview")
include(":feature:settings")

include(":flavor:flavor-gp")
include(":flavor:flavor-sam")

include(":baseline-profile")
