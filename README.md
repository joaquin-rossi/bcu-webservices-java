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