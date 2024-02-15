plugins {
    java
}

dependencies {
    annotationProcessor(rootProject.libs.spring.boot.configuration.processor)

    implementation(rootProject.libs.bcpkix.lts8on)
    implementation(rootProject.libs.hivemq.client)
    implementation(rootProject.libs.slf4j.api)
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