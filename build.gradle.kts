
plugins {
    id("java")
}

group = "com.github.joshuacgunn"
version = "v0.2.0"

repositories {
    mavenCentral()
    maven { url = uri("https://www.jetbrains.com/intellij-repository/releases") }
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.github.joshuacgunn.core.gameplay.Game"
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
    implementation("com.intellij:forms_rt:7.0.3")
}

tasks.test {
    useJUnitPlatform()
}