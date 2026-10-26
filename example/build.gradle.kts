plugins {
    application
    alias(libs.plugins.spotless)
}

application {
    mainModule = "xyz.keibit.bcu.example"
    mainClass = "xyz.keibit.bcu.example.Main"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

dependencies {
    implementation(project(":core"))
}

spotless {
    java {
        googleJavaFormat().aosp()
    }
}
