module xyz.keibit.bcu {
    requires com.sun.xml.ws;
    // BcuClientConfig.customizer expone jakarta.xml.ws.BindingProvider.
    requires transitive jakarta.xml.ws;
    requires static lombok;

    exports xyz.keibit.bcu;

    // JAXB y JAX-WS acceden por reflexión a las clases generadas por wsimport.
    opens xyz.keibit.bcu.wsdl.cotizaciones to
            com.sun.xml.bind,
            com.sun.xml.ws;
    opens xyz.keibit.bcu.wsdl.monedas to
            com.sun.xml.bind,
            com.sun.xml.ws;
    opens xyz.keibit.bcu.wsdl.ultimocierre to
            com.sun.xml.bind,
            com.sun.xml.ws;
}
