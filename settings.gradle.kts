pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.neoforged.net/releases/")
        maven("https://maven.kikugie.dev/snapshots")
    }
    buildscript {
        dependencies {
            // Need this for some reason or else IntelliJ doesn't load gradle properly
            classpath("gradle.plugin.org.jetbrains.gradle.plugin.idea-ext:gradle-idea-ext:1.4.1")
        }
    }
}

rootProject.name = "ClickUI"

plugins {
    id("dev.kikugie.stonecutter") version "0.10-alpha.11"
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
        version("26.1", "fabric", "neoforge")
        version("26.2", "fabric", "neoforge")
        version("26.3", "fabric", "neoforge")
        vcsVersion = "26.3-fabric"
    }
}