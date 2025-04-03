plugins {
    id("java")
}

group = "com.github.joshuacgunn"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation(project(":Core"))
    implementation(project(":Common"))
    implementation("commons-io:commons-io:2.15.1")
}

tasks.test {
    useJUnitPlatform()
}