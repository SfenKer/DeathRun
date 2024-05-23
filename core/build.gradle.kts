import com.palantir.gradle.gitversion.VersionDetails
import groovy.lang.Closure

plugins {
    id("java")
    id("net.kyori.blossom") version "1.3.1"
    id("com.palantir.git-version") version "3.0.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

val versionDetails: Closure<VersionDetails> by extra

project.group = project.parent?.group!!
project.version = project.parent?.version!!

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

blossom {
    replaceToken("{version}", project.version)
    replaceToken("{gitBranch}", versionDetails().branchName)
    replaceToken("{gitCommitHash}", versionDetails().gitHashFull)
}

repositories {
    mavenCentral()
    maven("https://repo.mrstudios.pl/public/")
    maven("https://maven.enginehub.org/repo/")
    maven("https://repo.panda-lang.org/releases/")
    maven("https://repo.opencollab.dev/maven-releases/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://storehouse.okaeri.eu/repository/maven-public/")
}

dependencies {

    shadow(project(":api"))

    /* Minecraft */
    compileOnly("com.destroystokyo.paper:paper-api:${project.parent?.property("minecraft.version")}")

    /* Lite Commands */
    shadow("dev.rollczi:litecommands-core:${project.parent?.property("litecommands.version")}")
    shadow("dev.rollczi:litecommands-bukkit:${project.parent?.property("litecommands.version")}")

    /* Okaeri Configs */
    shadow("eu.okaeri:okaeri-configs-yaml-bukkit:${project.parent?.property("okaeri.configs.version")}")
    shadow("eu.okaeri:okaeri-configs-serdes-bukkit:${project.parent?.property("okaeri.configs.version")}")

    /* Commons */
    shadow("pl.mrstudios.commons:commons-bukkit:${project.parent?.property("mrstudios.commons.version")}")
    shadow("pl.mrstudios.commons:commons-inject:${project.parent?.property("mrstudios.commons.version")}")
    shadow("pl.mrstudios.commons:commons-reflection:${project.parent?.property("mrstudios.commons.version")}")

    /* Kyori Adventure */
    shadow("net.kyori:adventure-api:${project.parent?.property("kyori.adventure.version")}")
    shadow("net.kyori:adventure-text-minimessage:${project.parent?.property("kyori.adventure.version")}")

    /* Kyori Adventure Platform */
    shadow("net.kyori:adventure-platform-bukkit:${project.parent?.property("kyori.adventure.platform.version")}")
    shadow("net.kyori:adventure-text-serializer-bungeecord:${project.parent?.property("kyori.adventure.platform.version")}")

    /* Apache Commons IO */
    shadow("commons-io:commons-io:${project.parent?.property("apache.commons.io.version")}")

    /* Protocol Sidebar */
    shadow("me.catcoder:bukkit-sidebar:${project.parent?.property("protocol.sidebar.version")}")

    /* WorldEdit */
    compileOnly("com.sk89q.worldedit:worldedit-bukkit:${project.parent?.property("worldedit.version")}")

    /* JetBrains Annotations */
    compileOnly("org.jetbrains:annotations:${project.parent?.property("jetbrains.annotations.version")}")
    annotationProcessor("org.jetbrains:annotations:${project.parent?.property("jetbrains.annotations.version")}")

}

tasks {

    processResources {
        inputs.property("version", project.version)
        expand(inputs.properties)
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    jar {
        dependsOn(shadowJar)
        manifest {
            attributes["Git-Branch"] = versionDetails().branchName
            attributes["Git-Commit"] = versionDetails().gitHashFull
        }
    }

    shadowJar {

        configurations = listOf(project.configurations.shadow.get())
        archiveFileName.set("${project.parent?.name}-${project.name}-${project.version}.jar")

        exclude("META-INF/**")
        dependencies {
            isEnableRelocation = true
            relocationPrefix = "${project.parent?.group}.libraries"
        }

        relocate("pl.mrstudios.deathrun.api", "pl.mrstudios.deathrun.api")

    }

    build {
        finalizedBy("finalize")
    }

    register("finalize") {
        doLast {
            file("build/libs/${project.name}-${project.version}.jar").delete()
        }
    }

}