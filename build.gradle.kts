plugins {
    id("java")
}

group = "io.github.btarg"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.javalin:javalin:5.+")
    implementation("org.slf4j:slf4j-simple:2.+")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.+")
    implementation("dev.langchain4j:langchain4j-open-ai:0.25.0")
    implementation("dev.langchain4j:langchain4j:0.25.0")
}