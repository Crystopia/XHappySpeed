plugins {
    kotlin("jvm") version "2.+"
    id("com.gradleup.shadow") version "9.+"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("de.eldoria.plugin-yml.paper") version "0.7.0"
    kotlin("plugin.serialization") version "2.+"
}

val mcVersion = properties["minecraftVerions"] as String
val projectVersion = properties["version"] as String
val projectName = properties["name"] as String
val groupID = properties["group"] as String
val mainClass = properties["main"] as String
val projectDescription = properties["description"] as String
val commandAPIVersion = properties["commandAPIVersion"] as String

group = groupID
version = projectVersion

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.jespersen.zip/releases")
}

dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    // Paper
    compileOnly("io.papermc.paper:paper-api:${mcVersion}-R0.1-SNAPSHOT")
    implementation("zip.jespersen:Kore.kt:0.1.0")
    implementation("net.crystopia.crystalshard.paper:core:1.2.6")
    implementation("net.crystopia.crystalshard.paper:custom:1.2.6")

    // Command API
    compileOnly("dev.jorel:commandapi-paper-core:${commandAPIVersion}")
    implementation("dev.jorel:commandapi-kotlin-paper:${commandAPIVersion}")
    implementation("dev.jorel:commandapi-paper-shade:${commandAPIVersion}")
}

kotlin {
    jvmToolchain(25)
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks {
    shadowJar {
        relocate("dev.jorel.commandapi", "dev.xyzjesper.package.commandapi")
    }
    runServer {
        minecraftVersion(mcVersion)
    }
}

paper {
    name = projectName
    version = version
    description = projectDescription
    main = mainClass
    authors = listOf("jespersen")
    apiVersion = "1.21"
}
