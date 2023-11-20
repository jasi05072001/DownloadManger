pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()

        //jitpack
        maven { url = uri("https://jitpack.io") }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        maven { url = uri("https://jitpack.io") }
        mavenCentral()
    }
}

rootProject.name = "DownloadManger"
include(":app")
include(":downloadManager")
