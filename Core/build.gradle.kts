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
    implementation("org.mapstruct:mapstruct:1.6.3")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")
    implementation("com.google.code.gson:gson:2.12.1")
    implementation("commons-io:commons-io:2.15.1")
    implementation ("org.reflections:reflections:0.10.2")
}

tasks.test {
    useJUnitPlatform()
}