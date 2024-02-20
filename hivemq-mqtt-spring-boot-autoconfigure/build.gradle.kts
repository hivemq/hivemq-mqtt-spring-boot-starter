plugins {
    `java-library`
}

dependencies {
    api(project(":hivemq-mqtt-spring-boot"))
    api(rootProject.libs.hivemq.client)

    implementation(rootProject.libs.spring.boot)
    implementation(rootProject.libs.spring.boot.autoconfigure)

    testImplementation(rootProject.libs.spring.boot.starter.test)
    testImplementation(rootProject.libs.bundles.junit)
    testImplementation(rootProject.libs.bundles.testconatiners)
}

tasks.withType<Test> {
    useJUnitPlatform()
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