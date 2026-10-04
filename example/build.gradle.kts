plugins {
    application
}

repositories {
    mavenCentral()
}

application {
    mainClass = "bcu.example.Main"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(27)
    }
}

dependencies {
    implementation(project(":core"))
    runtimeOnly("com.sun.xml.ws:jaxws-rt:4.0.5")
}
