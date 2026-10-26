# BCU WebServices

[![CI](https://github.com/joaquin-rossi/bcu-webservices-java/actions/workflows/ci.yml/badge.svg)](https://github.com/joaquin-rossi/bcu-webservices-java/actions/workflows/ci.yml) [![BCU](https://github.com/joaquin-rossi/bcu-webservices-java/actions/workflows/bcu.yml/badge.svg)](https://github.com/joaquin-rossi/bcu-webservices-java/actions/workflows/bcu.yml)

[![Maven Central](https://img.shields.io/maven-central/v/xyz.keibit/bcu-webservices)](https://central.sonatype.com/artifact/xyz.keibit/bcu-webservices) [![Javadoc](https://javadoc.io/badge2/xyz.keibit/bcu-webservices/javadoc.svg)](https://javadoc.io/doc/xyz.keibit/bcu-webservices)

> [!IMPORTANT]
> Sin asociación con el Banco Central del Uruguay.

Cliente Java para los webservices SOAP de cotizaciones del Banco Central del Uruguay:

* WebService de Cotizaciones.
* WebService de Monedas.
* WebService de Último Cierre.

Basado en https://www.bcu.gub.uy/Acerca-de-BCU/RD_Solicitudes_Informacion/Documentaci%C3%B3n-Agregada/PedroScheeffer169.pdf.

## Instalación

Requiere Java 25 o superior.

Gradle:

```kotlin
implementation("xyz.keibit:bcu-webservices:0.2.0")
```

Maven:

```xml
<dependency>
    <groupId>xyz.keibit</groupId>
    <artifactId>bcu-webservices</artifactId>
    <version>0.2.0</version>
</dependency>
```

Si se usa el module path, agregar `requires xyz.keibit.bcu;` al `module-info.java`.

## Uso

```java
var client = new BcuClient();

client.ultimoCierre();
// BcuUltimoCierreResponse[fecha=2026-10-23]

client.monedas(new BcuMonedasRequest(BcuMonedasRequest.Grupo.COTIZACIONES_LOCALES));
// BcuMonedasResponse[monedas=[
//     Moneda[codigo=501, nombre=PESO ARG.BILLETE],
//     Moneda[codigo=1001, nombre=REAL BILLETE],
//     Moneda[codigo=2224, nombre=DLS. USA CABLE],
//     Moneda[codigo=2225, nombre=DLS. USA BILLETE],
//     Moneda[codigo=2230, nombre=DLS.PROMED.FONDO],
//     Moneda[codigo=9700, nombre=UNIDAD PREVISIONAL],
//     Moneda[codigo=9800, nombre=UNIDAD INDEXADA],
//     Moneda[codigo=9900, nombre=UNIDAD REAJUSTAB]
// ]]

client.cotizaciones(new BcuCotizacionesRequest(
        List.of(2225, 9800), // DLS. USA BILLETE, UNIDAD INDEXADA
        LocalDate.of(2026, 10, 22),
        LocalDate.of(2026, 10, 23),
        BcuCotizacionesRequest.Grupo.COTIZACIONES_LOCALES
));
// BcuCotizacionesResponse[lineas=[
//     Linea[fecha=2026-10-22, monedaCodigo=2225, monedaNombre=DLS. USA BILLETE, monedaCodigoISO=DLS., monedaPaisEmisor=ESTADOS UNIDOS, tipoCambioCompra=40.083, tipoCambioVenta=40.083, arbitraje=1.0, formaArbitrar=UNIDAD_DE_MONEDA_POR_DOLARES_EEUU],
//     Linea[fecha=2026-10-23, monedaCodigo=2225, monedaNombre=DLS. USA BILLETE, monedaCodigoISO=DLS., monedaPaisEmisor=ESTADOS UNIDOS, tipoCambioCompra=40.078, tipoCambioVenta=40.078, arbitraje=1.0, formaArbitrar=UNIDAD_DE_MONEDA_POR_DOLARES_EEUU],
//     Linea[fecha=2026-10-22, monedaCodigo=9800, monedaNombre=UNIDAD INDEXADA, monedaCodigoISO=U.I., monedaPaisEmisor=URUGUAY, tipoCambioCompra=6.6734, tipoCambioVenta=6.6734, arbitraje=0.16649, formaArbitrar=DOLARES_EEUU_POR_UNIDAD_DE_MONEDA],
//     Linea[fecha=2026-10-23, monedaCodigo=9800, monedaNombre=UNIDAD INDEXADA, monedaCodigoISO=U.I., monedaPaisEmisor=URUGUAY, tipoCambioCompra=6.6745, tipoCambioVenta=6.6745, arbitraje=0.166538, formaArbitrar=DOLARES_EEUU_POR_UNIDAD_DE_MONEDA]
// ]]
```
