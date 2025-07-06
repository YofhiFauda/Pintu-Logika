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

rootProject.name = "Pintu Logika"
include(":app")
include(":features:kelola_materi")
include(":features:kelola_kuis_materi")
include(":features:tampilkan_materi")
include(":core:core_ui")
include(":features:halaman_materi")
include(":core:core_data")
include(":features:halaman_simulasi")
