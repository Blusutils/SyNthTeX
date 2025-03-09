plugins {
    kotlin("multiplatform") version "2.0.21"
    id("com.vanniktech.maven.publish") version "0.31.0"
    `maven-publish`
    signing
}

group = "net.blusutils.synthtex"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvm()
    js {
        browser()
        nodejs()
        binaries.executable()
    }
    iosArm64()
    iosX64()
    macosX64()
    macosArm64()
    linuxX64()
    mingwX64()

    sourceSets {
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

mavenPublishing {
    publishToMavenCentral(com.vanniktech.maven.publish.SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()

    coordinates(group.toString(), "synthtex", version.toString())

    pom {
        name = "SyNthTeX"
        description = "Simple parser for TeX-like syntax"
        inceptionYear = "2025"
        url = "https://github.com/Blusutils/SyNthTeX"
        licenses {
            license {
                name = "MIT License"
                url = "https://opensource.org/licenses/MIT"
            }
        }
        developers {
            developer {
                id = "EgorBron"
                name = "Egor Bron"
                url = "https://github.com/EgorBron"
                email = "bron@blusutils.net"
            }
        }
        scm {
            url = "https://github.com/Blusutils/SyNthTeX"
            connection = "scm:git:git://github.com/Blusutils/SyNthTeX.git"
            developerConnection = "scm:git:ssh://github.com/Blusutils/SyNthTeX.git"
        }
    }
}