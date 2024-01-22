plugins {
    `java-library`
    `maven-publish`
    id("java")
    id("com.github.johnrengelman.shadow") version "8.+"
    id("io.papermc.paperweight.userdev") version "1.+"
    id("xyz.jpenilla.run-paper") version "2.+"
    id("io.freefair.lombok") version "8.+"
}

repositories {
    mavenCentral()
    google()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.unnamed.team/repository/unnamed-public/")
}

dependencies {
    paperweight.paperDevBundle("1.20.4-R0.1-SNAPSHOT")
    api("commons-io:commons-io:2.+")
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.+")
    implementation("dev.langchain4j:langchain4j-open-ai:0.25.0")
    implementation("dev.langchain4j:langchain4j:0.25.0")
}

group = "io.github"
version = "1.0-SNAPSHOT"
description = "CraftGPT"
java.sourceCompatibility = JavaVersion.VERSION_17

tasks {
    assemble {
        dependsOn(reobfJar)
        dependsOn(shadowJar)
    }
    runServer {
        dependsOn(assemble)
        minecraftVersion("1.20.4")
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
        val props = mapOf(
            "version" to project.version
        )
        inputs.properties(props)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}