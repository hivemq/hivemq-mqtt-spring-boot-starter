plugins {
    java
}

dependencies {
    implementation(rootProject.libs.hivemq.client)

    implementation(rootProject.libs.spring.boot)
    implementation(rootProject.libs.spring.boot.autoconfigure)
}

publishing {
    publications {
        create<MavenPublication>("library") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
            from(components["java"])
        }
    }
}