plugins {
    `java-library`
}

dependencies {
    api(project(":hivemq-mqtt-spring-boot"))
    api(rootProject.libs.hivemq.client)

    implementation(rootProject.libs.spring.boot)
    implementation(rootProject.libs.spring.boot.autoconfigure)
}

publishing {
    publications {
        create<MavenPublication>("autoconfiguration") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
            from(components["java"])
        }
    }
}