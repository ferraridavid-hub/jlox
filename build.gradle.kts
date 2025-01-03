plugins {
    id("application")
}

group = "com.primeur"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

application {
    mainClass = "com.primeur.Lox"
}

tasks.test {
    useJUnitPlatform()
}