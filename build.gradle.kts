import org.gradle.api.publish.internal.component.MavenPublishingAwareVariant

plugins {
    id("java")
}

group = "com.github.joshuacgunn"
version = "v0.2.0"

repositories {
    mavenCentral()
    maven { url = uri("https://www.jetbrains.com/intellij-repository/releases") }
    allprojects {
        repositories {
            maven { url = uri("https://jitpack.io") }
        }
    }
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "io.game.core.gameplay.PlayGame"
    }
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation(project(":Core"))
    implementation("org.mapstruct:mapstruct:1.6.3")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")
    implementation("commons-io:commons-io:2.15.1")
    implementation("org.slf4j:slf4j-api:2.0.12")
    implementation("com.google.code.gson:gson:2.12.1")
    implementation ("org.reflections:reflections:0.10.2")
    implementation("ch.qos.logback:logback-classic:1.5.6")
    implementation("com.github.javafaker:javafaker:1.0.2")
    implementation("org.jline:jline-terminal:3.21.0")
    implementation("org.jline:jline-reader:3.21.0")
    implementation("org.hexworks.zircon:zircon.core-jvm:2021.1.0-RELEASE")
    implementation("org.hexworks.zircon:zircon.jvm.swing:2021.1.0-RELEASE")
}

tasks.test {
    useJUnitPlatform()
}