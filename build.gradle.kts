plugins {
    id("java")
    id("net.fabricmc.fabric-loom-remap") version "1.17-SNAPSHOT"
    id("maven-publish")
}

group = "de.clickism"
version = "0.1"

repositories {
    mavenCentral()
}

sourceSets {
    create("testmod") {
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.main.get().output
    }
}

loom {
    createRemapConfigurations(sourceSets["testmod"])
    mods {
        create("clickui-testmod") {
            sourceSet(sourceSets["testmod"])
        }
    }
    runs {
        create("testmodClient") {
            client()
            displayName = "Testmod Client"
            sourceSet = "testmod"
        }
    }
}

configurations {
    named("testmodCompileClasspath") {
        extendsFrom(named("compileClasspath").get())
    }

    named("testmodRuntimeClasspath") {
        extendsFrom(named("runtimeClasspath").get())
    }
}

dependencies {
    minecraft("com.mojang:minecraft:1.20.1")
    mappings(loom.officialMojangMappings())

    // Testmod-only dependencies
    "modTestmodImplementation"("net.fabricmc:fabric-loader:0.16.14")
    "modTestmodImplementation"("net.fabricmc.fabric-api:fabric-api:0.92.2+1.20.1")

    compileOnly("org.jetbrains:annotations:24.0.1")
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = group.toString()
            artifactId = "clickui"
            version = project.version.toString()
        }
    }
}