// Los plugins se declaran acá para que core y example compartan la misma instancia.
plugins {
    alias(libs.plugins.lombok) apply false
    alias(libs.plugins.maven.publish) apply false
    alias(libs.plugins.spotless) apply false
}
