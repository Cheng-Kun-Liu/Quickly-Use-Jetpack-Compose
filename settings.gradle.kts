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
include(":core-logic:http")
include(":core-logic:database")
include(":core-logic:repository")
include(":core-logic:location")
include(":core-logic:language")

include(":core-launcher")

include(":feature:demo")
include(":feature:firebase")
include(":feature:http")
include(":feature:chat")
include(":feature:date")
include(":feature:scroll")
include(":feature:biometric")
include(":feature:painter")
include(":feature:video")
include(":feature:webview")
include(":feature:setting:language")
include(":feature:setting:font")

include(":flavor:flavor-gp")
include(":flavor:flavor-sam")

include(":baseline-profile")
