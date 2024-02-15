plugins {
    `java-library`
    `maven-publish`
    signing
    alias(libs.plugins.gradle.versions)
    alias(libs.plugins.spring.dependency.management)
}

allprojects {
    apply(plugin = "maven-publish")

    group = "com.hivemq"
    description = "Spring Boot Starter to integrate HiveMQ MQTT Client SDK"

    publishing {
        repositories {
            maven {
                name = "default"
                url = uri(layout.buildDirectory.dir("repo"))
            }
        }
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "signing")
    apply(plugin = "io.spring.dependency-management")

    dependencyManagement {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:${rootProject.libs.versions.spring.boot.get()}")
        }
    }

    repositories {
        mavenCentral()
    }

    java {
        withSourcesJar()
        withJavadocJar()
    }

    artifacts {
        archives(tasks["jar"])
        archives(tasks["sourcesJar"])
        archives(tasks["javadocJar"])
    }

    dependencies {
    }
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":hivemq-mqtt-spring-boot-autoconfigure"))
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("starter") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
            from(components["java"])

            pom {

            }
        }
    }
    repositories {
        maven {
            url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2")
            credentials {
                username = findProperty("sonatypeUsername") as String? ?: ""
                password = findProperty("sonatypePassword") as String? ?: ""
            }
        }
    }
}

allprojects {
    signing {
        val signingKey: String? by project
        val signingPassword: String? by project
        useInMemoryPgpKeys(signingKey, signingPassword)
        publishing.publications.configureEach {
            sign(this)
        }
    }
}


