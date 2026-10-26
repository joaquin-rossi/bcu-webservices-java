# BCU WebServices

[![CI](https://github.com/joaquin-rossi/bcu-webservices-java/actions/workflows/ci.yml/badge.svg)](https://github.com/joaquin-rossi/bcu-webservices-java/actions/workflows/ci.yml)
[![BCU](https://github.com/joaquin-rossi/bcu-webservices-java/actions/workflows/bcu.yml/badge.svg)](https://github.com/joaquin-rossi/bcu-webservices-java/actions/workflows/bcu.yml)
[![Maven Central](https://img.shields.io/maven-central/v/xyz.keibit/bcu-webservices)](https://central.sonatype.com/artifact/xyz.keibit/bcu-webservices)

> [!IMPORTANT]
> Sin asociación con el Banco Central del Uruguay.

Adapter de WebServices SOAP expuestos por el BCU para Java 25.

* WebService de Cotizaciones
* WebService de Monedas.
* WebService de Último Cierre.

Basado en https://www.bcu.gub.uy/Acerca-de-BCU/RD_Solicitudes_Informacion/Documentaci%C3%B3n-Agregada/PedroScheeffer169.pdf.

* El módulo `core` contiene el cliente `xyz.keibit.bcu.BcuClient`, que utiliza el código generado por wsimport.
* El módulo `example` contiene un ejemplo de uso.

## Instalación

Requiere Java 25 o superior.

Gradle:

```kotlin
implementation("xyz.keibit:bcu-webservices:0.1.0")
```

Maven:

```xml
<dependency>
    <groupId>xyz.keibit</groupId>
    <artifactId>bcu-webservices</artifactId>
    <version>0.1.0</version>
</dependency>
```

Si se usa el module path, agregar `requires xyz.keibit.bcu;` al `module-info.java`.

## Desarrollo

Las versiones de Java y Gradle están fijadas en `mise.toml`.

```
gradle build                  # compila, verifica el formato y corre los tests unitarios
gradle spotlessApply          # formatea el código
gradle :core:integrationTest  # tests contra los webservices reales del BCU
```

## Ejemplos

```
> ultimoCierre()
2025-10-10
```

```
> monedas(BcuMonedasRequest[grupo=COTIZACIONES_LOCALES])
[
    BcuMonedasResponseMoneda[codigo=501, nombre=PESO ARG.BILLETE],
    BcuMonedasResponseMoneda[codigo=1001, nombre=REAL BILLETE],
    BcuMonedasResponseMoneda[codigo=2224, nombre=DLS. USA CABLE],
    BcuMonedasResponseMoneda[codigo=2225, nombre=DLS. USA BILLETE],
    BcuMonedasResponseMoneda[codigo=2230, nombre=DLS.PROMED.FONDO],
    BcuMonedasResponseMoneda[codigo=9700, nombre=UNIDAD PREVISIONAL],
    BcuMonedasResponseMoneda[codigo=9800, nombre=UNIDAD INDEXADA],
    BcuMonedasResponseMoneda[codigo=9900, nombre=UNIDAD REAJUSTAB]
]
```

```
> cotizaciones(BcuCotizacionesRequest[monedas=[9800], fechaDesde=2025-10-03, fechaHasta=2025-10-10, grupo=COTIZACIONES_LOCALES])
[
    BcuCotizacionesResponseLinea[fecha=2025-10-03, monedaCodigo=9800, monedaNombre=UNIDAD INDEXADA, monedaCodigoISO=U.I., monedaPaisEmisor=URUGUAY, tipoCambioCompra=6.3637, tipoCambioVenta=6.3637, arbitraje=0.159615, formaArbitrar=DOLARES_EEUU_POR_UNIDAD_DE_MONEDA],
    BcuCotizacionesResponseLinea[fecha=2025-10-04, monedaCodigo=9800, monedaNombre=UNIDAD INDEXADA, monedaCodigoISO=U.I., monedaPaisEmisor=URUGUAY, tipoCambioCompra=6.3637, tipoCambioVenta=6.3637, arbitraje=0.159615, formaArbitrar=DOLARES_EEUU_POR_UNIDAD_DE_MONEDA],
    BcuCotizacionesResponseLinea[fecha=2025-10-05, monedaCodigo=9800, monedaNombre=UNIDAD INDEXADA, monedaCodigoISO=U.I., monedaPaisEmisor=URUGUAY, tipoCambioCompra=6.3636, tipoCambioVenta=6.3636, arbitraje=0.159613, formaArbitrar=DOLARES_EEUU_POR_UNIDAD_DE_MONEDA],
    BcuCotizacionesResponseLinea[fecha=2025-10-06, monedaCodigo=9800, monedaNombre=UNIDAD INDEXADA, monedaCodigoISO=U.I., monedaPaisEmisor=URUGUAY, tipoCambioCompra=6.3645, tipoCambioVenta=6.3645, arbitraje=0.159531, formaArbitrar=DOLARES_EEUU_POR_UNIDAD_DE_MONEDA],
    BcuCotizacionesResponseLinea[fecha=2025-10-07, monedaCodigo=9800, monedaNombre=UNIDAD INDEXADA, monedaCodigoISO=U.I., monedaPaisEmisor=URUGUAY, tipoCambioCompra=6.3653, tipoCambioVenta=6.3653, arbitraje=0.159511, formaArbitrar=DOLARES_EEUU_POR_UNIDAD_DE_MONEDA],
    BcuCotizacionesResponseLinea[fecha=2025-10-08, monedaCodigo=9800, monedaNombre=UNIDAD INDEXADA, monedaCodigoISO=U.I., monedaPaisEmisor=URUGUAY, tipoCambioCompra=6.3662, tipoCambioVenta=6.3662, arbitraje=0.159024, formaArbitrar=DOLARES_EEUU_POR_UNIDAD_DE_MONEDA],
    BcuCotizacionesResponseLinea[fecha=2025-10-09, monedaCodigo=9800, monedaNombre=UNIDAD INDEXADA, monedaCodigoISO=U.I., monedaPaisEmisor=URUGUAY, tipoCambioCompra=6.367, tipoCambioVenta=6.367, arbitraje=0.158956, formaArbitrar=DOLARES_EEUU_POR_UNIDAD_DE_MONEDA],
    BcuCotizacionesResponseLinea[fecha=2025-10-10, monedaCodigo=9800, monedaNombre=UNIDAD INDEXADA, monedaCodigoISO=U.I., monedaPaisEmisor=URUGUAY, tipoCambioCompra=6.3679, tipoCambioVenta=6.3679, arbitraje=0.158528, formaArbitrar=DOLARES_EEUU_POR_UNIDAD_DE_MONEDA]
]
```
