plugins {
    java
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

val jaxwsTools by configurations.creating
dependencies {
    annotationProcessor("org.projectlombok:lombok:1.18.42")
    compileOnly("org.projectlombok:lombok:1.18.42")
    implementation("com.sun.xml.ws:jaxws-rt:4.0.3")

    jaxwsTools("com.sun.xml.ws:jaxws-tools:4.0.3")
}

fun registerWsImportTask(taskName: String, wsdlUrl: String, pkg: String) =
    tasks.register<JavaExec>(taskName) {
        classpath = jaxwsTools
        mainClass.set("com.sun.tools.ws.WsImport")

        args(
            "-keep",
            "-Xnocompile",
            "-s", layout.projectDirectory.dir("src/main/java").asFile.absolutePath,
            "-p", pkg,
            "-wsdllocation", wsdlUrl,
            wsdlUrl
        )
    }

val taskWsImportBcuCotizaciones = registerWsImportTask(
    "wsimport-bcu-cotizaciones",
    "https://cotizaciones.bcu.gub.uy/wscotizaciones/servlet/awsbcucotizaciones?wsdl",
    "bcu.wsdl.cotizaciones",
)

val taskWsImportBcuMonedas = registerWsImportTask(
    "wsimport-bcu-monedas",
    "https://cotizaciones.bcu.gub.uy/wscotizaciones/servlet/awsbcumonedas?wsdl",
    "bcu.wsdl.monedas",
)

val taskWsImportBcuUltimoCierre = registerWsImportTask(
    "wsimport-bcu-ultimocierre",
    "https://cotizaciones.bcu.gub.uy/wscotizaciones/servlet/awsultimocierre?wsdl",
    "bcu.wsdl.ultimocierre",
)

val taskWsImport = tasks.register("wsimport") {
    dependsOn(
        taskWsImportBcuCotizaciones.get().path,
        taskWsImportBcuMonedas.get().path,
        taskWsImportBcuUltimoCierre.get().path
    )
}