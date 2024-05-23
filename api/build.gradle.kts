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
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {

    /* Minecraft */
    compileOnly("com.destroystokyo.paper:paper-api:${project.parent?.property("minecraft.version")}")

    /* JetBrains Annotations */
    compileOnly("org.jetbrains:annotations:${project.parent?.property("jetbrains.annotations.version")}")
    annotationProcessor("org.jetbrains:annotations:${project.parent?.property("jetbrains.annotations.version")}")

}

tasks {

    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    jar {
        manifest {
            attributes["Git-Branch"] = versionDetails().branchName
            attributes["Git-Commit"] = versionDetails().gitHashFull
        }
    }

    shadowJar {
        archiveFileName.set("${project.parent?.name}-${project.name}-${project.version}.jar")
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