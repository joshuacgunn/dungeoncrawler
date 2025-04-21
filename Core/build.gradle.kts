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
    implementation("com.github.javafaker:javafaker:1.0.2")
    implementation("org.slf4j:slf4j-api:1.6.1")
    implementation("org.slf4j:slf4j-simple:1.6.1")
    implementation("com.intellij:forms_rt:7.0.3")
}


tasks.test {
    useJUnitPlatform()
}