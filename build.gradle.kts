import dev.deftu.gradle.utils.DependencyType
import dev.deftu.gradle.utils.isLoomPresent

plugins {
    java

    id("dev.deftu.gradle.multiversion")

    id("dev.deftu.gradle.tools")
    id("dev.deftu.gradle.tools.resources")
    id("dev.deftu.gradle.tools.minecraft.api")
    id("dev.deftu.gradle.tools.minecraft.loom")
    id("dev.deftu.gradle.tools.minecraft.releases-v2")
    id("dev.deftu.gradle.tools.publishing.maven")
    id("dev.deftu.gradle.tools.publishing.github")
}

repositories {
    maven("https://api.modrinth.com/maven")
}

dependencies {
    if (mcData.isFabric) {
        modImplementation("net.fabricmc.fabric-api:fabric-api:${mcData.dependencies.fabric.fabricApiVersion}")
    }
}

fabricApi {
    configureDataGeneration {
        client = true
    }
}

java {
    base.archivesName.set(modData.id)

    tasks {
        if (isLoomPresent()) {
            named<org.gradle.jvm.tasks.Jar>("remapJar") {
                archiveBaseName.set(modData.id)
            }
        } else {
            named<Jar>("jar") {
                archiveBaseName.set(modData.id)
            }
        }
    }
}

toolkitReleasesV2 {
    detectVersionType.set(true)

    projectIds {
        modrinth.set("IxcERKCt")
        curseforge.set("1246655")
    }

    rootProject.file("changelogs/${modData.version}.md").let { file ->
        if (file.exists())
            changelog.content.set(file.readText())
    }

    if (mcData.isFabric) {
        mod("fabric-api", DependencyType.REQUIRED) {
            projectId.set("fabric-api")
        }
    }
}

toolkitGitHubPublishing {
    owner.set("TheClashFruit")
    repository.set("DataDrivenGUIs")

    useSourcesJar.set(true)

    rootProject.file("changelogs/${modData.version}.md").let { file ->
        if (file.exists())
            changelogFile.set(file)
    }
}

toolkitMavenPublishing {
    forceLowercase.set(true)

    setupPublication.set(true)
    setupRepositories.set(false)

    repositories {
        maven {
            name = "tcfReleases"
            url = uri("https://mvn.theclashfruit.me/releases")

            credentials(PasswordCredentials::class)
            authentication {
                create<BasicAuthentication>("basic")
            }
        }

        maven {
            name = "tcfSnapshots"
            url = uri("https://mvn.theclashfruit.me/snapshots")

            credentials(PasswordCredentials::class)
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
}