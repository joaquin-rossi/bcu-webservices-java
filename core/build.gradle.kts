import java.net.URI

plugins {
    `java-library`
    alias(libs.plugins.lombok)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.spotless)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

lombok {
    version = libs.versions.lombok
}

val jaxwsTools = configurations.create("jaxwsTools")
dependencies {
    api(libs.jakarta.xml.ws.api)
    implementation(libs.jaxws.rt)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)

    jaxwsTools(libs.jaxws.tools)
}

tasks.test {
    useJUnitPlatform {
        excludeTags("integration")
    }
}

// Tests contra los webservices reales del BCU; no forman parte de `check` porque dependen de la red.
val integrationTest = tasks.register<Test>("integrationTest") {
    description = "Ejecuta los tests contra los webservices reales del BCU."
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    testClassesDirs = sourceSets.test.get().output.classesDirs
    classpath = sourceSets.test.get().runtimeClasspath
    useJUnitPlatform {
        includeTags("integration")
    }
}

tasks.javadoc {
    // Se genera sin module-info para poder excluir el código de wsimport, que no es parte de la API.
    modularity.inferModulePath = false
    exclude("module-info.java", "xyz/keibit/bcu/wsdl/**")
    (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:all,-missing", "-quiet")
}

spotless {
    java {
        targetExclude("src/main/java/xyz/keibit/bcu/wsdl/**")
        googleJavaFormat().aosp()
    }
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    coordinates(group.toString(), "bcu-webservices", version.toString())

    pom {
        name = "BCU WebServices"
        description = "Cliente Java para los webservices SOAP de cotizaciones del Banco Central del Uruguay."
        inceptionYear = "2025"
        url = "https://github.com/joaquin-rossi/bcu-webservices-java"
        licenses {
            license {
                name = "GNU Affero General Public License v3.0"
                url = "https://www.gnu.org/licenses/agpl-3.0.txt"
                distribution = "repo"
            }
        }
        developers {
            developer {
                id = "joaquin-rossi"
                name = "Joaquín Rossi"
                url = "https://github.com/joaquin-rossi"
            }
        }
        scm {
            url = "https://github.com/joaquin-rossi/bcu-webservices-java"
            connection = "scm:git:https://github.com/joaquin-rossi/bcu-webservices-java.git"
            developerConnection = "scm:git:ssh://git@github.com/joaquin-rossi/bcu-webservices-java.git"
        }
    }
}

val wsdlDir = layout.projectDirectory.dir("src/main/resources/wsdl")

// Descarga el WSDL publicado por el BCU a src/main/resources/wsdl.
fun registerWsdlDownloadTask(taskName: String, wsdlUrl: String, wsdlFileName: String) =
    tasks.register(taskName) {
        val wsdlFile = wsdlDir.file(wsdlFileName).asFile
        outputs.file(wsdlFile)
        outputs.upToDateWhen { false }
        doLast {
            wsdlFile.parentFile.mkdirs()
            URI.create(wsdlUrl).toURL().openStream().use { input ->
                wsdlFile.outputStream().use { output -> input.copyTo(output) }
            }
        }
    }

// Genera el cliente a partir del WSDL local; en runtime el WSDL se lee del classpath, sin acceder a la red.
fun registerWsImportTask(taskName: String, wsdlFileName: String, pkg: String) =
    tasks.register<JavaExec>(taskName) {
        classpath = jaxwsTools
        mainClass.set("com.sun.tools.ws.WsImport")

        args(
            "-keep",
            "-Xnocompile",
            "-s", layout.projectDirectory.dir("src/main/java").asFile.absolutePath,
            "-p", pkg,
            "-wsdllocation", "/wsdl/$wsdlFileName",
            wsdlDir.file(wsdlFileName).asFile.absolutePath
        )
    }

val taskWsdlDownloadBcuCotizaciones = registerWsdlDownloadTask(
    "wsdl-download-bcu-cotizaciones",
    "https://cotizaciones.bcu.gub.uy/wscotizaciones/servlet/awsbcucotizaciones?wsdl",
    "cotizaciones.wsdl",
)

val taskWsdlDownloadBcuMonedas = registerWsdlDownloadTask(
    "wsdl-download-bcu-monedas",
    "https://cotizaciones.bcu.gub.uy/wscotizaciones/servlet/awsbcumonedas?wsdl",
    "monedas.wsdl",
)

val taskWsdlDownloadBcuUltimoCierre = registerWsdlDownloadTask(
    "wsdl-download-bcu-ultimocierre",
    "https://cotizaciones.bcu.gub.uy/wscotizaciones/servlet/awsultimocierre?wsdl",
    "ultimocierre.wsdl",
)

val taskWsdlDownload = tasks.register("wsdl-download") {
    dependsOn(
        taskWsdlDownloadBcuCotizaciones,
        taskWsdlDownloadBcuMonedas,
        taskWsdlDownloadBcuUltimoCierre,
    )
}

val taskWsImportBcuCotizaciones = registerWsImportTask(
    "wsimport-bcu-cotizaciones",
    "cotizaciones.wsdl",
    "xyz.keibit.bcu.wsdl.cotizaciones",
)

val taskWsImportBcuMonedas = registerWsImportTask(
    "wsimport-bcu-monedas",
    "monedas.wsdl",
    "xyz.keibit.bcu.wsdl.monedas",
)

val taskWsImportBcuUltimoCierre = registerWsImportTask(
    "wsimport-bcu-ultimocierre",
    "ultimocierre.wsdl",
    "xyz.keibit.bcu.wsdl.ultimocierre",
)

val taskWsImport = tasks.register("wsimport") {
    dependsOn(
        taskWsImportBcuCotizaciones,
        taskWsImportBcuMonedas,
        taskWsImportBcuUltimoCierre,
    )
}
