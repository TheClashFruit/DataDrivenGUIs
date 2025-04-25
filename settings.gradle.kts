pluginManagement {
    repositories {
        maven("https://maven.deftu.dev/releases")
        maven("https://maven.fabricmc.net")
        maven("https://maven.architectury.dev/")
        maven("https://maven.minecraftforge.net")
        maven("https://maven.neoforged.net/releases")
        maven("https://repo.essential.gg/repository/maven-public")
        maven("https://server.bbkr.space/artifactory/libs-release/")
        maven("https://jitpack.io/")

        maven("https://maven.deftu.dev/snapshots")
        mavenLocal()

        gradlePluginPortal()
        mavenCentral()
    }

    plugins {
        id("dev.deftu.gradle.multiversion-root") version("2.33.3")
    }
}

val projectName: String = "Data Driven GUIs"

rootProject.name          = projectName
rootProject.buildFileName = "root.gradle.kts"

listOf(
    "1.21-fabric",
    "1.21.1-fabric",
    "1.21.2-fabric",
    "1.21.3-fabric",
    "1.21.4-fabric",
    "1.21.5-fabric",
).forEach { version ->
    include(":$version")
    project(":$version").apply {
        projectDir    = file("versions/$version")
        buildFileName = "../../build.gradle.kts"
    }
}