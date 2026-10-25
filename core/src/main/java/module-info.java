module bcu.webservices {
    requires com.sun.xml.ws;
    requires static lombok;
    
    exports bcu.webservices;
    
    // JAXB y JAX-WS acceden por reflexión a las clases generadas por wsimport.
    opens bcu.wsdl.cotizaciones to com.sun.xml.bind, com.sun.xml.ws;
    opens bcu.wsdl.monedas to com.sun.xml.bind, com.sun.xml.ws;
    opens bcu.wsdl.ultimocierre to com.sun.xml.bind, com.sun.xml.ws;
}
