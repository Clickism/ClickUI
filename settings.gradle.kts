pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.neoforged.net/releases/")
    }
}

rootProject.name = "ClickUI"

plugins {
    id("dev.kikugie.stonecutter") version "0.9"
}

stonecutter {
    create(rootProject) {
        fun version(version: String, vararg loaders: String) {
            loaders.forEach {
                var loader = it.substringBeforeLast('-')
                this.version("$version-$loader", version)
                    .buildscript = "build.$it.gradle.kts"
            }
        }
        version("1.20.1", "fabric-remap", "forge")
        version("1.21.1", "fabric-remap", "neoforge")
        version("26.1", "fabric")
        vcsVersion = "1.20.1-fabric"
    }
}