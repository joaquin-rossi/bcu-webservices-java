import java.net.URI

plugins {
    `java-library`
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

val jaxwsTools = configurations.create("jaxwsTools")
dependencies {
    annotationProcessor(libs.lombok)
    compileOnly(libs.lombok)
    api(libs.jakarta.xml.ws.api)
    implementation(libs.jaxws.rt)

    jaxwsTools(libs.jaxws.tools)
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
    "bcu.wsdl.cotizaciones",
)

val taskWsImportBcuMonedas = registerWsImportTask(
    "wsimport-bcu-monedas",
    "monedas.wsdl",
    "bcu.wsdl.monedas",
)

val taskWsImportBcuUltimoCierre = registerWsImportTask(
    "wsimport-bcu-ultimocierre",
    "ultimocierre.wsdl",
    "bcu.wsdl.ultimocierre",
)

val taskWsImport = tasks.register("wsimport") {
    dependsOn(
        taskWsImportBcuCotizaciones,
        taskWsImportBcuMonedas,
        taskWsImportBcuUltimoCierre,
    )
}
