plugins {
    `java-library`
    `maven-publish`
    id("io.freefair.lombok") version "6.5.1"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.2"
}

group = "ru.spliterash"
version = "1.0.0"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))

    withSourcesJar()
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.aikar.co/content/groups/aikar/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.2-R0.1-SNAPSHOT")
}
bukkit {
    name = "InventoryUpdateEvent"
    main = "ru.spliterash.inventoryUpdateEvent.InventoryUpdatePlugin"
    apiVersion = "1.13"
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "ru.spliterash"
            artifactId = rootProject.name

            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "nexus"
            url = uri("https://repo.spliterash.ru/" + rootProject.name)
            credentials {
                username = findProperty("SPLITERASH_NEXUS_USR")?.toString()
                password = findProperty("SPLITERASH_NEXUS_PSW")?.toString()
            }
        }
    }
}