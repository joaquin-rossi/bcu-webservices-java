# BCU WebServices

> [!IMPORTANT]
> Sin asociación con el Banco Central del Uruguay.

Adapter de WebServices SOAP expuestos por el BCU para Java 25.

* WebService de Cotizaciones
* WebService de Monedas.
* WebService de Último Cierre.

Basado en https://www.bcu.gub.uy/Acerca-de-BCU/RD_Solicitudes_Informacion/Documentaci%C3%B3n-Agregada/PedroScheeffer169.pdf.

* El módulo `core` contiene el adapter `bcu.webservices.BcuService`, que utiliza el código generador por wsimport.
* El módulo `example` contiene un ejemplo de uso.

```
> ultimoCierre()
2025-10-10
```

```
> monedas(BcuMonedasRequest[grupo=CotizacionesLocales])
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
> cotizaciones(BcuCotizacionesRequest[monedas=[9800], fechaDesde=2025-10-03, fechaHasta=2025-10-10, grupo=CotizacionesLocales])
[
    BcuCotizacionesResponseLinea[fecha=2025-10-03, monedaCodigo=9800, monedaNombre=UNIDAD INDEXADA, monedaCodigoISO=U.I., monedaPaisEmisor=URUGUAY, tipoCambioCompra=6.3637, tipoCambioVenta=6.3637, arbitraje=0.159615, formaArbitrar=DolaresEEUUPorUnidadDeMoneda],
    BcuCotizacionesResponseLinea[fecha=2025-10-04, monedaCodigo=9800, monedaNombre=UNIDAD INDEXADA, monedaCodigoISO=U.I., monedaPaisEmisor=URUGUAY, tipoCambioCompra=6.3637, tipoCambioVenta=6.3637, arbitraje=0.159615, formaArbitrar=DolaresEEUUPorUnidadDeMoneda],
    BcuCotizacionesResponseLinea[fecha=2025-10-05, monedaCodigo=9800, monedaNombre=UNIDAD INDEXADA, monedaCodigoISO=U.I., monedaPaisEmisor=URUGUAY, tipoCambioCompra=6.3636, tipoCambioVenta=6.3636, arbitraje=0.159613, formaArbitrar=DolaresEEUUPorUnidadDeMoneda],
    BcuCotizacionesResponseLinea[fecha=2025-10-06, monedaCodigo=9800, monedaNombre=UNIDAD INDEXADA, monedaCodigoISO=U.I., monedaPaisEmisor=URUGUAY, tipoCambioCompra=6.3645, tipoCambioVenta=6.3645, arbitraje=0.159531, formaArbitrar=DolaresEEUUPorUnidadDeMoneda],
    BcuCotizacionesResponseLinea[fecha=2025-10-07, monedaCodigo=9800, monedaNombre=UNIDAD INDEXADA, monedaCodigoISO=U.I., monedaPaisEmisor=URUGUAY, tipoCambioCompra=6.3653, tipoCambioVenta=6.3653, arbitraje=0.159511, formaArbitrar=DolaresEEUUPorUnidadDeMoneda],
    BcuCotizacionesResponseLinea[fecha=2025-10-08, monedaCodigo=9800, monedaNombre=UNIDAD INDEXADA, monedaCodigoISO=U.I., monedaPaisEmisor=URUGUAY, tipoCambioCompra=6.3662, tipoCambioVenta=6.3662, arbitraje=0.159024, formaArbitrar=DolaresEEUUPorUnidadDeMoneda],
    BcuCotizacionesResponseLinea[fecha=2025-10-09, monedaCodigo=9800, monedaNombre=UNIDAD INDEXADA, monedaCodigoISO=U.I., monedaPaisEmisor=URUGUAY, tipoCambioCompra=6.367, tipoCambioVenta=6.367, arbitraje=0.158956, formaArbitrar=DolaresEEUUPorUnidadDeMoneda],
    BcuCotizacionesResponseLinea[fecha=2025-10-10, monedaCodigo=9800, monedaNombre=UNIDAD INDEXADA, monedaCodigoISO=U.I., monedaPaisEmisor=URUGUAY, tipoCambioCompra=6.3679, tipoCambioVenta=6.3679, arbitraje=0.158528, formaArbitrar=DolaresEEUUPorUnidadDeMoneda]
]
```
