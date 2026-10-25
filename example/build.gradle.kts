plugins {
    application
}

repositories {
    mavenCentral()
}

application {
    mainModule = "bcu.example"
    mainClass = "bcu.example.Main"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

dependencies {
    implementation(project(":core"))
}
